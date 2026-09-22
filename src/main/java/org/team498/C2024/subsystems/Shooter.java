package org.team498.C2024.subsystems;

import org.team498.C2024.Constants;
import org.team498.C2024.Ports;
import org.team498.C2024.Robot;
import org.team498.C2024.RobotPosition;
import org.team498.C2024.ShooterUtil;
import org.team498.C2024.State;
import org.team498.C2024.StateController;
import org.team498.C2024.Constants.ShooterConstants;
import org.team498.C2024.Constants.ShooterConstants.AngleConstants;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.AbsoluteSensorRangeValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.interpolation.TimeInterpolatableBuffer;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
/*
 * This is an example Flywheel subsystem that can be used for reference while writing your own subsystems :)
 * The mechanism uses two NEO brushless motors controlled by Spark Maxes
 * They are mounted opposite each other and power a single-wheel flywheel with velocity control
 * While this code is for a Flywheel, the ideas here can be used for any velocity-based subsystem
 */
public class Shooter extends SubsystemBase {
    // all variable/object declaration goes at the top of the class. 
    // They can be instantiated (given values) later, but they must be declared here

    // Motors will almost always be private because they will only be controlled using public methods. There should be NO global use where motors 
    private final TalonFX leftMotor; // Declaration for a NEO or NEO550 brushless motor
    private final TalonFX rightMotor; // We use left / right descriptors to make identification easier in testing and communication
    private final TalonFX angleMotor; //Declaration for a Built-in NEO/NEO550 encoder
    private final TalonFX feedMotor;
    
    private final CANcoder angleEncoder;

    private final PIDController rightController; //Declaration for a P Controller
    private final PIDController leftController; 
    private final PIDController feedController;
    private final PIDController angleController;

    private final SimpleMotorFeedforward rightFeedForward;
    private final SimpleMotorFeedforward leftFeedForward;
    private final SimpleMotorFeedforward feedFeedforward;
    private final ArmFeedforward angleFeedForward;

    // Variables will store the current properties of the subsystem
    private double rightSpeed;
    private double leftSpeed;
    private double angle;
    private double feedSpeed;
    private boolean isManual;
    private double manualSpeed;
    private double manualSpeedRight;
    private double manualSpeedLeft;
    private double manualSpeedFeed;
    private State.Shooter currentState;
    private double previousSpeakerDistance;

    private boolean isActivated = true;
    public TimeInterpolatableBuffer<Double> angleHistory = TimeInterpolatableBuffer.createDoubleBuffer(3);
    
    // Constructor: Configure Motor Controller settings and  
    // Instantiate all objects (assign values to every variable and object)
    public Shooter() {
        leftMotor = new TalonFX(Ports.ShooterPorts.LEFT_MOTOR);
        rightMotor = new TalonFX(Ports.ShooterPorts.RIGHT_MOTOR);
        feedMotor = new TalonFX(Ports.ShooterPorts.FEED_MOTOR);
        angleMotor = new TalonFX(Ports.ShooterPorts.ANGLE_MOTOR); //this can be left or right motor, whichever is most convenient
        
        angleEncoder = new CANcoder(Ports.ShooterPorts.ANGLE_ENCODER);

        // Use the subsystems constants to instantiate PID and Feedforward
        rightController = new PIDController(Constants.ShooterConstants.P,Constants.ShooterConstants.I, Constants.ShooterConstants.D);
        leftController = new PIDController(Constants.ShooterConstants.P,Constants.ShooterConstants.I, Constants.ShooterConstants.D);
        feedController = new PIDController(Constants.ShooterConstants.fP, Constants.ShooterConstants.fI, Constants.ShooterConstants.fD);
        angleController = new PIDController(Constants.ShooterConstants.AngleConstants.P, Constants.ShooterConstants.AngleConstants.I, Constants.ShooterConstants.AngleConstants.D);

        rightFeedForward = new SimpleMotorFeedforward(Constants.ShooterConstants.S, Constants.ShooterConstants.V, Constants.ShooterConstants.A);
        leftFeedForward = new SimpleMotorFeedforward(Constants.ShooterConstants.S, Constants.ShooterConstants.V, Constants.ShooterConstants.A);
        feedFeedforward = new SimpleMotorFeedforward(0, ShooterConstants.fV, 0);
        angleFeedForward = new ArmFeedforward(Constants.ShooterConstants.AngleConstants.S, Constants.ShooterConstants.AngleConstants.G, Constants.ShooterConstants.AngleConstants.V);

        // Instantiate variables to intitial values
        currentState = State.Shooter.IDLE;
        rightSpeed = State.Shooter.IDLE.speed;
        leftSpeed = State.Shooter.IDLE.speed;
        feedSpeed = State.Shooter.IDLE.feedSpeed;
        angle = State.Shooter.IDLE.angle;

        rightController.setTolerance(300);
        leftController.setTolerance(300);
        feedController.setTolerance(500);
        angleController.setTolerance(0.5);

        // reset motor defaults to ensure all settings are clear
        TalonFXConfiguration flywheelConfig = new TalonFXConfiguration();
        flywheelConfig.CurrentLimits.SupplyCurrentLimit = 70;
        flywheelConfig.CurrentLimits.StatorCurrentLimit = 60;
        flywheelConfig.CurrentLimits.StatorCurrentLimitEnable = true;
        flywheelConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
        rightMotor.getConfigurator().apply(flywheelConfig);
        leftMotor.getConfigurator().apply(flywheelConfig);
        angleMotor.getConfigurator().apply(new TalonFXConfiguration());

        CANcoderConfiguration canCoderConfig = new CANcoderConfiguration();
        canCoderConfig.MagnetSensor.MagnetOffset = Constants.ShooterConstants.AngleConstants.ANGLE_OFFSET;
        canCoderConfig.MagnetSensor.AbsoluteSensorRange = AbsoluteSensorRangeValue.Unsigned_0To1;
        angleEncoder.getConfigurator().apply(canCoderConfig);
    }

    public void configMotors() {
        angleMotor.setNeutralMode(NeutralModeValue.Brake);
    }
    double llSavedAngle = State.Shooter.PODIUM.angle;
    // This method will run every 10-20 milliseconds (about 50-100 times in one second)
    @Override
    public void periodic() {
        SmartDashboard.putNumber("Shooter Angle", getAngle());
        SmartDashboard.putNumber("left flywheel speed", getLeftSpeedRPM());
        SmartDashboard.putNumber("right flywheel speed", getRightSpeedRPM());
        SmartDashboard.putNumber("left flywheel setpoint", leftSpeed);
        SmartDashboard.putNumber("right flywheel setpoint", rightSpeed);
        SmartDashboard.putNumber("feed flywheel speed", getFeedSpeedRPM());
        SmartDashboard.putNumber("feed flywheel setpoint", feedSpeed);
        
        SmartDashboard.putNumber("Adjusted Angle", calculateAngle(RobotPosition.speakerDistance()));
        SmartDashboard.putNumber("Adjusted Velocity", calculateSpeed(RobotPosition.speakerDistance()));
        // This condition will reduce CPU utilization when the motor is not meant to run and save power because 
        // it will not actively deccelerate the wheel
        angleHistory.addSample(Timer.getFPGATimestamp(), getAngle());
        if (currentState == State.Shooter.CRESCENDO){
            double speakerDistance = RobotPosition.distanceToSpeaker();
            if (speakerDistance == 0) {
                speakerDistance = previousSpeakerDistance;
            } else {
                previousSpeakerDistance = speakerDistance;
            }
            double expectedAngle = calculateAngle(speakerDistance);
            if (expectedAngle > 25) {
                llSavedAngle = expectedAngle;
            }
            this.angle = llSavedAngle;
            this.leftSpeed = calculateSpeed(speakerDistance);
            this.rightSpeed = this.leftSpeed - ShooterConstants.SPIN_DIFF;
            // this.feedSpeed = currentState.feedSpeed;//feedController.calculate(getFeedSpeedRPM(), feedSpeed) + feedFeedforward.calculate(feedSpeed);
            // this.angle = calculateAngle(RobotPosition.speakerDistance());
        }

        else if (currentState == State.Shooter.VISION) {
            this.angle = RobotPosition.calculateLimelightAngleToNote(StateController.getInstance().getNote());
        }
        if (this.angle > ShooterConstants.AngleConstants.MAX_ANGLE) {
            this.angle = ShooterConstants.AngleConstants.MAX_ANGLE;
        // } else if (this.angle < ShooterConstants.AngleConstants.MIN_ANGLE && Intake.getInstance().getState() == State.Intake.IDLE) {
        //     this.angle = ShooterConstants.AngleConstants.MIN_ANGLE;
        } else if (this.angle < ShooterConstants.AngleConstants.MIN_ANGLE && Intake.getInstance().getState() == State.Intake.IDLE) {
            this.angle = ShooterConstants.AngleConstants.MIN_ANGLE;
        } else if(this.angle < ShooterConstants.AngleConstants.AUTO_MIN_ANGLE && Intake.getInstance().getState() != State.Intake.IDLE) {
            this.angle = ShooterConstants.AngleConstants.AUTO_MIN_ANGLE;
        }

        if (currentState.speed == 0) {
            //IF State is IDLE, don't Spin the right wheel
            this.leftSpeed = currentState.speed;
            this.rightSpeed = currentState.speed;
        }

        double rightShooterSpeed = 0;
        double leftShooterSpeed = 0; // We will use this variable to keep track of our desired speed
        double feedShooterSpeed = 0;
        double angleSpeed = 0;

        if (isActivated) {
            leftShooterSpeed = leftController.calculate(getLeftSpeedRPM(), leftSpeed) + leftFeedForward.calculate(leftSpeed);
            rightShooterSpeed = rightController.calculate(getRightSpeedRPM(), rightSpeed) + rightFeedForward.calculate(rightSpeed);
            feedShooterSpeed = feedController.calculate(getFeedSpeedRPM(), feedSpeed) + feedFeedforward.calculate(feedSpeed);
            angleSpeed = angleController.calculate(getAngle(), this.angle);

            if (leftSpeed == 0) {
                leftShooterSpeed = 0;
                rightShooterSpeed = 0;
            }
        }

        if (isManual) {
            angleSpeed = manualSpeed;
            rightShooterSpeed = manualSpeedRight;
            leftShooterSpeed = manualSpeedLeft;
            feedShooterSpeed = manualSpeedFeed;

        }
            
        
        if ((getAngle() < AngleConstants.MIN_ANGLE && angleSpeed < 0) || (getAngle() > AngleConstants.MAX_ANGLE && angleSpeed > 0)) {
            rightShooterSpeed = 0;
            leftShooterSpeed = 0;
            feedShooterSpeed = 0;
            angleSpeed = 0;
        }
        // set(manualSpeedRight, manualSpeedLeft);
        set(rightShooterSpeed, leftShooterSpeed); // set the motor behavior using set() to interact with the controllers
        setFeed(feedShooterSpeed);
        setAngle(angleSpeed + angleFeedForward.calculate(Units.degreesToRadians(getAngle()), 0));
        // We divide by MAX_RPM to scale to {-1, 1}
    }
    
    //sets top speed and bottom speed
    private void set(double rightSpeed, double leftSpeed) {
        leftMotor.set(leftSpeed);
        rightMotor.set(-rightSpeed); // invert speed on right side (assuming the motor is facing opposite the left)
    }

    //sets speed for feedForward
    public void setFeed(double speed){
        feedMotor.set(-speed);
    }
    
    //sets speed for angleMotor
    public void setAngle(double speed){
        angleMotor.set(-speed);
    }

    public void setAngleManual(boolean isManual, double speed, double flywheelSpeed){
        this.isManual = isManual;
        this.manualSpeed = speed;
        this.manualSpeedRight = flywheelSpeed;
        this.manualSpeedLeft = flywheelSpeed;
        this.manualSpeedFeed = flywheelSpeed == 0.0 ? 0.0 : 1.0;
    }

    /**
     * sets states for PID controllers and motor speeds
     */
    public void setState(State.Shooter state) {
        isActivated = true;
        currentState = state; // update state
        leftSpeed = state.speed;
        rightSpeed = state.speed - ShooterConstants.SPIN_DIFF;
        feedSpeed = state.feedSpeed;
        angle = state.angle;
        rightController.setSetpoint(this.leftSpeed); // update pController
        leftController.setSetpoint(this.rightSpeed);
        feedController.setSetpoint(this.feedSpeed);
        angleController.setSetpoint(this.angle);
    }

    // Getter method to retrieve current State
    public State.Shooter getState() {
        return currentState;
    }

    public boolean isSubwoofer(){
        return State.Shooter.SUBWOOFER == currentState;
    }

    /**
     * returns leftController, rightController, and angleController setpoints
     */
    public boolean atSetpoint(){
        //return angleController.atSetpoint();
         return leftController.atSetpoint() && rightController.atSetpoint();// && angleController.atSetpoint();
    }

    /**
     * returns angle of angleEncoder
     */
    public double getAngle() {
        return angleEncoder.getAbsolutePosition().getValueAsDouble() * 360;
    }
    public double getAngle(double timestamp) {
        return angleHistory.getSample(timestamp).orElseGet(this::getAngle);
    }
    public double getLeftSpeedMPS() {
        return leftMotor.getVelocity().getValueAsDouble() * ShooterConstants.GEAR_RATIO * ShooterConstants.CIRCUMFERENCE;
    }

    public double getLeftSpeedRPM() {
        return leftMotor.getVelocity().getValueAsDouble() * 60 * ShooterConstants.GEAR_RATIO;
    }
    public double getRightSpeedMPS() {
        return -rightMotor.getVelocity().getValueAsDouble() * ShooterConstants.GEAR_RATIO * ShooterConstants.CIRCUMFERENCE;
    }
    public double getRightSpeedRPM() {
        return -rightMotor.getVelocity().getValueAsDouble() * 60 * ShooterConstants.GEAR_RATIO;
    }

    public double getFeedSpeedRPM() {
        return -feedMotor.getVelocity().getValueAsDouble();
    }

    public double getTimeOfFlight() {
        // double yVelocity = getRightSpeedMPS() * Math.sin(Math.toRadians(getAngle()));
        double yVelocity = RPM_to_MPS(2900) * Math.sin(Math.toRadians(getAngle()));
        double distanceToSpeaker = RobotPosition.distanceToSpeakerStatic();
        return (distanceToSpeaker / yVelocity) + RobotPosition.defaultTOF;
        // return RobotPosition.defaultTOF;
    }

    public boolean shooterState(){
        return getState() == State.Shooter.CRESCENDO || getState() == State.Shooter.PODIUM || getState() == State.Shooter.SUBWOOFER;
    }

    private static double RPM_to_MPS(double rpm) {
        return (rpm / 60.0) * ShooterConstants.CIRCUMFERENCE;
    }
    private static double MPS_to_RPM (double mps) {
        return (mps * 60.0) / ShooterConstants.CIRCUMFERENCE;
    }


    // private double calculateRightSpeed(double distance){
    //     double v = 0;
    //     double theta = 0;
    //     return offsetVelocity(RobotPosition.getSpeakerRelativeVelocity(), v, theta);
    // }

    private double calculateSpeed(double distance){
        double v = RPM_to_MPS(ShooterUtil.getShooterSpeed(distance));
        double theta = this.angle;
        return MPS_to_RPM(offsetVelocity(RobotPosition.getSpeakerRelativeVelocity(), v, theta));
    }    
    
    // private double calculateFeedSpeed(double distance){
    //     return 0;
    // }

    private double calculateAngle(double distance){
        if (distance > 4.5)
        return ShooterConstants.AngleConstants.MIN_ANGLE;
        
        
        
        double v = RPM_to_MPS(currentState.speed);
        double theta = ShooterUtil.getShooterAngle(distance);
        // double c1 = 71.381;
        // double c2 = -26.351;
        // double c3 = 6.056;
        // double c4 = -0.533;
        // double theta = c4 * Math.pow(distance, 3) + c3 * distance * distance + c2 * distance + c1; //angle in degrees as given in team498/notebook/shooter_model.ipynb
        // if (distance > 5) {
        //     theta = 40;
        // }
        return offsetAngle(RobotPosition.getSpeakerRelativeVelocity(), v, theta);
    }
    private double offsetAngle(double r, double v, double theta) {
        return Units.radiansToDegrees(Math.atan((v * Math.sin(Units.degreesToRadians(theta))) / (v * Math.cos(Units.degreesToRadians(theta)) - r)));
    }
    private double offsetVelocity(double r, double v, double theta) {
        return Math.sqrt(Math.pow(v * Math.sin(Units.degreesToRadians(theta)), 2) + Math.pow(v * Math.cos(Units.degreesToRadians(theta)) - r, 2));
    }
    
    // Using static instances to reference the flywheel object ensures that we only use ONE FLywheel throughout the code 
    // This makes it very easy to access the flywheel object
    private static Shooter instance;

    public static Shooter getInstance() {
        if (instance == null) instance = new Shooter(); // Make sure there is an instance (this will only run once)
        return instance;
    }
}

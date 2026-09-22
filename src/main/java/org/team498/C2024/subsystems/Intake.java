package org.team498.C2024.subsystems;

import org.team498.C2024.Constants;
import org.team498.C2024.Ports;
import org.team498.C2024.State;
import org.team498.C2024.Constants.IntakeConstants;
import org.team498.C2024.Constants.ShooterConstants;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DutyCycle;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase {

    private final TalonFX motor;
    private final DutyCycle angleEncoder;

    private final PIDController pidController;
    private final ArmFeedforward gravityFeedforward;
    private final SimpleMotorFeedforward rotationFeedforward;

    private boolean isManual;
    private double manualSpeed;
    
    private State.Intake currentState;
    private double setpoint;

    private boolean isActivated = true;
    
    // Constructor: Configure Motor Controller settings and  
    // Instantiate all objects (assign values to every variable and object)
    public Intake() {
        // motor = new LazySparkMax(Ports.IntakePorts.LMOTOR, MotorType.kBrushless);
        motor = new TalonFX(Ports.IntakePorts.MOTOR);
        angleEncoder = new DutyCycle(new DigitalInput(Ports.IntakePorts.ANGLE_ENCODER));

        motor.getConfigurator().apply(new CurrentLimitsConfigs().withStatorCurrentLimit(60).withStatorCurrentLimitEnable(true));

        pidController = new PIDController(IntakeConstants.P, IntakeConstants.I, IntakeConstants.D);
        gravityFeedforward = new ArmFeedforward(IntakeConstants.S, IntakeConstants.G, IntakeConstants.V);
        rotationFeedforward = new SimpleMotorFeedforward(0, IntakeConstants.rV, 0);

        // Instantiate variables to intitial values
        currentState = State.Intake.IDLE;
        setpoint = currentState.speed;

        pidController.setTolerance(0.03);
        pidController.reset();
        pidController.setSetpoint(setpoint);

        // reset motor defaults to ensure all settings are clear
       
    }
    public void configMotors() {
        motor.getConfigurator().apply(new TalonFXConfiguration());
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("Intake Position", getPosition());
        
        SmartDashboard.putNumber("Manual Intake Speed", manualSpeed);
        SmartDashboard.putString("Intake State", currentState.name());
        // We will use this variable to keep track of our desired speed
        double speed = 0;
        if (isActivated) {
            double rotation = CommandSwerveDrivetrain.getInstance().getState().speeds.omegaRadiansPerSecond;
            //double driveAccel = Drivetrain.getInstance().getRobotRelativeYAcceleration();

            double initialPID = pidController.calculate(getPosition(), this.setpoint);
            double gravityOffset = gravityFeedforward.calculate(getPosition(), 0);
            //double driveOffset = driveFeedforward.calculate(driveAccel);
            double rotationOffset = rotationFeedforward.calculate(Math.abs(rotation));

            speed = initialPID + gravityOffset; // adjust for feedback error using proportional gain
        }
        if (Shooter.getInstance().getAngle() < ShooterConstants.AngleConstants.MIN_ANGLE - 1) {
            speed = 0;
        }
        // if (Shooter.getInstance().getAngle() < ShooterConstants.AngleConstants.MIN_ANGLE - 1) //Tolerance for Intake Angle
        //     speed = 0;
        if (isManual) speed = manualSpeed;
        set(speed);
    }

    /**
     * sets Motor Speed
     */
    public void set(double speed) {
        motor.set(speed);
    }

    /**
     * sets state for speed and sets PID controller to setpoint
     */
    public void setState(State.Intake state) {
        SmartDashboard.putNumber("Intake Setpoint", setpoint);
        isActivated = true;
        currentState = state;
        setpoint = state.speed; // update state
        pidController.setSetpoint(this.setpoint); // update pController
    }

    public void setPositionManual(boolean isManual, double speed){
        this.isManual = isManual;
        this.manualSpeed = speed / 5;
    }

    // Getter method to retrieve current State
    public State.Intake getState() {
        return currentState;
    }

    /**
     *returns PID controller when it reaches setpoint
     */
    public boolean atSetpoint(){
        return pidController.atSetpoint();
    }

    /**
     * returns encoder angle
     */
    public double getPosition() {
        return (getRawEncoder() * Math.PI * 2 - Constants.IntakeConstants.ENCODER_OFFSET); // -(getRawEncoder())
    }

    public double getRawEncoder(){
            double angle = angleEncoder.getOutput() + 0.8;
            if (angle < 1)
                angle += 1;
    
            return angle + 0.4;
    }
    
    // Using static instances to reference the flywheel object ensures that we only use ONE FLywheel throughout the code 
    // This makes it very easy to access the flywheel object
    private static Intake instance;

    public static Intake getInstance() {
        if (instance == null) instance = new Intake(); // Make sure there is an instance (this will only run once)
        return instance;
    }
}

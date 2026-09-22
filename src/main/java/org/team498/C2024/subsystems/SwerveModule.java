package org.team498.C2024.subsystems;

import static org.team498.C2024.Constants.DrivetrainConstants.DRIVE_WHEEL_CIRCUMFERENCE;
import static org.team498.C2024.Constants.DrivetrainConstants.MK4I_DRIVE_REDUCTION_L3;
import static org.team498.C2024.Constants.DrivetrainConstants.MK4I_STEER_REDUCTION_L3;

import org.team498.C2024.Constants;
import org.team498.C2024.Constants.DrivetrainConstants;
import org.team498.lib.util.Falcon500Conversions;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.AbsoluteSensorRangeValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.util.Units;
//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SwerveModule {
    private final TalonFX drive;
    private final TalonFX steer;
    private final CANcoder encoder;

    private SwerveModuleState currentTarget = new SwerveModuleState();
    double angleOffset;
    public SwerveModule(int driveID, int steerID, int encoderID, String canBus, double angleOffset){
        drive = new TalonFX(driveID,canBus);
        steer = new TalonFX(steerID,canBus);
        encoder = new CANcoder(encoderID,canBus);
        this.angleOffset = angleOffset;
        configDriveMotor(drive);
        configSteerMotor(steer);
        configCANCoder(encoder);
        enableBrakeMode(true);
    }

    //sets drive and steer motors to brake mode
    public void enableBrakeMode(boolean setBrake) {
        drive.setNeutralMode(setBrake ? NeutralModeValue.Brake : NeutralModeValue.Coast);
        steer.setNeutralMode(setBrake ? NeutralModeValue.Brake : NeutralModeValue.Coast);
    }

    public double driveSetpoint = 0;
    public double steerSetpoint = 0;
    /**
     * sets swerve module state and sets motor setpoints
     */
    public void setState(SwerveModuleState state) {
        currentTarget = optimize(state, Falcon500Conversions.falconToDegrees(steer.getPosition().getValueAsDouble(), MK4I_STEER_REDUCTION_L3));
        driveSetpoint = currentTarget.speedMetersPerSecond * MK4I_DRIVE_REDUCTION_L3 / Units.inchesToMeters(DRIVE_WHEEL_CIRCUMFERENCE);
        steerSetpoint = Falcon500Conversions.degreesToFalcon(currentTarget.angle.getDegrees(), MK4I_STEER_REDUCTION_L3);
        //sets setpoints for drive and steer motors
        drive.setControl(new VelocityVoltage(driveSetpoint));
        steer.setControl(new PositionVoltage(steerSetpoint));
    }

    /**
     * sets positions for encoders
    */
    public void updateIntegratedEncoder() {
        steer.setPosition(Falcon500Conversions.degreesToFalcon(getAngle(), MK4I_STEER_REDUCTION_L3), Constants.CAN_TIMEOUT_SECONDS);
        steer.setControl(new PositionVoltage(Falcon500Conversions.degreesToFalcon(getAngle(), MK4I_STEER_REDUCTION_L3)));
    }
    public void updateIntegratedEncoder(double updatevalue) {
        steer.setPosition(updatevalue, Constants.CAN_TIMEOUT_SECONDS);
        steer.setControl(new PositionVoltage(updatevalue));
    }

    /**
     * returns current speed in meters per second
     */
    public double getSpeed(){
        return currentTarget.speedMetersPerSecond;
    }

    /**
     * returns velocity of drive motors
     */
    public double getDriveMotorSpeed() {
        return drive.getVelocity().getValueAsDouble() * Units.inchesToMeters(DRIVE_WHEEL_CIRCUMFERENCE) / DrivetrainConstants.MK4I_DRIVE_REDUCTION_L3;
    }

    /**
     * returns swerve module position in degrees
     */
    public double getPosition(){
        return Falcon500Conversions.falconToDegrees(drive.getPosition().getValueAsDouble(), MK4I_DRIVE_REDUCTION_L3) / 360 * Units.inchesToMeters(DRIVE_WHEEL_CIRCUMFERENCE);
    }

   /**
    * returns angle in degrees
    */
    public double getAngle(){
        return encoder.getAbsolutePosition().getValueAsDouble() * 360;
    }

    public double getYAcceleration() {
        return drive.getAcceleration().getValueAsDouble() * Math.cos(Math.toRadians(getAngle()));
    }
    
    // Custom optimize method by team 364
    private SwerveModuleState optimize(SwerveModuleState desiredState, double currentAngle) {
        double targetAngle = placeInAppropriate0To360Scope(currentAngle, desiredState.angle.getDegrees());

        double targetSpeed = desiredState.speedMetersPerSecond;

        double delta = targetAngle - currentAngle;

        if (Math.abs(delta) > 90) {
            targetSpeed = -targetSpeed;
            if (delta > 90) {targetAngle -= 180;} else {targetAngle += 180;}
        }
        return new SwerveModuleState(targetSpeed, Rotation2d.fromDegrees(targetAngle));
    }

    private double placeInAppropriate0To360Scope(double scopeReference, double newAngle) {
        double lowerBound;
        double upperBound;
        double lowerOffset = scopeReference % 360;
        if (lowerOffset >= 0) {
            lowerBound = scopeReference - lowerOffset;
            upperBound = scopeReference + (360 - lowerOffset);
        } else {
            upperBound = scopeReference - lowerOffset;
            lowerBound = scopeReference - (360 + lowerOffset);
        }
        while (newAngle < lowerBound) {
            newAngle += 360;
        }
        while (newAngle > upperBound) {
            newAngle -= 360;
        }
        if (newAngle - scopeReference > 180) {
            newAngle -= 360;
        } else if (newAngle - scopeReference < -180) {
            newAngle += 360;
        }
        return newAngle;
    }
     private void configDriveMotor(TalonFX motor) {
       // motor.configFactoryDefault();

        TalonFXConfiguration driveConfig = new TalonFXConfiguration();
        driveConfig.CurrentLimits.SupplyCurrentLimit = 50;//35
        driveConfig.CurrentLimits.StatorCurrentLimit = 50;//45
        driveConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
        driveConfig.CurrentLimits.StatorCurrentLimitEnable = true;
        driveConfig.Slot0.kP = 0.3;//0.025;
        driveConfig.Slot0.kI = 0.0;
        driveConfig.Slot0.kD = 0.0;//0.5;
        driveConfig.Slot0.kV = 0.14;
        driveConfig.Slot0.kA = 0.012754;
        
        //TODO: ADD FEET FOWARD
        driveConfig.OpenLoopRamps.VoltageOpenLoopRampPeriod = 1;
        motor.getConfigurator().apply(driveConfig);
        motor.setInverted(false);
        motor.setNeutralMode(NeutralModeValue.Brake);
        
        motor.getPosition().setUpdateFrequency(200, Constants.CAN_TIMEOUT_SECONDS);
        motor.getVelocity().setUpdateFrequency(200, Constants.CAN_TIMEOUT_SECONDS);
        motor.getAcceleration().setUpdateFrequency(200, Constants.CAN_TIMEOUT_SECONDS);
    }

    private void configSteerMotor(TalonFX motor) {
       // motor.configFactoryDefault();

        TalonFXConfiguration steerConfig = new TalonFXConfiguration();
        steerConfig.CurrentLimits.SupplyCurrentLimit = 35;
        steerConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
        steerConfig.Slot0.kP = 2.6; //0.2
        steerConfig.Slot0.kI = 0.0;
        steerConfig.Slot0.kD = 0.2; // 0.1
        steerConfig.OpenLoopRamps.VoltageOpenLoopRampPeriod = 1;
        motor.getConfigurator().apply(steerConfig);
        motor.setInverted(true);
        motor.setNeutralMode(NeutralModeValue.Brake);
        motor.getPosition().setUpdateFrequency(200, Constants.CAN_TIMEOUT_SECONDS);
        // motor.getVelocity().setUpdateFrequency(100);
    }

    private void configCANCoder(CANcoder CANCoder) {
        //CANCoder.configFactoryDefault();
        CANcoderConfiguration encoderConfig = new CANcoderConfiguration();
        encoderConfig.MagnetSensor.AbsoluteSensorRange = AbsoluteSensorRangeValue.Unsigned_0To1;
        encoderConfig.MagnetSensor.MagnetOffset = angleOffset;
        CANCoder.getConfigurator().apply(encoderConfig);
        CANCoder.getAbsolutePosition().setUpdateFrequency(200, Constants.CAN_TIMEOUT_SECONDS);
        //CANCoder.configSensorInitializationStrategy(SensorInitializationStrategy.BootToAbsolutePosition);
    }

}


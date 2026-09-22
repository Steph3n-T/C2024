package org.team498.C2024.subsystems;

import static org.team498.C2024.Constants.DrivetrainConstants.MAX_ACCELERATION_METERS_PER_SECOND_SQUARED;

import java.util.Optional;
import java.util.function.Supplier;

import org.team498.C2024.subsystems.PhotonVision.TimedPose;

import com.ctre.phoenix6.Utils;
import com.ctre.phoenix6.mechanisms.swerve.SwerveDrivetrain;
import com.ctre.phoenix6.mechanisms.swerve.SwerveDrivetrainConstants;
import com.ctre.phoenix6.mechanisms.swerve.SwerveModuleConstants;
import com.ctre.phoenix6.mechanisms.swerve.SwerveRequest;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.estimator.PoseEstimator;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.interpolation.TimeInterpolatableBuffer;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.proto.Kinematics;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Notifier; 
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.util.HolonomicPathFollowerConfig;
import com.pathplanner.lib.util.PIDConstants;
import com.pathplanner.lib.util.ReplanningConfig;
/**
 * Class that extends the Phoenix SwerveDrivetrain class and implements
 * subsystem so it can be used in command-based projects easily.
 */
// camera enabled if robot is not moving
 public class CommandSwerveDrivetrain extends SwerveDrivetrain implements Subsystem {
    private static final double kSimLoopPeriod = 0.005; // 5 ms
    private Notifier m_simNotifier = null;
    private double m_lastSimTime;
    public TimeInterpolatableBuffer<Double> headingHistory = TimeInterpolatableBuffer.createDoubleBuffer(6);
    public LimelightLocalization limelightLocalization = new LimelightLocalization(m_odometry, m_pigeon2);

    private final PIDController rotationController = new PIDController(5.75 * 3.14/180.0, 0, 0);

    private final SlewRateLimiter xLimiter = new SlewRateLimiter(MAX_ACCELERATION_METERS_PER_SECOND_SQUARED);
    private final SlewRateLimiter yLimiter = new SlewRateLimiter(MAX_ACCELERATION_METERS_PER_SECOND_SQUARED);

    /* Blue alliance sees forward as 0 degrees (toward red alliance wall) */
    private final Rotation2d BlueAlliancePerspectiveRotation = Rotation2d.fromDegrees(0);
    /* Red alliance sees forward as 180 degrees (toward blue alliance wall) */
    private final Rotation2d RedAlliancePerspectiveRotation = Rotation2d.fromDegrees(180);
    /* Keep track if we've ever applied the operator perspective before or not */
    private Boolean hasAppliedOperatorPerspective = false;
    public boolean isMoving() {
        return (Math.abs(this.getState().speeds.vxMetersPerSecond) >= 0.1 || Math.abs(this.getState().speeds.vyMetersPerSecond) >= 0.1 || Math.abs(this.getState().speeds.omegaRadiansPerSecond) >= 0.5);
    }
    public CommandSwerveDrivetrain(SwerveDrivetrainConstants driveTrainConstants, double OdometryUpdateFrequency, SwerveModuleConstants... modules) {
        super(driveTrainConstants, OdometryUpdateFrequency, modules);
        if (Utils.isSimulation()) {
            startSimThread();
        }
    }
    public CommandSwerveDrivetrain(SwerveDrivetrainConstants driveTrainConstants, SwerveModuleConstants... modules) {
        super(driveTrainConstants, modules);

        AutoBuilder.configureHolonomic(
            () -> this.getState().Pose, // Robot pose supplier
            this::seedFieldRelative, // Method to reset odometry (will be called if your auto has a starting pose)
            () -> this.getState().speeds, // ChassisSpeeds supplier. MUST BE ROBOT RELATIVE
            this::driveRobotRelative, // Method that will drive the robot given ROBOT RELATIVE ChassisSpeeds
            new HolonomicPathFollowerConfig( // HolonomicPathFollowerConfig, this should likely live in your Constants class
                    new PIDConstants(5.0, 0.0, 0.0), // Translation PID constants
                    new PIDConstants(5.0, 0.0, 0.0), // Rotation PID constants
                    4.5, // Max module speed, in m/s
                    0.4, // Drive base radius in meters. Distance from robot center to furthest module.
                    new ReplanningConfig() // Default path replanning config. See the API for the options here
            ),
            () -> {
              // Boolean supplier that controls when the path will be mirrored for the red alliance
              // This will flip the path being followed to the red side of the field.
              // THE ORIGIN WILL REMAIN ON THE BLUE SIDE

              var alliance = DriverStation.getAlliance();
              if (alliance.isPresent()) {
                return alliance.get() == DriverStation.Alliance.Red;
              }
              return false;
            },
            this // Reference to this subsystem to set requirements
    );
        if (Utils.isSimulation()) {
            startSimThread();
        }
        rotationController.enableContinuousInput(0, 360);
    }

    public void setYaw(double angle) {
        this.seedFieldRelative(new Pose2d(getState().Pose.getTranslation(), Rotation2d.fromDegrees(angle)));
        this.m_pigeon2.setYaw(angle);
    }

    public double getHeading(double timestamp) {
        return headingHistory.getSample(timestamp).orElse((double) 0);
    }

    public void driveRobotRelative(ChassisSpeeds speeds) {
        this.setControl(new SwerveRequest.RobotCentric().withVelocityX(speeds.vxMetersPerSecond).withVelocityY(speeds.vyMetersPerSecond).withRotationalRate(speeds.omegaRadiansPerSecond));        
    }
    public void driveFieldRelative(ChassisSpeeds speeds) {
        this.setControl(new SwerveRequest.FieldCentric().withVelocityX(xLimiter.calculate(speeds.vxMetersPerSecond)).withVelocityY(yLimiter.calculate(speeds.vyMetersPerSecond)).withRotationalRate(speeds.omegaRadiansPerSecond));
    }

    public void driveFieldRelativeAngleLock(ChassisSpeeds speeds, double degrees) {
        double rotationRate = rotationController.calculate(CommandSwerveDrivetrain.getInstance().getState().Pose.getRotation().getDegrees(), degrees % 360);
        this.setControl(new SwerveRequest.FieldCentric().withVelocityX(xLimiter.calculate(speeds.vxMetersPerSecond)).withVelocityY(yLimiter.calculate(speeds.vyMetersPerSecond)).withRotationalRate(rotationRate));
    }

    public Command applyRequest(Supplier<SwerveRequest> requestSupplier) {
        return run(() -> this.setControl(requestSupplier.get()));
    }

    private void startSimThread() {
        m_lastSimTime = Utils.getCurrentTimeSeconds();

        /* Run simulation at a faster rate so PID gains behave more reasonably */
        m_simNotifier = new Notifier(() -> {
            final double currentTime = Utils.getCurrentTimeSeconds();
            double deltaTime = currentTime - m_lastSimTime;
            m_lastSimTime = currentTime;

            /* use the measured time delta, get battery voltage from WPILib */
            updateSimState(deltaTime, RobotController.getBatteryVoltage());
        });
        m_simNotifier.startPeriodic(kSimLoopPeriod);
    }

    Field2d field = new Field2d();
    @Override
    public void periodic() {
        /* Periodically try to apply the operator perspective */
        /* If we haven't applied the operator perspective before, then we should apply it regardless of DS state */
        /* This allows us to correct the perspective in case the robot code restarts mid-match */
        /* Otherwise, only check and apply the operator perspective if the DS is disabled */
        /* This ensures driving behavior doesn't change until an explicit disable event occurs during testing*/
        if (!hasAppliedOperatorPerspective || DriverStation.isDisabled()) {
            DriverStation.getAlliance().ifPresent((allianceColor) -> {
                this.setOperatorPerspectiveForward(
                        allianceColor == Alliance.Red ? RedAlliancePerspectiveRotation
                                : BlueAlliancePerspectiveRotation);
                hasAppliedOperatorPerspective = true;
            });
        }
        headingHistory.addSample(Timer.getFPGATimestamp(), this.getState().Pose.getRotation().getDegrees());
        
        
        Optional<PhotonVision.TimedPose> timedPose = PhotonVision.getInstance().getEstimatedPose();
        if (isMoving() == false && timedPose.isPresent() && DriverStation.isTeleopEnabled()) {
            TimedPose useGyro = new TimedPose(new Pose2d(timedPose.get().pose.getTranslation(), Rotation2d.fromDegrees(getHeading(timedPose.get().timeStamp))), timedPose.get().timeStamp);
            this.addVisionMeasurement(useGyro.pose, useGyro.timeStamp);
        }
        field.setRobotPose(this.getState().Pose);
        SmartDashboard.putData(field);
            
        limelightLocalization.update();
    }
    public class TimedPose{
        //4.1 , 6.5
        //0.16, 5.57
        //4.106
        Pose2d pose;
        double timeStamp;

        public TimedPose(Pose2d pose, double timeStamp){
            this.pose = pose;
            this.timeStamp = timeStamp;
        }

        // public EstimatedRobotPose toEstimatedRobotPose() {
        //     return new EstimatedRobotPose(null, timeStamp, null, null)
        // }
    }

    @Override
    public void seedFieldRelative() {
        try {
            m_stateLock.writeLock().lock();

            Pose2d newPose =
                    new Pose2d(getState().Pose.getTranslation(), m_operatorForwardDirection);
            m_odometry.resetPosition(
                    Rotation2d.fromDegrees(m_yawGetter.getValue()), m_modulePositions, newPose);

            DriverStation.getAlliance()
                    .ifPresent(
                            (alliance) -> {
                                hasAppliedOperatorPerspective = true;
                                setOperatorPerspectiveForward(
                                        alliance == Alliance.Red ? RedAlliancePerspectiveRotation
                                            : BlueAlliancePerspectiveRotation);
                            });

        } finally {
            m_stateLock.writeLock().unlock();
        }
    }

    private static CommandSwerveDrivetrain instance;

    public static CommandSwerveDrivetrain getInstance() {
        if (instance == null) instance = TunerConstants.DriveTrain; 
        return instance;
    }
}
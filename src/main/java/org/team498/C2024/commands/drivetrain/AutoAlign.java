package org.team498.C2024.commands.drivetrain;

import org.team498.C2024.Robot;
import org.team498.C2024.RobotPosition;
import org.team498.C2024.Controls;
import org.team498.C2024.subsystems.CommandSwerveDrivetrain;
import org.team498.C2024.subsystems.Hopper;
import org.team498.C2024.subsystems.Limelight;
import org.team498.C2024.subsystems.TunerConstants;
import org.team498.lib.LimelightHelpers;
import org.team498.lib.LimelightHelpers.LimelightResults;
import org.team498.lib.LimelightHelpers.LimelightTarget_Fiducial;
import org.team498.lib.drivers.Xbox;
import org.team498.lib.util.PoseUtil;

import com.ctre.phoenix6.mechanisms.swerve.SwerveRequest;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

public class AutoAlign extends Command{
    double tx = 0;
    boolean end = true;
    private final CommandSwerveDrivetrain drivetrain = CommandSwerveDrivetrain.getInstance();
    private Xbox controller = Robot.controls.driver;
    private double lastTX = 0;
    private double targetAngle = -1000;
    private boolean useLL = true;
    private Timer endTimer = new Timer();
    private final double endTime;
    public AutoAlign(double endTime) {
            this.endTime = endTime;
            addRequirements(drivetrain);
        }
        
        /// add math code here
    @Override
    public void execute() {
        useLL = true;
        int desiredTagID = Robot.alliance.get() == Alliance.Red ? 4 : 7;
        // while (tx == lastTX) {
        LimelightResults results = LimelightHelpers.getLatestResults("limelight");
        for (LimelightTarget_Fiducial tag : results.targetingResults.targets_Fiducials) {
            if (tag.fiducialID == desiredTagID) {
                tx = tag.tx;
                break;
            }
            // } else {
            //     tx = 0;
            // }
        }
        double limelightLatency = results.targetingResults.latency_pipeline + results.targetingResults.latency_capture + results.targetingResults.latency_jsonParse;
        limelightLatency = limelightLatency / 1000.0;  // Limelight publishes latency in ms, we need it in seconds.
        double limelightTimestamp = Timer.getFPGATimestamp() - limelightLatency;
        if (tx == lastTX) {
            useLL = false;
        }
        
        if (useLL) {
            targetAngle = drivetrain.getHeading(limelightTimestamp) - tx;
        } else {
            targetAngle = RobotPosition.calculateDegreesToSpeaker();
        }
        //end = true;
        
        // }
        if ((Math.abs(tx) <= 5.25 && useLL) || (Math.abs(drivetrain.getState().Pose.getRotation().getDegrees() - targetAngle) < 5 && !useLL)) {
            LimelightHelpers.setLEDMode_ForceOn("limelight");
            endTimer.start();
        } else {
            LimelightHelpers.setLEDMode_ForceOff("limelight");
            endTimer.stop();
            endTimer.reset();
        }
        // double speeds = 0;
        // speeds = tx * (Math.PI / 180);
        // drivetrain.driveFieldRelative(new ChassisSpeeds(
        //     controller.leftY() * controller.leftY() * controller.leftY() * TunerConstants.kSpeedAt12VoltsMps,
        //     controller.leftX() * controller.leftX() * controller.leftX() * TunerConstants.kSpeedAt12VoltsMps,
        //     -5 * speeds
        // ));
        // if (tx == lastTX) {
        //     drivetrain.driveFieldRelative(new ChassisSpeeds(
        //         0,
        //         0,
        //         0)
        //     );
        // } else {
        drivetrain.driveFieldRelativeAngleLock(new ChassisSpeeds(
            -controller.leftY() * controller.leftY() * controller.leftY() * TunerConstants.kSpeedAt12VoltsMps,
            -controller.leftX() * controller.leftX() * controller.leftX() * TunerConstants.kSpeedAt12VoltsMps,
            0), 
            targetAngle
        );
        // }
        
        lastTX = tx;

    }
    @Override
    public boolean isFinished() {
        return endTimer.get() >= endTime;
    }
    @Override
    public void end(boolean interrupted) {
        LimelightHelpers.setLEDMode_ForceOff("limelight");
        // if (Hopper.getInstance().getBackBeamBreak() && !CommandScheduler.getInstance().isScheduled(Controls.scoreCommand)) {
        //     CommandScheduler.getInstance().schedule(Controls.scoreCommand);
        // }
    }
}

package org.team498.C2024.commands.drivetrain;

import org.team498.C2024.Robot;
import org.team498.C2024.subsystems.CommandSwerveDrivetrain;
import org.team498.C2024.subsystems.TunerConstants;
import org.team498.lib.LimelightHelpers;
import org.team498.lib.LimelightHelpers.LimelightResults;
import org.team498.lib.LimelightHelpers.LimelightTarget_Fiducial;
import org.team498.lib.drivers.Xbox;
import org.team498.C2024.RobotPosition;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Command;

public class AutoLock extends Command {
    private double degrees;
    private final CommandSwerveDrivetrain drivetrain = CommandSwerveDrivetrain.getInstance();
    private Xbox controller = Robot.controls.driver;
    public AutoLock() {
        addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        
    }

    @Override
    public void execute() {
        this.degrees = RobotPosition.calculateDegreesToSpeaker();
        drivetrain.driveFieldRelativeAngleLock(new ChassisSpeeds(
            -controller.leftY() * controller.leftY() * controller.leftY() * TunerConstants.kSpeedAt12VoltsMps,
            -controller.leftX() * controller.leftX() * controller.leftX() * TunerConstants.kSpeedAt12VoltsMps,
            0), 
            this.degrees);
    }
    @Override
    public void end(boolean interrupted) {
        drivetrain.driveFieldRelative(new ChassisSpeeds());
    }

    @Override
    public boolean isFinished() {
        //currentTime > trajectory.getTotalTimeSeconds();
        return Math.abs(RobotPosition.calculateDegreesToSpeaker() - drivetrain.getState().Pose.getRotation().getDegrees()) <= 2;
    }
}

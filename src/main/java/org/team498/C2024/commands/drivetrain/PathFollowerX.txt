package org.team498.C2024.commands.drivetrain;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Command;

import org.team498.C2024.Robot;
import org.team498.C2024.RobotPosition;
import org.team498.C2024.StateController;
import org.team498.C2024.subsystems.Drivetrain;
import org.team498.lib.util.PoseUtil;
import org.team498.lib.wpilib.ChassisSpeeds;

import com.pathplanner.lib.path.PathPlannerTrajectory;
import com.pathplanner.lib.path.PathPlannerTrajectory.State;

public class PathFollowerX extends Command {
    private final Drivetrain drivetrain;
    private final PathPlannerTrajectory trajectory;
    private final Timer trajectoryTimer = new Timer();
    private double currentTime;
    private double dt;
    private StateController stateController = StateController.getInstance();

    public PathFollowerX(PathPlannerTrajectory trajectory) {
        this.drivetrain = Drivetrain.getInstance();
        this.trajectory = trajectory;
        addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        trajectoryTimer.reset();
        trajectoryTimer.start();
        currentTime = 0;
        dt = 0;
    }

    // boolean hasReset = false;

    @Override
    public void execute() {
        double slowDrive = stateController.getSlowDrive();
        Pose2d targetPose = stateController.getTargetDrive();
        boolean hasTargetDrive = stateController.getTargetDriveActive();
        dt = trajectoryTimer.get();
        trajectoryTimer.restart();
        currentTime += dt * slowDrive;
        State state = trajectory.sample(currentTime);
        // if (currentTime >= trajectory.getTotalTimeSeconds()) state = new State();
        if (Robot.alliance.get() == Alliance.Blue) {
            drivetrain.setPositionGoal(new Pose2d(state.positionMeters.getX(), state.positionMeters.getY(), state.targetHolonomicRotation));
            if (hasTargetDrive) drivetrain.setAngleGoal(RobotPosition.calculateDegreesToTarget(targetPose));
        } else {
            drivetrain.setPositionGoal(PoseUtil.flip(new Pose2d(state.positionMeters.getX(), state.positionMeters.getY(), state.targetHolonomicRotation)));
            if (hasTargetDrive) drivetrain.setAngleGoal(RobotPosition.calculateDegreesToTarget(PoseUtil.flip(targetPose)));
        }
        ChassisSpeeds speeds = drivetrain.calculatePositionSpeed();
        drivetrain.drive(speeds.vxMetersPerSecond, 0.0, speeds.omegaRadiansPerSecond, true);
    }

    @Override
    public boolean isFinished() {
        //currentTime > trajectory.getTotalTimeSeconds();
        if (Robot.alliance.get() == Alliance.Blue) {
            return RobotPosition.isNear(PoseUtil.toPose2d(trajectory.getEndState().positionMeters), 0.1);
        } else {
            return RobotPosition.isNear(PoseUtil.flip(PoseUtil.toPose2d(trajectory.getEndState().positionMeters)), 0.1);
        }
    }

    @Override
    public void end(boolean interrupted) {
        drivetrain.stop();
    }
}

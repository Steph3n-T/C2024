package org.team498.C2024.commands.drivetrain;

import java.util.List;
import java.util.function.BooleanSupplier;

import org.team498.C2024.Robot;
import org.team498.C2024.RobotPosition;
import org.team498.C2024.subsystems.Drivetrain;
import org.team498.lib.util.PoseUtil;
import org.team498.lib.util.Trajectories;
import org.team498.lib.wpilib.ChassisSpeeds;

import com.pathplanner.lib.path.PathPlannerTrajectory;
import com.pathplanner.lib.path.PathPlannerTrajectory.State;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;

public class OTFFollower extends Command{

    private final Timer trajectoryTimer = new Timer();
    private PathPlannerTrajectory trajectory;
    private BooleanSupplier slowDriveSupplier;

    private List <Pose2d> waypoints;
    private double currentTime;
    private double dt;
    public OTFFollower(List <Pose2d> poses, BooleanSupplier slowDriveSupplier){
        waypoints = poses;
        this.slowDriveSupplier = slowDriveSupplier;
        addRequirements(Drivetrain.getInstance());
        
    }
    @Override
    public void initialize() {
        List<Pose2d> poses = List.of(Drivetrain.getInstance().getPose(), waypoints.get(0), waypoints.get(1));
        trajectory = Trajectories.getOTFTrajectory(poses);
        
        trajectoryTimer.reset();
        trajectoryTimer.start();
        currentTime = 0;
        dt = 0;

        // // Display the trajectory on the driver station dashboard
        // List<Pose2d> poses = new LinkedList<>();
        // List<State> trajectoryStates = trajectory.getStates();
        // for (int i = 0; i < trajectoryStates.size(); i += trajectoryStates.size() / 60) {
        //     if (Robot.alliance.get() == Alliance.Blue) {
        //         poses.add(PoseUtil.toPose2d(trajectoryStates.get(i).positionMeters));
        //     } else {
        //         poses.add(PoseUtil.flip(PoseUtil.toPose2d(trajectoryStates.get(i).positionMeters)));
        //     }
        // }



    }
    
     @Override
    public void execute() {
        dt = trajectoryTimer.get();
        trajectoryTimer.restart();
        currentTime += slowDriveSupplier.getAsBoolean() ? dt / 2 : dt;
        State state = trajectory.sample(currentTime);

        if (Robot.alliance.get() == Alliance.Blue) {
            Drivetrain.getInstance().setPositionGoal(new Pose2d(state.positionMeters.getX(), state.positionMeters.getY(), state.targetHolonomicRotation));
        } else {
            Drivetrain.getInstance().setPositionGoal(PoseUtil.flip(new Pose2d(state.positionMeters.getX(), state.positionMeters.getY(), state.targetHolonomicRotation)));
        }

        ChassisSpeeds speeds = Drivetrain.getInstance().calculatePositionSpeed();
        Drivetrain.getInstance().drive(speeds.vxMetersPerSecond, speeds.vyMetersPerSecond, speeds.omegaRadiansPerSecond, true);
        
        SmartDashboard.putNumber("trajectory time", currentTime);
    }

    @Override
    public boolean isFinished() {
        if (Robot.controls.driver.isLeftStickActive() || Robot.controls.driver.isRightStickActive() || Robot.controls.driver.isPOVActive())
            return true;
        // return trajectoryTimer.get() > trajectory.getTotalTimeSeconds();
        if (Robot.alliance.get() == Alliance.Blue) {
            return RobotPosition.isNear(PoseUtil.toPose2d(trajectory.getEndState().positionMeters), 0.1);
        } else {
            return RobotPosition.isNear(PoseUtil.flip(PoseUtil.toPose2d(trajectory.getEndState().positionMeters)), 0.1);
        }
    }

    @Override
    public void end(boolean interrupted) {
        trajectoryTimer.stop();
        Drivetrain.getInstance().stop();
    }
}

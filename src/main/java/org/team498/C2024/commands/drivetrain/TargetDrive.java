package org.team498.C2024.commands.drivetrain;

import org.team498.C2024.StateController;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.InstantCommand;

public class TargetDrive extends InstantCommand{
    
    private final Pose2d targetPose;

    /**
     * 
     * @param targetPose pose to point at. use null for Optional.empty()
     */
    public TargetDrive(Pose2d targetPose){
        this.targetPose = targetPose;
    }

    @Override
    public void execute() {
        StateController.getInstance().setTargetDrive(targetPose);
    }
}

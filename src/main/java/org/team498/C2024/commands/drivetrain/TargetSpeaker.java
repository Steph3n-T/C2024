package org.team498.C2024.commands.drivetrain;

import org.team498.C2024.FieldPositions;
import org.team498.C2024.StateController;

import edu.wpi.first.wpilibj2.command.InstantCommand;

public class TargetSpeaker extends InstantCommand{

    /**
     * 
     * @param targetPose pose to point at. use null for Optional.empty()
     */
    public TargetSpeaker(){
    }

    @Override
    public void execute() {
        StateController.getInstance().setTargetDrive(FieldPositions.getSpeaker());
    }
}

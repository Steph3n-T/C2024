package org.team498.C2024.commands.robot;

import org.team498.C2024.State;
import org.team498.C2024.commands.hopper.SetHopperNextState;
import org.team498.C2024.commands.intake.SetIntakeNextState;
import org.team498.C2024.commands.intake.SetIntakeRollersNextState;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class SetIntakeIdle extends SequentialCommandGroup {
    public SetIntakeIdle(){
        super(
            //Sets states to IDLE
            new SetState(State.IDLE),
            new ParallelCommandGroup(
                new SetHopperNextState(),
                new SetIntakeNextState(),
                new SetIntakeRollersNextState()
            )
        );
    }
}

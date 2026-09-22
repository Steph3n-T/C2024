package org.team498.C2024.commands.robot.scoring;

import org.team498.C2024.State;
import org.team498.C2024.commands.hopper.SetHopperNextState;
import org.team498.C2024.commands.kicker.SetKickerNextState;
import org.team498.C2024.commands.kicker.SetKickerState;
import org.team498.C2024.commands.robot.SetState;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class CancelAmp extends SequentialCommandGroup{
    public CancelAmp(){
        super(
                //Set Kicker to Reverse
                new SetState(State.CANCEL_AMP),
                new SetKickerState(State.Kicker.REVERSE),

                //Moves Note back to hopper
                // new MoveHopper(16),

                //Set kicker to Idle
                new SetState(State.IDLE),
                new SetHopperNextState(),
                new SetKickerNextState()
        );
    }
}
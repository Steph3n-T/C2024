package org.team498.C2024.commands.robot.scoring;

import org.team498.C2024.State;
import org.team498.C2024.commands.hopper.SetHopperNextState;
import org.team498.C2024.commands.kicker.SetKickerNextState;
import org.team498.C2024.commands.kicker.SetKickerState;
import org.team498.C2024.commands.robot.SetState;
import org.team498.C2024.commands.shooter.SetShooterNextState;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class CancelSpeaker extends SequentialCommandGroup {
    public CancelSpeaker(){
        super(
            // Sets Kicker to Forward and Shooter to Idle
            new SetState(State.CANCEL_SPEAKER),
            new ParallelCommandGroup(
                new SetShooterNextState(),
                new SetKickerState(State.Kicker.REVERSE)

                //Moves Note back into Hopper
                // new MoveHopper(0)
            ),

            //Sets Kicker and Shooter to Idle
            new SetState(State.IDLE),
            new ParallelCommandGroup(
                new SetShooterNextState(),
                new SetHopperNextState(),
                new SetKickerNextState()
            )
        );
    }
}
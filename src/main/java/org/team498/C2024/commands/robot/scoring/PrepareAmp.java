package org.team498.C2024.commands.robot.scoring;

import org.team498.C2024.State;
import org.team498.C2024.commands.hopper.MoveHopper;
import org.team498.C2024.commands.kicker.SetKickerState;
import org.team498.C2024.commands.robot.SetState;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class PrepareAmp extends SequentialCommandGroup {
    public PrepareAmp(){
        super(
            //Starts in AMP state

            // Sets Kicker to forward
            new SetKickerState(State.Kicker.FORWARD),
            //new SetHopperState(State.Hopper.FORWARD),

            // Moves Note into to Kicker
            new MoveHopper(-12),
            new SetKickerState(State.Kicker.IDLE),

            //new ConditionalCommand(new SetKickerState(State.Kicker.IDLE), new SetKickerNextState(), Kicker.getInstance()::isKickerBeamBreakEnabled),
            //new ConditionalCommand(new SetHopperState(State.Hopper.IDLE), new SetHopperNextState(), Kicker.getInstance()::isKickerBeamBreakEnabled),

            // Sets Kicker to Idle
            // new SetState(State.IDLE),
            // new SetKickerNextState(),

            // Sets robot state back to AMP
            new SetState(State.AMP)
        );
    }
}
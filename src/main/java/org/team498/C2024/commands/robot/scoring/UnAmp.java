package org.team498.C2024.commands.robot.scoring;

import org.team498.C2024.State;
import org.team498.C2024.commands.hopper.MoveHopper;
import org.team498.C2024.commands.kicker.SetKickerState;
import org.team498.C2024.commands.robot.SetState;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class UnAmp extends SequentialCommandGroup {
    public UnAmp(){
        super(
            //Starts in AMP state

            // Sets Kicker to REVERSE
            new SetKickerState(State.Kicker.REVERSE),
            //new SetHopperState(State.Hopper.FORWARD),

            // Moves Note from hopper to kicker
            new MoveHopper(6),
            new SetKickerState(State.Kicker.IDLE),

            //new ConditionalCommand(new SetKickerState(State.Kicker.IDLE), new SetKickerNextState(), Kicker.getInstance()::isKickerBeamBreakEnabled),
            //new ConditionalCommand(new SetHopperState(State.Hopper.IDLE), new SetHopperNextState(), Kicker.getInstance()::isKickerBeamBreakEnabled),

            // Sets Kicker to Idle
            // new SetState(State.IDLE),
            // new SetKickerNextState(),

            // Sets robot state back to AMP
            new SetState(State.IDLE)
        );
    }
}
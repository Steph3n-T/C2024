package org.team498.C2024.commands.robot.loading;

import org.team498.C2024.State;
import org.team498.C2024.commands.hopper.MoveHopper;
import org.team498.C2024.commands.hopper.SetHopperNextState;
import org.team498.C2024.commands.kicker.SetKickerNextState;
import org.team498.C2024.commands.robot.SetState;
import org.team498.C2024.commands.shooter.SetShooterNextState;
import org.team498.C2024.subsystems.Hopper;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;

public class LoadSource extends SequentialCommandGroup{
    public LoadSource(){
        super(
            new SetState(State.SOURCE),
            // Set States to intake
            new ParallelCommandGroup(
                new SetShooterNextState(),
                new SetHopperNextState(),
                new SetKickerNextState()
            ),
            //Move Note to Beam Break
            new WaitUntilCommand(Hopper.getInstance()::getBackBeamBreak),

             // Set Subsystems to IDLE
            new SetState(State.IDLE),
            new ParallelCommandGroup(
                new SetShooterNextState(),
                new SetHopperNextState(),
                new SetKickerNextState()
            ),
            // Move Note to other side of Beam Break
            new MoveHopper(-1)
        );
    }
}

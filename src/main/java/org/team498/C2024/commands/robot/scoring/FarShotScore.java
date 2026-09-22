package org.team498.C2024.commands.robot.scoring;

import org.team498.C2024.State;
import org.team498.C2024.StateController;
import org.team498.C2024.StateController.ScoringOption;
import org.team498.C2024.commands.robot.SetState;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class FarShotScore extends SequentialCommandGroup{
    public FarShotScore(){
        super(
            new SetState(State.FARSHOT),
            new InstantCommand(()-> StateController.getInstance().setNextScoringOption(ScoringOption.FARSHOT)),
            //new InstantCommand(StateController.getInstance().setNextScoringOption(ScoringOption.SUBWOOFER))),
            new ParallelDeadlineGroup(
               // new WaitCommand(0.5),
                new PrepareToScore()
            ),
            new HalfScore()
            // new WaitCommand(1),
            // new ReturnToIdle()
        );
    }
}

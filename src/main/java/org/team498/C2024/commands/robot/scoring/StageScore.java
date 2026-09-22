package org.team498.C2024.commands.robot.scoring;

import org.team498.C2024.State;
import org.team498.C2024.StateController;
import org.team498.C2024.StateController.ScoringOption;
import org.team498.C2024.commands.robot.SetState;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class StageScore extends SequentialCommandGroup{
    public StageScore(){
        super(
            new SetState(State.OUTER_STAGE),
            new InstantCommand(()-> StateController.getInstance().setNextScoringOption(ScoringOption.OUTER_STAGE)),
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

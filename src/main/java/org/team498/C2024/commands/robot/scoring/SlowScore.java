package org.team498.C2024.commands.robot.scoring;

import org.team498.C2024.Constants.DrivetrainConstants;
import org.team498.C2024.StateController;
import org.team498.C2024.StateController.ScoringOption;
import org.team498.C2024.commands.drivetrain.SlowDrive;
import org.team498.C2024.commands.drivetrain.TargetDrive;
import org.team498.C2024.commands.drivetrain.TargetSpeaker;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class SlowScore extends SequentialCommandGroup{
    public SlowScore(double wait){
        super(
            new InstantCommand(()-> StateController.getInstance().setNextScoringOption(ScoringOption.CRESCENDO)),
            new ParallelDeadlineGroup(
                new WaitCommand(wait),
                new TargetSpeaker(),
                new SlowDrive(DrivetrainConstants.AUTO_SPEED_SCALAR),
                new PrepareToScore()
                //new WaitCommand(wait),
            ),
            new HalfScore(),
            new TargetDrive(null),
            new SlowDrive(DrivetrainConstants.AUTO_SLOW_SPEED_SCALAR)
        );
    }
}

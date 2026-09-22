package org.team498.C2024.commands.robot.scoring;

import org.team498.C2024.Constants.DrivetrainConstants;
import org.team498.C2024.commands.drivetrain.SlowDrive;
import org.team498.C2024.commands.drivetrain.TargetDrive;
import org.team498.C2024.commands.drivetrain.TargetSpeaker;

import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class FullScoreSlow extends SequentialCommandGroup{
    public FullScoreSlow(double wait){
        super(
            new ParallelDeadlineGroup(
                new WaitCommand(wait),
                new TargetSpeaker(),
                new SlowDrive(DrivetrainConstants.TEN_PERCENT),
                new PrepareToScore()
                //new WaitCommand(wait),
            ),
            new HalfScore(),
            new TargetDrive(null),
            new SlowDrive(DrivetrainConstants.FULL_SPEED_SCALAR)
        );
    }
}

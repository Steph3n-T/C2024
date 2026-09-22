package org.team498.C2024.commands.robot.scoring;

import org.team498.C2024.State;
import org.team498.C2024.commands.kicker.SetKickerState;
import org.team498.C2024.commands.robot.SetScoringState;
import org.team498.C2024.commands.shooter.SetShooterNextState;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class PrepareSpeaker extends SequentialCommandGroup {
    public PrepareSpeaker(){
        super(
            // Starts in PODIUM, SUBWOOFER, or CRESCENDO
            // Sets Shooter to Speed and sets kicker to REVERSE
            new ParallelCommandGroup(
                new SetShooterNextState()
                // Moves Note into Shooter
                // new MoveHopper(-0)
            )
            
            //Sets Kicker to IDLE

            //Sets state Back to Speaker
            // new SetScoringState()
        );
    }
}
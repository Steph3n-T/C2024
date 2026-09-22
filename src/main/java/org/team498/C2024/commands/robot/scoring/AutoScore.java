package org.team498.C2024.commands.robot.scoring;

import org.team498.C2024.State;
import org.team498.C2024.commands.hopper.SetHopperNextState;
import org.team498.C2024.commands.kicker.SetKickerNextState;
import org.team498.C2024.commands.robot.SetState;
import org.team498.C2024.commands.shooter.SetShooterNextState;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class AutoScore extends SequentialCommandGroup {
    public AutoScore(){
        super(

            //Starts in CRESCENDO, SUBWOOFER, PODIUM, or AMP
            new SetState(State.CRESCENDO),

            //Sets Hopper, Shooter and Kicker to Next state
            new SetKickerNextState(),
            new SetHopperNextState(),
            new SetShooterNextState(),

            //Waits for Note to shoot
            new WaitCommand(0.5),

            // Sets Shooter, Hopper and Kicker to IDLE
            new SetState(State.IDLE),
            new SetShooterNextState(),
            new SetHopperNextState(),
            new SetKickerNextState()
        );
    }
    
}

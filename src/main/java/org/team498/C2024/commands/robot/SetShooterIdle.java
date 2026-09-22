package org.team498.C2024.commands.robot;

import org.team498.C2024.State;
import org.team498.C2024.commands.hopper.SetHopperNextState;
import org.team498.C2024.commands.kicker.SetKickerNextState;
import org.team498.C2024.commands.shooter.SetShooterNextState;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class SetShooterIdle extends SequentialCommandGroup {
    public SetShooterIdle(){
        super(
            // Sets Shooter, Hopper and Kicker to IDLE
            new SetState(State.IDLE),
            new SetShooterNextState(),
            new SetHopperNextState(),
            new SetKickerNextState()
        );
    }
    
}

package org.team498.C2024.commands.robot;

import org.team498.C2024.State;
import org.team498.C2024.Constants.DrivetrainConstants;
import org.team498.C2024.commands.drivetrain.SlowDrive;
import org.team498.C2024.commands.drivetrain.TargetDrive;
import org.team498.C2024.commands.hopper.SetHopperNextState;
import org.team498.C2024.commands.kicker.SetKickerNextState;
import org.team498.C2024.commands.shooter.SetShooterNextState;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class IdleIntakeOn extends SequentialCommandGroup {
    public IdleIntakeOn(){
        super(
            //Sets states to IDLE
            new SetState(State.IDLE),
            new ParallelCommandGroup(
                new SetShooterNextState(),
                new SetKickerNextState(),
                new SetHopperNextState(),
                new TargetDrive(null),
                new SlowDrive(DrivetrainConstants.FULL_SPEED_SCALAR)
            )
        );
    }
}

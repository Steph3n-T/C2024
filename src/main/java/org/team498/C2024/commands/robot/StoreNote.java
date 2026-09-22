package org.team498.C2024.commands.robot;

import org.team498.C2024.State;
import org.team498.C2024.commands.hopper.MoveHopper;
import org.team498.C2024.commands.hopper.SetHopperNextState;
import org.team498.C2024.commands.hopper.SetHopperState;
import org.team498.C2024.subsystems.Hopper;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;

public class StoreNote extends SequentialCommandGroup{
    public StoreNote(){
        super(

            // Sets Hopper to INTAKE
            new SetState(State.INTAKE),
            new SetHopperNextState(),

            // //Intakes Note until it Hits the BeamBreak
            new WaitUntilCommand(() -> Hopper.getInstance().getBackBeamBreak()),
            //new MoveHopper(4),
            //Sets Hopper to IDLE
            new SetHopperState(State.Hopper.IDLE)
        );
    }
}

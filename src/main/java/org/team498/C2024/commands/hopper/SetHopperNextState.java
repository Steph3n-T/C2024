package org.team498.C2024.commands.hopper;

import org.team498.C2024.StateController;
import org.team498.C2024.subsystems.Hopper;

import edu.wpi.first.wpilibj2.command.InstantCommand;

public class SetHopperNextState extends InstantCommand {
    private final Hopper hopper = Hopper.getInstance();

    public SetHopperNextState() {
        //addRequirements(hopper);
    }

    @Override
    public void initialize() {
        hopper.setState(StateController.getInstance().getState().hopper);
    }
}


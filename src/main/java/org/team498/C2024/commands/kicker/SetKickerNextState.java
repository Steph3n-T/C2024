package org.team498.C2024.commands.kicker;

import org.team498.C2024.StateController;
import org.team498.C2024.subsystems.Kicker;

import edu.wpi.first.wpilibj2.command.InstantCommand;

public class SetKickerNextState extends InstantCommand{
    private final Kicker kicker = Kicker.getInstance();

    public SetKickerNextState(){
        addRequirements(kicker);
    }

    @Override
    public void initialize() {
        kicker.setState(StateController.getInstance().getState().kicker);
    }

}

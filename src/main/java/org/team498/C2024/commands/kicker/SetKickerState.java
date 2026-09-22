package org.team498.C2024.commands.kicker;

import org.team498.C2024.State;
import org.team498.C2024.subsystems.Kicker;

import edu.wpi.first.wpilibj2.command.InstantCommand;

public class SetKickerState extends InstantCommand{
    private final Kicker kicker = Kicker.getInstance();
    private final State.Kicker state;

    public SetKickerState(State.Kicker state){
        this.state = state;
        addRequirements(kicker);
    }

    @Override
    public void initialize() {
        kicker.setState(state);
    }

}

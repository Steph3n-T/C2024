package org.team498.C2024.commands.hopper;
import org.team498.C2024.State;
import org.team498.C2024.subsystems.Hopper;
import edu.wpi.first.wpilibj2.command.InstantCommand;

public class SetHopperState extends InstantCommand {
    private final Hopper hopper = Hopper.getInstance();
    private final State.Hopper state;

    public SetHopperState(State.Hopper state) {
        this.state = state;
        addRequirements(hopper);
    }

    @Override
    public void initialize() {
        hopper.setState(state);
    }
}


package org.team498.C2024.commands.robot;

import org.team498.C2024.State;
import org.team498.C2024.StateController;

import edu.wpi.first.wpilibj2.command.InstantCommand;

public class SetScoringState extends InstantCommand {
    private final StateController stateController = StateController.getInstance();

    private State state;

    public SetScoringState() {
    }

    @Override
    public void initialize() {
        state = stateController.getNextScoringState();
        stateController.setState(state);
    }
}


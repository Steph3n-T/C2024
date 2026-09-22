package org.team498.C2024.commands.intake;

import org.team498.C2024.State;
import org.team498.C2024.StateController;
import org.team498.C2024.subsystems.IntakeRollers;

import edu.wpi.first.wpilibj2.command.InstantCommand;

public class SetIntakeRollerState extends InstantCommand {
    private final IntakeRollers intakeRollers = IntakeRollers.getInstance();
    private final State.IntakeRollers state;

    public SetIntakeRollerState(State.IntakeRollers state) {
        this.state = state;
        // addRequirements(intakeRollers);
    }

    @Override
    public void initialize() {
        intakeRollers.setState(state);
    }
}


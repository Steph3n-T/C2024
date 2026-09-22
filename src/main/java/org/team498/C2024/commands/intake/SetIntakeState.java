package org.team498.C2024.commands.intake;

import org.team498.C2024.State;
import org.team498.C2024.StateController;
import org.team498.C2024.subsystems.Intake;
import org.team498.C2024.subsystems.IntakeRollers;

import edu.wpi.first.wpilibj2.command.InstantCommand;

public class SetIntakeState extends InstantCommand {
    private final Intake intake = Intake.getInstance();
    private final State.Intake state;

    public SetIntakeState(State.Intake state) {
        this.state = state;
        addRequirements(intake);
    }

    @Override
    public void initialize() {
        intake.setState(state);
    }
}


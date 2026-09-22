package org.team498.C2024.commands.intake;

import org.team498.C2024.StateController;
import org.team498.C2024.subsystems.Intake;

import edu.wpi.first.wpilibj2.command.Command;

public class SetIntakeNextState extends Command {
    private final Intake intake = Intake.getInstance();

    public SetIntakeNextState() {
        addRequirements(intake);
    }

    @Override
    public void initialize() {
        intake.setState(StateController.getInstance().getState().intake);
    }

    @Override
    public boolean isFinished() {
        return true;//intake.atSetpoint();
    }
}


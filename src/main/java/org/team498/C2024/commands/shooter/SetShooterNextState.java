package org.team498.C2024.commands.shooter;

import org.team498.C2024.StateController;
import org.team498.C2024.subsystems.Shooter;

import edu.wpi.first.wpilibj2.command.Command;

public class SetShooterNextState extends Command {
    private final Shooter shooter = Shooter.getInstance();

    public SetShooterNextState() {
        addRequirements(shooter);
    }

    @Override
    public void initialize() {
        shooter.setState(StateController.getInstance().getState().shooter);
    }

    @Override
    public boolean isFinished() {
        return true;//shooter.atSetpoint();
    }
}


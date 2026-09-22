package org.team498.C2024.commands.shooter;

import org.team498.C2024.State;
import org.team498.C2024.subsystems.Shooter;

import edu.wpi.first.wpilibj2.command.Command;

public class SetShooterState extends Command {
    private final Shooter shooter = Shooter.getInstance();
    private final State.Shooter state;

    public SetShooterState(State.Shooter state) {
        this.state = state;
        addRequirements(shooter);
    }

    @Override
    public void initialize() {
        shooter.setState(state);
    }

    @Override
    public boolean isFinished() {
        return shooter.atSetpoint();
    }
}


package org.team498.C2024.commands.shooter;

import java.util.function.DoubleSupplier;

import org.team498.C2024.subsystems.Shooter;

import edu.wpi.first.wpilibj2.command.Command;

public class SetShooterManual extends Command{
    private final Shooter shooter = Shooter.getInstance();
    private final DoubleSupplier angleSpeed;
    private final DoubleSupplier flywheelSpeed;
    private final boolean isManual;

    public SetShooterManual(boolean isManual, DoubleSupplier angleSpeed, DoubleSupplier flywheelSpeed){
        this.isManual = isManual;
        this.angleSpeed = angleSpeed;
        this.flywheelSpeed = flywheelSpeed;
        addRequirements(Shooter.getInstance());
    }

    @Override
    public void execute() {
        shooter.setAngleManual(isManual, angleSpeed.getAsDouble(), flywheelSpeed.getAsDouble());
    }

    @Override
    public void end(boolean interrupted) {
        shooter.setAngleManual(false, 0, 0);
    }
}

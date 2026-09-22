package org.team498.C2024.commands.intake;

import java.util.function.DoubleSupplier;

import org.team498.C2024.subsystems.Intake;
import edu.wpi.first.wpilibj2.command.Command;

public class SetIntakeManual extends Command{
    private final Intake intake = Intake.getInstance();
    private final DoubleSupplier speed;
    private final boolean isManual;

    public SetIntakeManual(boolean isManual, DoubleSupplier speed){
        this.isManual = isManual;
        this.speed = speed;
    }

    @Override
    public void execute() {
        intake.setPositionManual(isManual, speed.getAsDouble());
    }

    @Override
    public void end(boolean interrupted) {
        intake.setPositionManual(false, 0);
    }
}

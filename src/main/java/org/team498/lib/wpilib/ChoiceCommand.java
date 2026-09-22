package org.team498.lib.wpilib;

import edu.wpi.first.wpilibj2.command.Command;

import java.util.function.Supplier;

public class ChoiceCommand extends Command { //TODO in the offseason, figure out how to add requirements
    private final Supplier<Command> commandSupplier;
    private Command commandToRun;

    public ChoiceCommand(Supplier<Command> commandSupplier) {
        this.commandSupplier = commandSupplier;
    }

    @Override
    public void initialize() {
        commandToRun = commandSupplier.get();
        commandToRun.initialize();
    }

    @Override
    public void execute() {
        commandToRun.execute();
    }

    @Override
    public void end(boolean interrupted) {
        commandToRun.end(interrupted);
    }

    @Override
    public boolean isFinished() {
        return commandToRun.isFinished();
    }
}

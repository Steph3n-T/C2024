package org.team498.C2024.commands.drivetrain;
import org.team498.C2024.StateController;

import edu.wpi.first.wpilibj2.command.InstantCommand;

public class SlowDrive extends InstantCommand{
    
    private final double slowDrive;

    public SlowDrive(double slowDrive){
        this.slowDrive = slowDrive;
    }

    @Override
    public void execute() {
        StateController.getInstance().setSlowDrive(slowDrive);;
    }
}

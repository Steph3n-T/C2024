package org.team498.C2024.commands.hopper;

import org.team498.C2024.subsystems.Hopper;

import edu.wpi.first.wpilibj2.command.Command;

public class MoveHopper extends Command{
    private final Hopper hopper = Hopper.getInstance();
    private final double position;

    public MoveHopper(double position){
        this.position = position;
    }

    @Override
    public void initialize() {
        hopper.setPosition(position);
    }

    @Override
    public boolean isFinished() {
        return hopper.atSetpoint();
    }
}
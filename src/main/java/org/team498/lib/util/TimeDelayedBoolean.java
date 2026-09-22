package org.team498.lib.util;

import static edu.wpi.first.wpilibj2.command.Commands.waitSeconds;

import edu.wpi.first.wpilibj.Timer;

public class TimeDelayedBoolean {
    private boolean value;
    private Timer timer;

    public TimeDelayedBoolean(boolean initialValue) {
        value = initialValue;
        timer = new Timer();
    }

    public void setValue(boolean value, double delaySeconds) {
        timer.reset();
        timer.start();
        
        this.value = value;
    }
    public boolean getValue() {
        return value;
    }
}

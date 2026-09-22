package org.team498.lib.drivers;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

// Credit to team 254 for inspiring this class
public class LazyTalonFX extends TalonFX {
    private double currentSetpoint = Double.NaN;

    private NeutralModeValue currNeutralMode;

    public LazyTalonFX(int deviceNumber) {
        super(deviceNumber);
    }

    public LazyTalonFX(int deviceNumber, String canBus){
        super(deviceNumber, canBus);
    }

    @Override
    public void set(double setpoint){
        if(setpoint != currentSetpoint){
            currentSetpoint = setpoint;
            super.set(setpoint);
        }
    }
    @Override
    public void setNeutralMode(NeutralModeValue neutralMode){
        if(neutralMode != currNeutralMode){
            currNeutralMode = neutralMode;
            super.setNeutralMode(neutralMode);
        }
    }

    public double getSelectedSensorPosition(){
        return super.getPosition().getValueAsDouble();
    }
}
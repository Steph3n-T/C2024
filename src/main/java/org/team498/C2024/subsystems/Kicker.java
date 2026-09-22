package org.team498.C2024.subsystems;

import org.team498.C2024.Ports;
import org.team498.C2024.State;
import org.team498.C2024.Ports.KickerPorts;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Kicker extends SubsystemBase {

    private final TalonFX motor;

    private double setpoint;
    private State.Kicker currentState;
    private final DigitalInput kickerBeamBreak;
    private boolean kickerBeamBreakEnabled;
    
    // Constructor: Configure Motor Controller settings and  
    // Instantiate all objects (assign values to every variable and object)
    public Kicker() {
        motor = new TalonFX(Ports.KickerPorts.MOTOR);
        kickerBeamBreak = new DigitalInput(KickerPorts.KICKER_BEAM_BREAK);

        // Instantiate variables to intitial values
        setpoint = 0;
        currentState = State.Kicker.IDLE;

        // reset motor defaults to ensure all settings are clear
    }

    public void configMotors() {
        motor.getConfigurator().apply(new TalonFXConfiguration());
    }

    @Override
    public void periodic() {
        //updates setpoint
        double speed = setpoint;
        set(speed);
    }

    public boolean getKickerBeamBreak(){
        return !kickerBeamBreak.get();
    }

    /**
     * returns true if the beambreak is enabled
     */
    public boolean isKickerBeamBreakEnabled(){
        return kickerBeamBreakEnabled;
    }

    /**
     * enables or disables beambreak
     * @param isEnabled true if enabling
     */
    public void setKickerBeamBreakEnabled(boolean isEnabled){
        kickerBeamBreakEnabled = isEnabled;
    }

    //sets motor speeds
    public void set(double speed) {
        motor.set(-speed);
    }

    /**
     * sets Kicker state
     * updates setpoint
     */
    public void setState(State.Kicker state) {
        currentState = state; // update state
        setpoint = state.speed; // update setpoint
    }

    // Getter method to retrieve current State
    public State.Kicker getState() {
        return currentState;
    }

    private static Kicker instance;

    public static Kicker getInstance() {
        if (instance == null) instance = new Kicker(); // Make sure there is an instance (this will only run once)
        return instance;
    }
}

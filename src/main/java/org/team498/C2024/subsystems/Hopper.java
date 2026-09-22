package org.team498.C2024.subsystems;

import org.team498.C2024.Ports;
import org.team498.C2024.State;
import org.team498.C2024.Constants.HopperConstants;
import org.team498.C2024.Ports.HopperPorts;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Hopper extends SubsystemBase {

    private final TalonFX topMotor;
    private final TalonFX bottomMotor;
    private final PIDController pidController;
    private final DigitalInput backBeamBreak;
    private final DigitalInput frontBeamBreak;
    private boolean backBeamBreakEnabled;
    private boolean frontBeamBreakEnabled;

    private boolean pidEnabled;

    private double setpoint;
    private State.Hopper currentState;
    
    // Constructor: Configure Motor Controller settings and  
    // Instantiate all objects (assign values to every variable and object)
    public Hopper() {
        topMotor = new TalonFX(Ports.HopperPorts.TOP_MOTOR);
        bottomMotor = new TalonFX(Ports.HopperPorts.BOTTOM_MOTOR);
        backBeamBreak = new DigitalInput(HopperPorts.BACK_BEAM_BREAk);
        frontBeamBreak = new DigitalInput(HopperPorts.FRONT_BEAM_BREAk);
        pidController = new PIDController(HopperConstants.P, HopperConstants.I, HopperConstants.D);
        pidController.setTolerance(0.1);


        // Instantiate variables to intitial values
        setpoint = 0;
        currentState = State.Hopper.IDLE;
        pidEnabled = false;

        backBeamBreakEnabled = true;
        frontBeamBreakEnabled = true;
    }

    public void configMotors() {
        topMotor.getConfigurator().apply(new TalonFXConfiguration());
        bottomMotor.getConfigurator().apply(new TalonFXConfiguration());
    }

    @Override
    public void periodic() {
        if(pidController.atSetpoint()) pidEnabled = false;
        if(pidEnabled){
            set(pidController.calculate(bottomMotor.getPosition().getValueAsDouble()));
        }
        else{
            double speed = setpoint;
            set(speed);
        }
        SmartDashboard.putBoolean("Beambreak",getBackBeamBreak());
    }

    public void set(double speed) {
        topMotor.set(speed * 1.25);
        bottomMotor.set(speed);
    }

     public void setState(State.Hopper state) {
        // update state
        currentState = state;
        // update setpoint
        setpoint = state.speed;
    }

    // Getter method to retrieve current State
    public State.Hopper getState() {
        return currentState;
    }

    public boolean isPidEnabled(){
        return pidEnabled;
    }

    /**
     * returns true if pidController is at setpoint
     */
    public boolean atSetpoint(){
        return pidController.atSetpoint();
    }

    /**
     * resets encoder, enables PID and sets PID setpoint
     */
    public void setPosition(double position){
        // bottomMotor.setPosition(0);
        pidEnabled = true;
        pidController.setSetpoint(bottomMotor.getPosition().getValueAsDouble() + position);
    }

    /**
     * return true if beambreak sensor is active
     */
    public boolean getBackBeamBreak(){
        return !backBeamBreak.get();
    }

    /**
     * returns true if the beambreak is enabled
     */
    public boolean isBackBeamBreakEnabled(){
        return backBeamBreakEnabled;
    }

    /**
     * enables or disables beambreak
     * @param isEnabled true if enabling
     */
    public void setBackBeamBreakEnabled(boolean isEnabled){
        backBeamBreakEnabled = isEnabled;
    }

      public boolean getFrontBeamBreak(){
        return !frontBeamBreak.get();
    }

    /**
     * returns true if the beambreak is enabled
     */
    public boolean isFrontBeamBreakEnabled(){
        return frontBeamBreakEnabled;
    }

    /**
     * enables or disables beambreak
     * @param isEnabled true if enabling
     */
    public void setFrontBeamBreakEnabled(boolean isEnabled){
        frontBeamBreakEnabled = isEnabled;
    }
    private static Hopper instance;

    public static Hopper getInstance() {
        if (instance == null) instance = new Hopper(); 
        return instance;
    }
}

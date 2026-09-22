package org.team498.lib.drivers;

import com.revrobotics.CANSparkMax;
import com.revrobotics.REVLibError;

// // Credit to team 254 for inspiring this class
 public class LazySparkMax extends CANSparkMax {
     private double currentSetpoint = Double.NaN;
     private IdleMode currentIdleMode = null;

     public LazySparkMax(int deviceNumber, MotorType motorType) {
         super(deviceNumber, motorType);
     }

     @Override
     public void set(double setpoint) {
         if (setpoint != currentSetpoint) {
             this.currentSetpoint = setpoint;
             super.set(setpoint);
         }
     }

     @Override
     public REVLibError setIdleMode(IdleMode idleMode) {
         if (currentIdleMode != idleMode) {
             currentIdleMode = idleMode;
             return super.setIdleMode(idleMode);
         }
         return REVLibError.kOk;
     }
 }

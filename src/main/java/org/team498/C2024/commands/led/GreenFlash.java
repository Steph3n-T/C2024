// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.team498.C2024.commands.led;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

import javax.xml.stream.events.EndDocument;

import org.team498.C2024.subsystems.LED;
import org.team498.C2024.subsystems.LED.LEDState;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class GreenFlash extends SequentialCommandGroup {
  private LED led;
  /** Creates a new GreenFlash. */
  
  public GreenFlash() {}
    public Command getCommand(){
    return new SequentialCommandGroup(
    new InstantCommand(() -> led.setState(LEDState.INTAKE_SUCCESS)),
    new WaitCommand(0.1),
    new InstantCommand(() -> led.setState(LEDState.OFF)),
    new WaitCommand(0.1),
    new InstantCommand(() -> led.setState(LEDState.INTAKE_SUCCESS)),
    new WaitCommand(0.1),
    new InstantCommand(() -> led.setState(LEDState.OFF)),
    new WaitCommand(0.1),
    new InstantCommand(() -> led.setState(LEDState.INTAKE_SUCCESS)),
    new WaitCommand(0.1),
    new InstantCommand(() -> led.setState(LEDState.IDLE)));
    }
    
  
}

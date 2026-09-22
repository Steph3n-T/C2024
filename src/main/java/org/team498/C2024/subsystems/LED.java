package org.team498.C2024.subsystems;

//import edu.wpi.first.wpilibj2.command.InstantCommand;
//import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
//import edu.wpi.first.wpilibj2.command.WaitCommand;

import org.team498.lib.drivers.Blinkin;
import org.team498.lib.drivers.Blinkin.BlinkinColor;


public class LED extends SubsystemBase {
  //private BlinkinColor ledColor;
  private LEDState state = LEDState.IDLE;
  private Blinkin blinkin = Blinkin.getInstance();


  public enum LEDState {
    IDLE(BlinkinColor.SOLID_YELLOW),
    //Idle means the robot without a note | RED
    INTAKE_SUCCESS(BlinkinColor.SOLID_DARK_GREEN),
    //INTAKE_SUCCESS is when the first beam breaks are activated | Strobe Green
    SECURE(BlinkinColor.SOLID_BLUE_GREEN), 
    //Secure is when a note has hit the second beam breaks and is completely in the robot | Blue
    AMP(BlinkinColor.SOLID_YELLOW),
    //Amp is when the note is in the amper part of the hopper | Yellow
    SUBWOOFER(BlinkinColor.SOLID_HOT_PINK),
    //Communicates that the robot cannot go under the stage because the shooter is too high | Pink
    SHOOTER_READY(BlinkinColor.SOLID_GREEN),

    NO_SHOOT(BlinkinColor.SOLID_RED),
    //Communicates the Shooter being aligned
    AUTO(BlinkinColor.OCEAN_PALETTE),
    //BATTERY_LOW(BlinkinColor.STROBE_RED),
    //If the battery is low at the start of the match it will flash red TODO: Check if it works correctly
    //BATTERY_GOOD(BlinkinColor.FOREST_PALETTE),
    //If the battery is charged at the start of the match it will have a green pattern TODO: Check if it works correctly
    OFF(BlinkinColor.SOLID_BLACK),

    DISABLED(BlinkinColor.BREATH_RED);

    public BlinkinColor ledColor;

    private LEDState(BlinkinColor ledColor){
    this.ledColor = ledColor;
    }
  }

public void setState(LEDState state){
  this.state = state;
  blinkin.setColor(state.ledColor);
}

private static LED instance;

public static LED getInstance() {
    if (instance == null) instance = new LED(); // Make sure there is an instance (this will only run once)
    return instance;
}

}


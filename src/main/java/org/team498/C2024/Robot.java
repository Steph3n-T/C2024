package org.team498.C2024;


import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

import org.team498.C2024.commands.drivetrain.AutoAlign;
import org.team498.C2024.commands.drivetrain.AutoLock;
import org.team498.C2024.commands.intake.SetIntakeRollerState;
import org.team498.C2024.commands.intake.SetIntakeRollersNextState;
import org.team498.C2024.commands.intake.SetIntakeState;
import org.team498.C2024.commands.robot.ReturnToIdle;
import org.team498.C2024.commands.robot.SetIntakeIdle;
import org.team498.C2024.commands.robot.SetState;
import org.team498.C2024.commands.robot.loading.LoadGround;
import org.team498.C2024.commands.robot.loading.LoadGroundAuto;
import org.team498.C2024.commands.robot.scoring.AmpZoneScore;
import org.team498.C2024.commands.robot.scoring.AutoScore;
import org.team498.C2024.commands.robot.scoring.CrescendoScore;
import org.team498.C2024.commands.robot.scoring.SanghiScore;
import org.team498.C2024.commands.robot.scoring.FarShotScore;
import org.team498.C2024.commands.robot.scoring.FrontPodiumScore;
import org.team498.C2024.commands.robot.scoring.HalfScore;
import org.team498.C2024.commands.robot.scoring.PodiumScore;
import org.team498.C2024.commands.robot.scoring.PrepareToScore;
import org.team498.C2024.commands.robot.scoring.StageScore;
import org.team498.C2024.commands.robot.scoring.SubwooferScore;
import org.team498.C2024.commands.shooter.SetShooterNextState;
import org.team498.C2024.subsystems.CommandSwerveDrivetrain;
import org.team498.C2024.subsystems.Hopper;
import org.team498.C2024.subsystems.Intake;
import org.team498.C2024.subsystems.IntakeRollers;
import org.team498.C2024.subsystems.Kicker;
import org.team498.C2024.subsystems.LED;
import org.team498.C2024.subsystems.PhotonVision;
import org.team498.C2024.subsystems.Shooter;
import org.team498.C2024.subsystems.LED.LEDState;
import org.team498.lib.LimelightHelpers;
import org.team498.lib.auto.Auto;
import org.team498.lib.drivers.Blinkin;
import org.team498.lib.drivers.Gyro;
import java.util.List;
import java.util.Optional;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;


public class Robot extends TimedRobot{
    public static final double DEFAULT_PERIOD = 0.02;
    public final Timer setupTimer = new Timer();
    public double setupTime  = 0;
    // public static int coordinateFlip = 1;
    // public static int rotationOffset = 0;

    public static Optional<Alliance> alliance = Optional.empty();
    public static final Controls controls = new Controls();

    // private PowerDistribution pdh;
    private LED led = new LED();

    private final CommandSwerveDrivetrain drivetrain = CommandSwerveDrivetrain.getInstance();
    private final Gyro gyro = Gyro.getInstance();
    private final Blinkin blinkin = Blinkin.getInstance();
    //private final RobotState robotState = RobotState.getInstance();

    private SendableChooser<Command> autoChooser;
        // private Auto defaultAuto = new Auto() {
        
    // };
    // private Auto autoToRun = defaultAuto;

    // private boolean matchStarted = false;
    // private final List<Auto> autoOptions = List.of(
    //     //new FourNote(),
    //     //new FourNoteFull(),
    //     //new SixNoteAmp(),
    //     //new TestAuto(),
    //     //new FourPort(),
    //     //new FourNoteWing(),
    //     //new OneTaxi(),
    //     //new LongTaxi(),
    //     //new Spit(),
    //     //new Troll(),
    //     //new OuterWingTest(),rad
    //     //new TestPathing(),
    //     //new WingFacingSpeaker(),
    //     //new OuterWing(),
    //     //new AmpSideTaxi()
    //     //new PracticeAuto()
    //                                               );


    @Override
    public void robotInit() {

        LimelightHelpers.setLEDMode_ForceOff("limelight");
        NamedCommands.registerCommand("prepareToScore", new PrepareToScore());
        NamedCommands.registerCommand("halfScore", new HalfScore());
        NamedCommands.registerCommand("loadGround", new LoadGround());
        NamedCommands.registerCommand("setIntakeIdle", new SetIntakeRollerState(State.IntakeRollers.IDLE));
        NamedCommands.registerCommand("setIntakeOut", new SetIntakeState(State.Intake.INTAKE));
        NamedCommands.registerCommand("subwooferScore", new SubwooferScore(1));
        NamedCommands.registerCommand("setStatePodium", new SetState(State.PODIUM));
        NamedCommands.registerCommand("setStateFrontPodium", new SetState(State.FRONT_PODIUM));
        NamedCommands.registerCommand("podiumScore", new PodiumScore());
        NamedCommands.registerCommand("frontPodiumScore", new FrontPodiumScore());
        NamedCommands.registerCommand("autoAlign", new AutoAlign(0.1));
        NamedCommands.registerCommand("setShooterNextState", new SetShooterNextState());
        NamedCommands.registerCommand("ampZoneScore", new AmpZoneScore());
        NamedCommands.registerCommand("setStateOuterStage", new SetState(State.OUTER_STAGE));
        NamedCommands.registerCommand("stageScore", new StageScore());
        NamedCommands.registerCommand("setStateCrescendo", new SetState(State.CRESCENDO));
        NamedCommands.registerCommand("sanghiScore", new SanghiScore());
        NamedCommands.registerCommand("crescenlowScore", new FarShotScore());
        NamedCommands.registerCommand("crescendoScore", new CrescendoScore());
        NamedCommands.registerCommand("loadGroundAuto", new LoadGroundAuto());
        NamedCommands.registerCommand("setStateSanghi", new SetState(State.SANGHI));
        NamedCommands.registerCommand("setStateCrescenlow", new SetState(State.FARSHOT));
        NamedCommands.registerCommand("setStateAmpZone", new SetState(State.AMP_SPEAKER));

        // m_robotContainer = new RobotContainer();
        //new PowerDistribution(1, PowerDistribution.ModuleType.kRev).close(); // Enables power distribution logging
        CommandSwerveDrivetrain.getInstance().tareEverything();
        autoChooser = AutoBuilder.buildAutoChooser();
       // FieldPositions.displayAll();
        // autoChooser.setDefaultOption(defaultAuto.getName(), defaultAuto);
        // autoOptions.forEach(auto -> autoChooser.addOption(auto.getName(), auto));
        controls.configureDefaultCommands();
        controls.configureDriverCommands();
        controls.configureOperatorCommands();

        // PathLib.four_port_1.getClass();
        // PathLib.SL1Note1.getClass();
        // CommandSwerveDrivetrain.enableBrakeMode(true);
        // Register Subsystems
        CommandSwerveDrivetrain.getInstance();
        Shooter.getInstance();
        Hopper.getInstance();
        Kicker.getInstance();
        Intake.getInstance();
        IntakeRollers.getInstance();
        PhotonVision.getInstance();

        Shooter.getInstance().configMotors();
        Hopper.getInstance().configMotors();
        Kicker.getInstance().configMotors();
        Intake.getInstance().configMotors();
        IntakeRollers.getInstance().configMotors();

        // Limelight.getInstance();
        
    }

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
        
        SmartDashboard.putData(autoChooser);
        SmartDashboard.putBoolean("Note Ready To AMP", Kicker.getInstance().getKickerBeamBreak());
        SmartDashboard.putNumber("Distance To Speaker", RobotPosition.distanceToSpeaker());
        // SmartDashboard.putNumber("bl cancoder", kDefaultPeriod)
        // SmartDashboard.putBoolean("is Scoring", StateController.getInstance().isScoring());
        if (alliance.isEmpty()) {
            alliance = DriverStation.getAlliance();
            // This reverses the coordinates/direction of the drive commands on the red alliance
            // coordinateFlip = alliance.get() == Alliance.Blue
            //                  ? 1
            //                  : -1;
            // // Add 180 degrees to all teleop rotation setpoints while on the red alliance
            // rotationOffset = alliance.get() == Alliance.Blue
            //                  ? 0
            //                  : 180;
    
        }
        
        // blinkin.setColor(BlinkinColor.BREATH_RED);
        SmartDashboard.putBoolean("Shooter Aligned", Shooter.getInstance().atSetpoint() && Shooter.getInstance().shooterState() && !Hopper.getInstance().isPidEnabled());
        // if (RobotState.isEnabled()) {
        //     if(Shooter.getInstance().atSetpoint() && Shooter.getInstance().shooterState() && !Hopper.getInstance().isPidEnabled()) {
        //         blinkin.setColor(BlinkinColor.SOLID_DARK_GREEN);
        //         SmartDashboard.putBoolean("Shooter Aligned", true);
        //     }
        //     else {
        //         blinkin.setColor(BlinkinColor.SOLID_BLUE);
        //         SmartDashboard.putBoolean("Shooter Aligned", false);
        //     }
        // }
        // else { 
        //     if (RobotPosition.isNear(autoToRun.getInitialPose(), 1))
        //         blinkin.setColor(BlinkinColor.SOLID_DARK_GREEN);
        //     else blinkin.setColor(BlinkinColor.SOLID_HOT_PINK);

        // }

        
        }

    @Override
    public void disabledPeriodic() {
        alliance = DriverStation.getAlliance();
        // This reverses the coordinates/direction of the drive commands on the red alliance
        // coordinateFlip = alliance.get() == Alliance.Blue
        //                  ? 1
        //                  : -1;
        // // Add 180 degrees to all teleop rotation setpoints while on the red alliance
        // rotationOffset = alliance.get() == Alliance.Blue
        //                  ? 0
        //                  : 180;
        // if (setupTime < 10) {
        //     setupTime = setupTimer.get();
        //     Commandswer.updateIntegratedEncoders();
        // } else {
        //     setupTimer.stop();
        //     setupTime = setupTimer.get() + 10;
        // }

        // autoToRun = autoChooser.getSelected();
        
    }
        // if (autoToRun != null) {
        //     // robotState.setState(autoToRun.getInitialState());
        // }


            //Displays Battery Voltage via the LEDs on the Robot NEW

        // if(pdh.getVoltage() < 12.3 ){
        //     led.setState(LEDState.BATTERY_LOW);
        // } else {
        //     led.setState(LEDState.BATTERY_GOOD);
        // }
        //Could be used to check vision, subsystem positions, or even the robot position if there is one hardset with the april tags
  


    @Override
    public void teleopInit() {
        Shooter.getInstance().setFeed(0);
        Shooter.getInstance().setAngle(0);
        Hopper.getInstance().set(0);
        Intake.getInstance().set(0);
        IntakeRollers.getInstance().set(0);
        Kicker.getInstance().set(0);
        CommandScheduler.getInstance().schedule(new ReturnToIdle());

        

        // matchStarted = true;
        // drivetrain.enableBrakeMode(true);
    }

    @Override
    public void teleopPeriodic() {
        if (Hopper.getInstance().getBackBeamBreak()){
            controls.driver.rumble(0.1);
            controls.operator.rumble(0.05);
        }
        else {
            controls.driver.rumble(0);
            controls.operator.rumble(0);
        }
        
    if(RobotState.isEnabled()){
        if(Shooter.getInstance().atSetpoint() && Shooter.getInstance().getState() != State.Shooter.IDLE){
            led.setState(LEDState.SHOOTER_READY);
        }
        else if(Shooter.getInstance().isSubwoofer()){
            led.setState(LEDState.SUBWOOFER);
        } //When the Shooter is Aligned
            //When The intake beam break is activated
        else if(Hopper.getInstance().getFrontBeamBreak()){
            led.setState(LEDState.INTAKE_SUCCESS);
        // or run the one below for flashing
        // if(Hopper.getInstance().getFrontBeamBreak()){
        //     new GreenFlash();
        // }
        } //When the middle beam break is activated
        else if(Hopper.getInstance().getBackBeamBreak()){
            led.setState(LEDState.SECURE);
        } //When the Amping Beam Break is activated
        /*else if(Kicker.getInstance().getKickerBeamBreak()){
            led.setState(LEDState.AMP);
        } */ //When the Shooter is in Subwoofer mode
        
        //When no other cases are true
        else {
            led.setState(LEDState.IDLE);
        }
    }

        /* OLD Blinkin Code
        if (RobotState.isEnabled()) {
            if(StateController.getInstance().getState() == State.IDLE){
                blinkin.setColor(BlinkinColor.SOLID_DARK_RED);
            }
            else if(Shooter.getInstance().atSetpoint()) {
                blinkin.setColor(BlinkinColor.SOLID_GREEN);
            }
            else if (Shooter.getInstance().isSubwoofer()){
                blinkin.setColor(BlinkinColor.SOLID_HOT_PINK);
            }
            else if(Hopper.getInstance().getBackBeamBreak()) {
                blinkin.setColor(BlinkinColor.SOLID_BLUE);
            }
            else if(Hopper.getInstance().getFrontBeamBreak()) {
                blinkin.setColor(BlinkinColor.SOLID_DARK_GREEN);
            }
            // else if(Kicker.getInstance().getKickerBeamBreak()) {
            //     blinkin.setColor(BlinkinColor.SOLID_BLUE);
            }
            else {
                blinkin.setColor(BlinkinColor.SOLID_DARK_RED);
            }*/

    }


    @Override
    public void teleopExit() {
        controls.driver.rumble(0);
    }

    @Override
    public void disabledInit() {
        setupTimer.restart();
        // drivetrain.enableBrakeMode(false);
    }
    @Override
    public void autonomousInit() {
        // new FullScore().schedule();
        // drivetrain.enableBrakeMode(true);
        // matchStarted = true;
        Shooter.getInstance().setFeed(0);
        Shooter.getInstance().setAngle(0);
        Hopper.getInstance().set(0);
        Intake.getInstance().set(0);
        IntakeRollers.getInstance().set(0);
        Kicker.getInstance().set(0);

        // if (autoToRun == null)
            // autoToRun = defaultAuto;
        if (autoChooser.getSelected() != null)
            autoChooser.getSelected().schedule();
        //autoToRun = new HighHighCone();

        // if (alliance.get() == Alliance.Blue) {
        //     CommandSwerveDrivetrain.getInstance().(autoToRun.getInitialPose().getRotation().getDegrees());
        //     Drivetrain.getInstance().setPose(autoToRun.getInitialPose());
        // } else {
        //     Drivetrain.getInstance().setYaw(PoseUtil.flipAngleDegrees(autoToRun.getInitialPose().getRotation().getDegrees()));
        //     Drivetrain.getInstance().setPose(PoseUtil.flip(autoToRun.getInitialPose()));
        // }
        //SmartDashboard.putData((Sendable) autoToRun.getInitialPose());

        // autoToRun.getCommand().schedule();
        //new LongTaxi().getCommand().schedule();

        // CommandScheduler.getInstance().run();

        // if (alliance.get() == Alliance.Blue) {
        //     Drivetrain.getInstance().setYaw(autoToRun.getInitialPose().getRotation().getDegrees());
        //     Drivetrain.getInstance().setPose(autoToRun.getInitialPose());
        // } else {
        //     Drivetrain.getInstance().setYaw(PoseUtil.flip(autoToRun.getInitialPose()).getRotation().getDegrees());
        //     Drivetrain.getInstance().setPose(PoseUtil.flip(autoToRun.getInitialPose()));
        // }
        led.setState(LEDState.AUTO);
        //Sets the LEDs to a pattern for auto, this could be edited to include code for if vision is aligned for auto diagnosis
    }

    @Override
  public void autonomousPeriodic() {}


    @Override
    public void testInit() {
        CommandScheduler.getInstance().cancelAll();
    }

    public static void main(String... args) {
        RobotBase.startRobot(Robot::new);
        }
    }
package org.team498.C2024;

import org.team498.C2024.Constants.DrivetrainConstants;
import org.team498.C2024.Constants.OIConstants;
import org.team498.C2024.StateController.ScoringOption;
import org.team498.C2024.commands.drivetrain.AngleLock;
import org.team498.C2024.commands.drivetrain.AutoAlign;
import org.team498.C2024.commands.drivetrain.AutoLock;
import org.team498.C2024.commands.drivetrain.CrescendoAlign;
import org.team498.C2024.commands.drivetrain.SlowDrive;
import org.team498.C2024.commands.drivetrain.TargetDrive;
import org.team498.C2024.commands.hopper.SetHopperState;
import org.team498.C2024.commands.robot.IdleIntakeOn;
import org.team498.C2024.commands.robot.ReturnToIdle;
import org.team498.C2024.commands.robot.SetIntakeIdle;
import org.team498.C2024.commands.robot.SetScoringState;
import org.team498.C2024.commands.robot.StoreNote;
import org.team498.C2024.commands.robot.loading.LoadGround;
import org.team498.C2024.commands.robot.loading.Outtake;
import org.team498.C2024.commands.robot.scoring.PrepareAmp;
import org.team498.C2024.commands.robot.scoring.PrepareToScore;
import org.team498.C2024.commands.robot.scoring.Score;
import org.team498.C2024.commands.robot.scoring.UnAmp;
import org.team498.C2024.commands.shooter.SetShooterManual;
import org.team498.C2024.commands.shooter.SetShooterState;
import org.team498.C2024.subsystems.CommandSwerveDrivetrain;
import org.team498.C2024.subsystems.Hopper;
import org.team498.C2024.subsystems.LED;
import org.team498.C2024.subsystems.Shooter;
import org.team498.C2024.subsystems.TunerConstants;
import org.team498.C2024.subsystems.LED.LEDState;
import org.team498.lib.drivers.Blinkin;
import org.team498.lib.drivers.Xbox;
import org.team498.lib.drivers.Blinkin.BlinkinColor;
import org.team498.lib.util.PoseUtil;

import com.ctre.phoenix6.mechanisms.swerve.SwerveRequest;
import com.ctre.phoenix6.mechanisms.swerve.SwerveModule.DriveRequestType;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.RepeatCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;

import static edu.wpi.first.wpilibj2.command.Commands.*;

import java.util.function.Supplier;

public class Controls {
    private double MaxSpeed = TunerConstants.kSpeedAt12VoltsMps; // Initial max is true top speed
    private final double TurtleSpeed = 0.1; // Reduction in speed from Max Speed, 0.1 = 10%
    private final double MaxAngularRate = Math.PI * 3.5; // .75 rotation per second max angular velocity.  Adjust for max turning rate speed.
    private final double TurtleAngularRate = Math.PI * 0.5; // .75 rotation per second max angular velocity.  Adjust for max turning rate speed.
    private double AngularRate = MaxAngularRate; // This will be updated when turtle and reset to MaxAngularRate

     SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
      .withDriveRequestType(DriveRequestType.OpenLoopVoltage)
      .withDeadband(MaxSpeed * 0.1) // Deadband is handled on input
      .withRotationalDeadband(AngularRate * 0.1);

    public final Xbox driver = new Xbox(OIConstants.DRIVER_CONTROLLER_ID);
    public final Xbox operator = new Xbox(OIConstants.OPERATOR_CONTROLLER_ID);
    //CommandXboxPS5Controller drv = new CommandXboxPS5Controller(0); // driver xbox controller
    public static final Command scoreCommand = new Score();

    public Controls() {
        driver.setDeadzone(0.15);
        driver.setTriggerThreshold(0.2);
        operator.setDeadzone(0.2);
        operator.setTriggerThreshold(0.2);
    }
    private Supplier<SwerveRequest> controlStyle;
    private void newControlStyle () {
        controlStyle = () -> drive.withVelocityX(-driver.leftY() * driver.leftY() * driver.leftY() * MaxSpeed) // Drive forward -Y
            .withVelocityY(-driver.leftX() * driver.leftX() * driver.leftX() * MaxSpeed) // Drive left with negative X (left)
            .withRotationalRate(driver.rightX() * AngularRate); // Drive counterclockwise with negative X (left)
    }


     
    public void configureDefaultCommands() {
        newControlStyle();
         //CommandSwerveDrivetrain.getInstance().setDefaultCommand(new HybridDrive(driver::leftYSquared, driver::leftXSquared, driver::rightX, driver::rawPOVAngle));
         CommandSwerveDrivetrain.getInstance().setDefaultCommand(repeatingSequence( // Drivetrain will execute this command periodically
         runOnce(()-> CommandSwerveDrivetrain.getInstance().driveFieldRelative(new ChassisSpeeds(-driver.leftY() * driver.leftY() * driver.leftY() * MaxSpeed, -driver.leftX() * driver.leftX() * driver.leftX() * MaxSpeed, driver.rightX() * AngularRate)), CommandSwerveDrivetrain.getInstance())));
  }

    public void configureDriverCommands() {
        // driver.rightBumper().onTrue(new SlowDrive(DrivetrainConstants.SLOW_SPEED_SCALAR))
        //     .onFalse(new SlowDrive(DrivetrainConstants.FULL_SPEED_SCALAR));
        driver.rightBumper().onTrue(runOnce(() ->CommandSwerveDrivetrain.getInstance().setYaw(Robot.alliance.get() == Alliance.Red?180:0)));
        driver.leftBumper().whileTrue(new ConditionalCommand(
                new ConditionalCommand(
                    new AngleLock(-90),
                    new AngleLock(-90),
                    ()-> Robot.alliance.get() == Alliance.Red),
                // CRESCENDO MODE SHOOT COMMAND
                new AutoAlign(1),//.andThen(new SetScoringState().andThen(new WaitUntilCommand(() -> Shooter.getInstance().atSetpoint() && Hopper.getInstance().getBackBeamBreak())).andThen(new SetScoringState().andThen(runOnce(()-> CommandScheduler.getInstance().schedule(scoreCommand))))),
                // END CRESCENDO MODE SHOOT
                ()-> StateController.getInstance().getNextScoringState() == State.AMP))
            .onFalse(CommandSwerveDrivetrain.getInstance().getDefaultCommand());
        // driver.B().onTrue(new AutoAlign().andThen(
        //         new ConditionalCommand(
        //             new SetScoringState().andThen(runOnce(()-> CommandScheduler.getInstance().schedule(scoreCommand))),
        //             new WaitCommand(0.0),
        //             () -> Hopper.getInstance().getBackBeamBreak()
        //         )
        //     ));
        //driver.A().onTrue(runOnce(() -> Drivetrain.getInstance().setYaw(0 + Robot.rotationOffset)));
        //driver.B().onTrue(runOnce(() -> Drivetrain.getInstance().setPose(new Pose2d(15.18, 1.32, Rotation2d.fromDegrees(0 + Robot.rotationOffset)))));
        // driver.Y().onTrue(runOnce(() -> Drivetrain.getInstance().setPose(new Pose2d(15.07, 5.55, Rotation2d.fromDegrees(0 + Robot.rotationOffset)))));
        // driver.A().onTrue(runOnce(() ->CommandSwerveDrivetrain.getInstance().setYaw(Robot.alliance.get() == Alliance.Red?180:0)));
        driver.leftTrigger().onTrue(new LoadGround())
            .onFalse(new SetIntakeIdle());
        // driver.leftBumper().onTrue(new PrepareToScore())z
        //     .onFalse(runOnce(()-> {
        //             if (!StateController.getInstance().isScoring()) {
        //                 CommandScheduler.getInstance().schedule(new ConditionalCommand(new CancelAmp(), new CancelSpeaker(), ()-> StateController.getInstance().getState() == State.AMP));
        //             }
        //         }   
        //     ));
        //driver.rightTrigger().onTrue(runOnce(()-> CommandScheduler.getInstance().schedule(scoreCommand)));//.onFalse(runOnce(()-> scoreCommand.cancel()));
        driver.rightTrigger().onTrue(new SetScoringState().andThen(runOnce(()-> CommandScheduler.getInstance().schedule(scoreCommand))));
        driver.B().whileTrue(new ConditionalCommand(
                new ConditionalCommand(
                    new AngleLock(-90),
                    new AngleLock(-90),
                    ()-> Robot.alliance.get() == Alliance.Red),
                // CRESCENDO MODE SHOOT COMMAND
                new CrescendoAlign(0.07),//andThen(new SetScoringState().andThen(new WaitUntilCommand(() -> Shooter.getInstance().atSetpoint() && Hopper.getInstance().getBackBeamBreak())).andThen(new SetScoringState().andThen(runOnce(()-> CommandScheduler.getInstance().schedule(scoreCommand))))),
                // END CRESCENDO MODE SHOOT
                ()-> StateController.getInstance().getNextScoringState() == State.AMP))
            .onFalse(CommandSwerveDrivetrain.getInstance().getDefaultCommand());
        //driver.POV90().onTrue(new AutoAlign());
        driver.X().onTrue(runOnce(() -> StateController.getInstance().setNextScoringOption(ScoringOption.OUTREACH)));
        driver.X().toggleOnTrue(new PrepareToScore());
        driver.start().onTrue(new ReturnToIdle());
    }

    public void configureOperatorCommands() {
        operator.start().toggleOnTrue(new SetShooterManual(true, operator::leftY, ()-> 0));
        // operator.back().toggleOnTrue(new SetIntakeManual(true, operator::rightY));
        operator.rightTrigger().onTrue(new SetHopperState(State.Hopper.REVERSE))
            .onFalse(new ReturnToIdle());
        operator.leftTrigger().onTrue(new Outtake())
            .onFalse(new ReturnToIdle());
        operator.leftBumper().onTrue(runOnce(() -> StateController.getInstance().setNextScoringOption(ScoringOption.AMP)));
        operator.leftBumper().toggleOnTrue(new UnAmp());
        operator.rightBumper().onTrue(new IdleIntakeOn());
        // operator.back().toggleOnTrue(new SetShooterManual(true, operator::leftY, ()-> 0.5));
        // operator.A().toggleOnTrue(new SetShooterManual(true, operator::leftY, ()-> 1.0));
        // operator.B().toggleOnTrue(new SetShooterManual(true, operator::leftY, ()-> 0.0));
        // operator.Y().toggleOnTrue(new SetState(State.AMP).andThen(new SetKickerNextState()));
        // operator.X().toggleOnTrue(new SetState(State.SUBWOOFER).andThen(new SetKickerNextState()));
        // operator.rightBumper().toggleOnTrue(new SetState(State.IDLE).andThen(new SetKickerNextState()));
        // operator.back().onTrue(new SetShooterState(State.Shooter.CLIMB_UP)).onFalse(new SetShooterState(State.Shooter.CLIMB_DOWN));



        operator.X().onTrue(runOnce(() -> StateController.getInstance().setNextScoringOption(ScoringOption.FRONT_PODIUM)));
        operator.X().toggleOnTrue(new PrepareToScore());

        //operator.A().onTrue(runOnce(() -> StateController.getInstance().setNextScoringOption(ScoringOption.SUBWOOFER)));
        operator.A().onTrue(runOnce(() -> StateController.getInstance().setNextScoringOption(ScoringOption.SUBWOOFER)));
        operator.A().toggleOnTrue(new PrepareToScore());

        // operator.B().onTrue(runOnce(() -> StateController.getInstance().setNextScoringOption(ScoringOption.PODIUM)));
        // operator.B().toggleOnTrue(new PrepareToScore());
        operator.B().onTrue(runOnce(() -> StateController.getInstance().setNextScoringOption(ScoringOption.PODIUM)));
        operator.B().toggleOnTrue(new PrepareToScore());

        operator.Y().onTrue(runOnce(() -> StateController.getInstance().setNextScoringOption(ScoringOption.AMP)));
        operator.Y().toggleOnTrue(new PrepareAmp());

        operator.POV0().onTrue(runOnce(() -> StateController.getInstance().setNextScoringOption(ScoringOption.AMP_SPEAKER)));
        operator.POV0().toggleOnTrue(new PrepareToScore());

        operator.POVMinus90().onTrue(runOnce(() -> StateController.getInstance().setNextScoringOption(ScoringOption.FEED)));
        operator.POVMinus90().toggleOnTrue(new PrepareToScore());

        operator.POV90().onTrue(runOnce(() -> StateController.getInstance().setNextScoringOption(ScoringOption.CRESCENDO)));
        operator.POV90().toggleOnTrue(new PrepareToScore());

        // operator.leftBumper().onTrue(runOnce(() -> StateController.getInstance().setNextLoadingOption(LoadingOption.GROUND)));
        // operator.leftTrigger().onTrue(new LoadSource())
        //     .onFalse(new ReturnToIdle());
        // operator.rightBumper().onTrue(runOnce(() -> StateController.getInstance().setNextLoadingOption(LoadingOption.SOURCE)));
        // operator.rightTrigger().onTrue(new CollectSource())
        //     .onFalse(new ReturnToIdle());
    }
}
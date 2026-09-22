package org.team498.C2024.commands.drivetrain;

import org.team498.C2024.Robot;
import org.team498.C2024.RobotPosition;
import org.team498.C2024.StateController;
import org.team498.C2024.subsystems.Drivetrain;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;

import java.util.function.DoubleSupplier;

import static org.team498.C2024.Constants.DrivetrainConstants.AngleConstants.MAX_ANGULAR_SPEED_DEGREES_PER_SECOND;
import static org.team498.C2024.Constants.DrivetrainConstants.MAX_VELOCITY_METERS_PER_SECOND;

public class HybridDrive extends Command {
    private final Drivetrain drivetrain = Drivetrain.getInstance();

    private final DoubleSupplier xTranslationSupplier;
    private final DoubleSupplier yTranslationSupplier;
    private final DoubleSupplier rotationSupplier;
    // private final BooleanSupplier slowDriveSupplier;
    // private final BooleanSupplier targetDriveSupplier;
    private final DoubleSupplier povAngle;
    private double desiredAngle;
    private double rotationVelocity;
    private boolean isPOVControlled;
    private boolean isDriverControlled;
    private int i = 0;

    public HybridDrive(DoubleSupplier xTranslationSupplier,
                        DoubleSupplier yTranslationSupplier,
                        DoubleSupplier rotationSupplier,
                        DoubleSupplier povAngle
                        // BooleanSupplier slowDriveSupplier, 
                        // BooleanSupplier targetDriveSupplier
                        ) {
        this.xTranslationSupplier = xTranslationSupplier;
        this.yTranslationSupplier = yTranslationSupplier;
        this.rotationSupplier = rotationSupplier;
        // this.slowDriveSupplier = slowDriveSupplier;
        this.povAngle = povAngle;
        // this.targetDriveSupplier = targetDriveSupplier;

        addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        desiredAngle = drivetrain.getYaw();
        rotationVelocity = 0;
        isPOVControlled = false;
        isDriverControlled = false;
    }

    @Override
    public void execute() {
        double slowDrive = StateController.getInstance().getSlowDrive();
        boolean hasTargetDrive = StateController.getInstance().getTargetDriveActive();
        boolean hasAngleOverride = StateController.getInstance().getAngleOverrideActive();
        Pose2d targetDrive = StateController.getInstance().getTargetDrive();
        double angleOverride = StateController.getInstance().getAngleOverride();
        
        double xTranslation = xTranslationSupplier.getAsDouble() * MAX_VELOCITY_METERS_PER_SECOND * Robot.coordinateFlip * slowDrive;
        double yTranslation = yTranslationSupplier.getAsDouble() * MAX_VELOCITY_METERS_PER_SECOND * Robot.coordinateFlip * slowDrive;
        double rotation = Math.copySign(Math.pow(rotationSupplier.getAsDouble(), 3), rotationSupplier.getAsDouble()) * MAX_ANGULAR_SPEED_DEGREES_PER_SECOND * slowDrive;
        rotation *= 0.65;
        


        if (drivetrain.atAngleGoal()) isPOVControlled = false;
        // isPOVControlled = !drivetrain.atAngleGoal();

        if(rotation != 0){
            rotationVelocity = rotation * Robot.DEFAULT_PERIOD * 10;//Math.copySign(Math.sqrt(Math.abs(rotation)), rotation) * Robot.DEFAULT_PERIOD * 110;
            isDriverControlled = true;
            isPOVControlled = false;
            i = 0;
        } else {
            if (rotationVelocity > 300) {
                rotationVelocity -= rotationVelocity * Robot.DEFAULT_PERIOD * 2000;
                i = 0;
            } else if (rotationVelocity < -300) {
                rotationVelocity += rotationVelocity * Robot.DEFAULT_PERIOD * 2000;
                i = 0;
            } else {

                rotationVelocity = 0;
                if (i++ > 100) {
                    isDriverControlled = false;
                    i = 0;
                }
            }
        }
        if (!isPOVControlled && isDriverControlled) {
            desiredAngle = drivetrain.getYaw() + rotationVelocity;
        }

        if(povAngle.getAsDouble() != 1) {
            desiredAngle = povAngle.getAsDouble() - Robot.rotationOffset;
            isPOVControlled = true;
        }
        
        if (hasTargetDrive) desiredAngle = RobotPosition.calculateDegreesToTarget(targetDrive);
        
        if (hasAngleOverride) desiredAngle = angleOverride;
        drivetrain.setAngleGoal(desiredAngle);

        rotation = drivetrain.calculateAngleSpeed(!isPOVControlled && !hasAngleOverride);
        // Set the robot to drive in field relative mode, with the rotation controlled by the snap controller
        drivetrain.drive(xTranslation, yTranslation, rotation, true);
    }

    @Override
    public void end(boolean interrupted) {
        drivetrain.stop();
    }
}
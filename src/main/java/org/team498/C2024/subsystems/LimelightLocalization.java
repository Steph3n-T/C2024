package org.team498.C2024.subsystems;
import org.team498.C2024.FieldPositions;
import org.team498.lib.LimelightHelpers;
import org.team498.lib.LimelightHelpers.PoseEstimate;
import org.team498.lib.field.Point;
import org.team498.lib.field.Rectangle;

import com.ctre.phoenix6.hardware.Pigeon2;

import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.estimator.PoseEstimator;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class LimelightLocalization{
  private final SwerveDrivePoseEstimator PoseEstimator;
  private final Pigeon2 gyro;
  private boolean rejectLeftData;
  private boolean rejectRightData;
  public LimelightLocalization(SwerveDrivePoseEstimator PoseEstimator, Pigeon2 gyro) {

  this.PoseEstimator = PoseEstimator;
  this.gyro = gyro;
  int[] validIDs = {3,4};
  LimelightHelpers.SetFiducialIDFiltersOverride("leftLimelight", validIDs);
  LimelightHelpers.SetFiducialIDFiltersOverride("rightLimelight", validIDs);
  LimelightHelpers.SetRobotOrientation("leftLimelight", this.PoseEstimator.getEstimatedPosition().getRotation().getDegrees(), 0, 0, 0, 0, 0);
  LimelightHelpers.SetRobotOrientation("rightLimelight", this.PoseEstimator.getEstimatedPosition().getRotation().getDegrees(), 0, 0, 0, 0, 0);
  }

  public void update(){
    LimelightHelpers.SetRobotOrientation("leftLimelight", this.PoseEstimator.getEstimatedPosition().getRotation().getDegrees(), 0, 0, 0, 0, 0);
    LimelightHelpers.SetRobotOrientation("rightLimelight", this.PoseEstimator.getEstimatedPosition().getRotation().getDegrees(), 0, 0, 0, 0, 0);
    LimelightHelpers.PoseEstimate mt2l = LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2("leftLimelight");
    LimelightHelpers.PoseEstimate mt2r = LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2("rightLimelight");
    Rectangle field = new Rectangle(0, 0, FieldPositions.height, FieldPositions.width);
    boolean isInFieldLeft = field.contains(Point.fromPose2d(mt2l.pose));
    boolean isInFieldRight = field.contains(Point.fromPose2d(mt2r.pose));
    if(CommandSwerveDrivetrain.getInstance().isMoving()) // if our angular velocity is greater than 720 degrees per second, ignore vision updates
    {
      rejectLeftData = true;
      rejectRightData = true;
    }
    if(mt2r.tagCount == 0 || !isInFieldRight)
    {
      rejectRightData = true;
    }
    if(mt2l.tagCount == 0 || !isInFieldLeft)
    {
      rejectLeftData = true;
    }
    if(!rejectRightData)
    {
     this.PoseEstimator.setVisionMeasurementStdDevs(VecBuilder.fill(.7,.7,9999999));
      this.PoseEstimator.addVisionMeasurement(
          mt2r.pose,
          mt2r.timestampSeconds);
    }
    if(!rejectLeftData)
    {
     this.PoseEstimator.setVisionMeasurementStdDevs(VecBuilder.fill(.7,.7,9999999));
      this.PoseEstimator.addVisionMeasurement(
          mt2l.pose,
          mt2l.timestampSeconds);
    }
  }
}

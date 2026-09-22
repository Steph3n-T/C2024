package org.team498.C2024.subsystems;

import java.io.UncheckedIOException;
import java.util.Optional;

import org.photonvision.EstimatedRobotPose;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.PhotonPoseEstimator.PoseStrategy;
import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation;

public class PhotonVision {
    public PhotonCamera leftCamera;
    public PhotonCamera rightCamera;
    public PhotonPoseEstimator rightPoseEstimator;
    public PhotonPoseEstimator leftPoseEstimator;

    // private class CameraInputs{
    //     public boolean connected = false;
    //     public byte[] targetData = {};
    //     public double targetTimestamp = 0.0;
    //     public double[] cameraMatrixData = {};
    //     public double[] distanceCoefficientData = {};
    // }

    // private CameraInputs rightInputs;
    // private CameraInputs leftInputs;

    private final Transform3d rightCameraPose = new Transform3d(
            new Translation3d(Units.inchesToMeters(-3.4),// should be positive
                Units.inchesToMeters(-10.375),
                Units.inchesToMeters(21)),
            new Rotation3d(0, Units.degreesToRadians(-30), 0));

    private final Transform3d leftCameraPose = new Transform3d(
        new Translation3d(Units.inchesToMeters(-3.4), //should be negative
            Units.inchesToMeters(10.375),
            Units.inchesToMeters(21)),
        new Rotation3d(0, Units.degreesToRadians(-30), 0));
    
    public PhotonVision(){
        PhotonCamera.setVersionCheckEnabled(false);
        rightCamera = new PhotonCamera("red 2 camera"); //ACTUALLY RED
        leftCamera = new PhotonCamera("blue 2 camera"); //ACTUALLY BLUE
        // rightInputs = new CameraInputs();
        // leftInputs = new CameraInputs();

        try{
            AprilTagFieldLayout aprilTagFieldLayout = AprilTagFields.k2024Crescendo.loadAprilTagLayoutField();
            rightPoseEstimator = new PhotonPoseEstimator(aprilTagFieldLayout,PoseStrategy.MULTI_TAG_PNP_ON_COPROCESSOR, rightCamera, rightCameraPose);
            leftPoseEstimator = new PhotonPoseEstimator(aprilTagFieldLayout, PoseStrategy.MULTI_TAG_PNP_ON_COPROCESSOR, leftCamera, leftCameraPose);
            rightPoseEstimator.setMultiTagFallbackStrategy(PoseStrategy.LOWEST_AMBIGUITY);
            
            leftPoseEstimator.setMultiTagFallbackStrategy(PoseStrategy.LOWEST_AMBIGUITY);
        }catch(UncheckedIOException e){
            DriverStation.reportError("Failed to load AprilTagFieldLayout", e.getStackTrace());
            rightPoseEstimator = null;
            leftPoseEstimator = null;
        }

    }

    public Optional<EstimatedRobotPose> rightEstimatedPose() {
        if (rightPoseEstimator == null || !rightCameraConnected()){
            return Optional.empty();
        }
        return rightPoseEstimator.update();
    }

    public Optional<EstimatedRobotPose> leftEstimatedPose() {
        if (leftPoseEstimator == null || !leftCameraConnected()){
            return Optional.empty();
        }
        
        return leftPoseEstimator.update();
    }

    public Optional<TimedPose> getEstimatedPose(){

        boolean leftEnabled = leftCamera.isConnected();
        boolean rightEnabled = rightCamera.isConnected();

        Optional<EstimatedRobotPose> rightPose = Optional.empty();
        Optional<EstimatedRobotPose> leftPose = Optional.empty();
        if (rightEnabled) rightPose = rightEstimatedPose();
        if (leftEnabled) leftPose = leftEstimatedPose();

        if (rightPose.isPresent() && leftPose.isPresent())
            return Optional.of(averagePoses(new TimedPose(leftPose.get().estimatedPose.toPose2d(), leftPose.get().timestampSeconds), new TimedPose(rightPose.get().estimatedPose.toPose2d(), rightPose.get().timestampSeconds)));
        if (rightPose.isPresent())
            return Optional.of(new TimedPose(rightPose.get().estimatedPose.toPose2d(), rightPose.get().timestampSeconds));
        if (leftPose.isPresent())
            return Optional.of(new TimedPose(leftPose.get().estimatedPose.toPose2d(), leftPose.get().timestampSeconds));
        return Optional.empty();
        //return Optional.of(averagePoses(new TimedPose(leftPose.get().estimatedPose.toPose2d(), leftPose.get().timestampSeconds), new TimedPose(rightPose.get().estimatedPose.toPose2d(), rightPose.get().timestampSeconds)));
        
    }

    public boolean leftCameraConnected(){
        return leftCamera.isConnected();
    }
    
    public boolean rightCameraConnected(){
        return rightCamera.isConnected();
    }

    public TimedPose averagePoses(TimedPose poseOne, TimedPose poseTwo){
        double timestamp = (poseOne.timeStamp + poseTwo.timeStamp) / 2;
        double x = (poseOne.pose.getX() + poseTwo.pose.getX()) / 2;
        double y = (poseOne.pose.getY() + poseTwo.pose.getY()) / 2;
        Rotation2d rotation = Rotation2d.fromDegrees((poseOne.pose.getRotation().getDegrees() + poseTwo.pose.getRotation().getDegrees()) / 2);
        return new TimedPose(new Pose2d(x, y, rotation), timestamp);
}

    public class TimedPose{
        Pose2d pose;
        double timeStamp;

        public TimedPose(Pose2d pose, double timeStamp){
            this.pose = pose;
            this.timeStamp = timeStamp;
        }

        // public EstimatedRobotPose toEstimatedRobotPose() {
        //     return new EstimatedRobotPose(null, timeStamp, null, null)
        // }
    }

    private static PhotonVision instance;

    public static PhotonVision getInstance() {
        if (instance == null) {
            instance = new PhotonVision();
        }
        return instance;
    }
}

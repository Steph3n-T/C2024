package org.team498.C2024.subsystems;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.estimator.PoseEstimator;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import org.team498.lib.LimelightHelpers;
import org.team498.lib.LimelightHelpers.LimelightTarget_Detector;
import org.team498.lib.LimelightHelpers.PoseEstimate;


public class Limelight extends SubsystemBase{
   private double xMax = 15;
   private double xMin = -15;
   private double yMax = 15;
   private double yMin = -15;
   private boolean hasCenteredTarget = false;

    public void periodic(){
        hasCenteredTarget = false;
        for (LimelightTarget_Detector target : getResults()) {
            if (isCentered(target.tx,target.ty)) hasCenteredTarget = true;
        }
        SmartDashboard.putBoolean("Limelight Target", hasCenteredTarget);
    }

    public boolean hasCenteredTarget(){
        return hasCenteredTarget;
    }

    public LimelightTarget_Detector[] getResults(){
        return LimelightHelpers.getLatestResults("limelight").targetingResults.targets_Detector;

    }
    
    public boolean isCentered(double x, double y){
        if ((x > xMax || x < xMin) || (y > yMax || y < yMin))
        return false;
        return true;
    }

    

    private static Limelight instance;
    
    public static Limelight getInstance(){
        if (instance == null)
        instance = new Limelight();
        return instance;
    }
}
package org.team498.lib.wpilib;

import edu.wpi.first.math.geometry.Rotation2d;

public class ChassisSpeeds extends edu.wpi.first.math.kinematics.ChassisSpeeds {
    public ChassisSpeeds() {
        super();
    }

    public ChassisSpeeds(double vxMetersPerSecond, double vyMetersPerSecond, double omegaRadiansPerSecond) {
        this.vxMetersPerSecond = vxMetersPerSecond;
        this.vyMetersPerSecond = vyMetersPerSecond;
        this.omegaRadiansPerSecond = omegaRadiansPerSecond;
    }

    public static ChassisSpeeds fromFieldRelativeSpeeds(double vxMetersPerSecond,
                                                        double vyMetersPerSecond,
                                                        double omegaRadiansPerSecond,
                                                        double robotAngle) {
        double angleInRadians = Math.toRadians(robotAngle);
        double vxRobot = vxMetersPerSecond * Math.cos(angleInRadians) + vyMetersPerSecond * Math.sin(angleInRadians);
        double vyRobot = -vxMetersPerSecond * Math.sin(angleInRadians) + vyMetersPerSecond * Math.cos(angleInRadians);
        return new ChassisSpeeds(vxRobot, vyRobot, omegaRadiansPerSecond);
    }

    public static edu.wpi.first.math.kinematics.ChassisSpeeds fromFieldRelativeSpeeds(edu.wpi.first.math.kinematics.ChassisSpeeds fieldRelativeSpeeds,
                                                                                      double robotAngle) {
        return fromFieldRelativeSpeeds(fieldRelativeSpeeds.vxMetersPerSecond,
                                       fieldRelativeSpeeds.vyMetersPerSecond,
                                       fieldRelativeSpeeds.omegaRadiansPerSecond,
                                       robotAngle
        );
    }

    public static ChassisSpeeds toFieldRelativeSpeeds(double vxMetersPerSecond,
                                                      double vyMetersPerSecond,
                                                      double omegaRadiansPerSecond,
                                                      Rotation2d robotAngle) {
        return new ChassisSpeeds(vxMetersPerSecond * robotAngle.getCos() - vyMetersPerSecond * robotAngle.getSin(),
                                 vxMetersPerSecond * robotAngle.getSin() + vyMetersPerSecond * robotAngle.getCos(),
                                 omegaRadiansPerSecond
        );
    }

    public static ChassisSpeeds toFieldRelativeSpeeds(ChassisSpeeds fieldRelativeSpeeds, Rotation2d robotAngle) {
        return toFieldRelativeSpeeds(fieldRelativeSpeeds.vxMetersPerSecond,
                                     fieldRelativeSpeeds.vyMetersPerSecond,
                                     fieldRelativeSpeeds.omegaRadiansPerSecond,
                                     robotAngle
        );
    }

    public static ChassisSpeeds toFieldRelativeSpeeds(edu.wpi.first.math.kinematics.ChassisSpeeds fieldRelativeSpeeds, Rotation2d robotAngle) {
        return toFieldRelativeSpeeds(fieldRelativeSpeeds.vxMetersPerSecond,
                                     fieldRelativeSpeeds.vyMetersPerSecond,
                                     fieldRelativeSpeeds.omegaRadiansPerSecond,
                                     robotAngle
        );
    }

    public static ChassisSpeeds toFieldRelativeSpeeds(ChassisSpeeds fieldRelativeSpeeds, double robotAngleDegrees) {
        return toFieldRelativeSpeeds(fieldRelativeSpeeds, Rotation2d.fromDegrees(robotAngleDegrees));
    }


    public static ChassisSpeeds fromWPIChassisSpeeds(edu.wpi.first.math.kinematics.ChassisSpeeds speeds) {
        return new ChassisSpeeds(speeds.vxMetersPerSecond, speeds.vyMetersPerSecond, speeds.omegaRadiansPerSecond);
    }


}

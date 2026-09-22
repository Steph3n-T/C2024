package org.team498.C2024;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

import org.team498.lib.field.Point;
import org.team498.lib.field.Rectangle;
import org.team498.lib.util.PoseUtil;

import java.util.LinkedList;
import java.util.Map;

public final class FieldPositions {
    public static final double height = 8.21055;
    public static final double width = 16.54175;

    public static final double midline = width / 2;

    public static final Point blueSpeaker = new Point(0.16, 5.57); //0.16, 5.57
    // public static final Point blueAmp = new Point(1.82, 8.10);

    public static final Point[] blueNotes = {
        new Point(2.89, 6.997),
        new Point(2.89, 5.552),
        new Point(2.89, 4.107)
    };

    public static final Point[] midNotes = {
        new Point(8.28, 7.445),
        new Point(8.28, 5.782),
        new Point(8.28, 4.109),
        new Point(8.28, 2.441),
        new Point(8.28, 0.771)
    };


    public static final Point[] redNotes = {
        new Point(13.665, 6.997),
        new Point(13.665, 5.552),
        new Point(13.665, 4.107)
    };

    public static Pose2d getSpeaker(){
        return Robot.alliance.get() == Alliance.Blue ? blueSpeaker.toPose2d() : PoseUtil.flip(blueSpeaker.toPose2d());
    }
    
    // public static Pose2d getAmp(){
    //     return Robot.alliance.get() == Alliance.Blue ? blueSpeaker.toPose2d() : PoseUtil.flip(blueSpeaker.toPose2d());
    // }

    public static Point flip(Point input) {
        return new Point(midline + (midline - input.x), input.y);
    }

    public static Rectangle flip(Rectangle input) {
        return new Rectangle(midline + (midline - input.width - input.x), input.y, input.width, input.height);
    }

    public static void displayAll() {
        // Robot.field.getObject("Nodes").setPoses(nodePoses);

        LinkedList<Pose2d> aprilTagPoses = new LinkedList<>();
        for (int i = 1; i <= aprilTags.size(); i++) {
            aprilTagPoses.add(new Pose2d(aprilTags.get(i).getX(), aprilTags.get(i).getY(), new Rotation2d()));
        }

        // Robot.field.getObject("AprilTags").setPoses(aprilTagPoses);
    }

    public static final Map<Integer, Pose3d> aprilTags = Map.of(1,
                                                                new Pose3d(Units.inchesToMeters(610.77),
                                                                           Units.inchesToMeters(42.19),
                                                                           Units.inchesToMeters(18.22),
                                                                           new Rotation3d(0.0, 0.0, Math.PI)
                                                                ),
                                                                2,
                                                                new Pose3d(Units.inchesToMeters(610.77),
                                                                           Units.inchesToMeters(108.19),
                                                                           Units.inchesToMeters(18.22),
                                                                           new Rotation3d(0.0, 0.0, Math.PI)
                                                                ),
                                                                3,
                                                                new Pose3d(Units.inchesToMeters(610.77),
                                                                           Units.inchesToMeters(174.19),
                                                                           Units.inchesToMeters(18.22),
                                                                           new Rotation3d(0.0, 0.0, Math.PI)
                                                                ),
                                                                4,
                                                                new Pose3d(Units.inchesToMeters(636.96),
                                                                           Units.inchesToMeters(265.74),
                                                                           Units.inchesToMeters(27.38),
                                                                           new Rotation3d(0.0, 0.0, Math.PI)
                                                                ),
                                                                5,
                                                                new Pose3d(Units.inchesToMeters(14.25),
                                                                           Units.inchesToMeters(265.74),
                                                                           Units.inchesToMeters(27.38),
                                                                           new Rotation3d()
                                                                ),
                                                                6,
                                                                new Pose3d(Units.inchesToMeters(40.45),
                                                                           Units.inchesToMeters(174.19),
                                                                           Units.inchesToMeters(18.22),
                                                                           new Rotation3d()
                                                                ),
                                                                7,
                                                                new Pose3d(Units.inchesToMeters(40.45),
                                                                           Units.inchesToMeters(108.19),
                                                                           Units.inchesToMeters(18.22),
                                                                           new Rotation3d()
                                                                ),
                                                                8,
                                                                new Pose3d(Units.inchesToMeters(40.45),
                                                                           Units.inchesToMeters(42.19),
                                                                           Units.inchesToMeters(18.22),
                                                                           new Rotation3d()
                                                                )
    );

}
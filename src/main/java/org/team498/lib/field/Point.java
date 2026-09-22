package org.team498.lib.field;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;

import java.awt.geom.Point2D;


public class Point extends Point2D.Double implements BaseRegion {

    /**
     * Constructs and initializes a {@code Point} with the specified coordinates.
     *
     * @param x the X coordinate of the newly constructed {@code Point}
     * @param y the Y coordinate of the newly constructed {@code Point}
     */
    public Point(double x, double y) {
        super(x, y);
    }

    /**
     * Constructs and initializes a {@code Point} from a WPILib {@code Pose2d}
     *
     * @param pose pose of the newly constructed {@code Point}
     */
    public static Point fromPose2d(Pose2d pose) {
        return new Point(pose.getX(), pose.getY());
    }

    public Pose2d toPose2d() {
        return new Pose2d(x, y, new Rotation2d());
    }

    /**
     * Displays this region on a {@link edu.wpi.first.wpilibj.smartdashboard.Field2d Field2d} object.
     *
     * @param name the name of the object
     */
    @Override
    public void displayOnDashboard(String name) {
        // Robot.field.getObject(name).setPose(toPose2d());
    }

    /**
     * Check if a given point is within the region. Always returns false because points have no area.
     *
     * @return false
     */
    @Override
    public boolean contains(Point point) {
        return false;
    }
}

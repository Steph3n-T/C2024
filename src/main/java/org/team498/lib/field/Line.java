package org.team498.lib.field;

import edu.wpi.first.wpilibj.smartdashboard.Field2d;

import java.awt.geom.Line2D;


public class Line extends Line2D.Double implements BaseRegion {
    /**
     * Constructs and initializes a {@code Line2D} from the specified {@code Point2D} objects.
     *
     * @param p1 the start {@code Point2D} of this line segment
     * @param p2 the end {@code Point2D} of this line segment
     */
    public Line(Point p1, Point p2) {
        super(p1, p2);
    }

    /**
     * Constructs and initializes a {@code Line2D} from the specified coordinates.
     *
     * @param x1 the X coordinate of the start point
     * @param y1 the Y coordinate of the start point
     * @param x2 the X coordinate of the end point
     * @param y2 the Y coordinate of the end point
     */
    public Line(double x1, double y1, double x2, double y2) {
        super(x1, y1, x2, y2);
    }

    /**
     * Displays this region on a {@link Field2d Field2d} object.
     *
     * @param name the name of the object
     */
    @Override
    public void displayOnDashboard(String name) {
        // Robot.field.getObject(name).setPoses(new Pose2d(x1, y1, new Rotation2d()), new Pose2d(x2, y2, new Rotation2d()));
    }

    /**
     * Check if a given point is within the region. Always returns false because lines have no area.
     *
     * @return false
     */
    @Override
    public boolean contains(Point point) {
        return false;
    }
}
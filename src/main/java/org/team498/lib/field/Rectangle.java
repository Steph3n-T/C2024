package org.team498.lib.field;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;

import java.awt.geom.Rectangle2D;


public class Rectangle extends Rectangle2D.Double implements BaseRegion {

    /**
     * Constructs and initializes a {@code Rectangle} from the specified {@code double} coordinates.
     *
     * @param x the X coordinate of the lower-left corner of the newly constructed {@code Rectangle2D}
     * @param y the Y coordinate of the lower-left corner of the newly constructed {@code Rectangle2D}
     * @param w the width of the newly constructed {@code Rectangle2D}
     * @param h the height of the newly constructed {@code Rectangle2D}
     */
    public Rectangle(double x, double y, double w, double h) {
        super(x, y, w, h);
    }

    /**
     * Displays this region on a {@link Field2d Field2d} object.
     *
     * @param name the name of the object
     */
    @Override
    public void displayOnDashboard(String name) {
        Pose2d bottomLeft = new Pose2d(x + width, y, new Rotation2d());
        Pose2d bottomRight = new Pose2d(x + width, y + height, new Rotation2d());
        Pose2d upperRight = new Pose2d(x, y + height, new Rotation2d());
        Pose2d upperLeft = new Pose2d(x, y, new Rotation2d());

        // Robot.field.getObject(name).setPoses(bottomLeft, bottomRight, upperRight, upperLeft, bottomLeft);
    }

    /**
     * Check if a given point is within the region.
     *
     * @param point the point to test
     * @return true if the point is in the region, false otherwise
     */
    @Override
    public boolean contains(Point point) {
        return super.contains(point);
    }
}

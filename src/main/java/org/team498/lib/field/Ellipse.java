package org.team498.lib.field;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;

import java.awt.geom.Ellipse2D;
import java.util.LinkedList;


public class Ellipse extends Ellipse2D.Double implements BaseRegion {
    /**
     * Constructs and initializes an {@code Ellipse2D} from a framing rectangle.
     *
     * @param rectangle the framing rectangle
     */
    public Ellipse(Rectangle rectangle) {
        super(rectangle.x, rectangle.y, rectangle.height, rectangle.width);
    }

    /**
     * Displays this region on a {@link Field2d Field2d} object.
     *
     * @param name the name of the object
     */
    @Override
    public void displayOnDashboard(String name) {
        LinkedList<Pose2d> allPoses = new LinkedList<>();

        // Number of lines to draw the ellipse with, must be less than 85 for it to draw the whole thing
        double lines = 25;

        for (int angle = 0; angle < lines + 1; angle++) {

            double angleRadians = Math.toRadians(angle * (360 / lines));

            double centerX = x + (width / 2);
            double centerY = y + (height / 2);

            double a = width / 2;
            double b = height / 2;

            double xCoordinate = centerX + a * Math.cos(angleRadians);
            double yCoordinate = centerY + b * Math.sin(angleRadians);

            allPoses.add(new Pose2d(xCoordinate, yCoordinate, new Rotation2d()));
        }

        // Robot.field.getObject(name).setPoses(allPoses);
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

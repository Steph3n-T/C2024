package org.team498.lib.field;

public interface BaseRegion {
    /**
     * Displays this region on a {@link edu.wpi.first.wpilibj.smartdashboard.Field2d Field2d} object.
     *
     * @param name the name of the object
     */
    void displayOnDashboard(String name);

    /**
     * Check if a given point is within the region.
     *
     * @param point the point to test
     * @return true if the point is in the region, false otherwise
     */
    boolean contains(Point point);
}

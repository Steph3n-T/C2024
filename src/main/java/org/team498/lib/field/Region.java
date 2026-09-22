package org.team498.lib.field;

import java.util.ArrayList;
import java.util.Collections;

public class Region implements BaseRegion {
    private final ArrayList<BaseRegion> regions = new ArrayList<>();

    public Region(BaseRegion... regions) {
        Collections.addAll(this.regions, regions);
    }

    @Override
    public boolean contains(Point point) {
        for (BaseRegion region : regions) {
            if (region.contains(point)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void displayOnDashboard(String name) {
        int regionCount = 0;
        for (BaseRegion region : regions) {
            region.displayOnDashboard(name + " " + regionCount);
            regionCount++;
        }
    }

}
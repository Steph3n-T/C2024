package org.team498.lib.auto;

//import org.team498.C2024.State;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;

public interface Auto {
    default String getName() {
        return getClass().getSimpleName();
    }

    Command getCommand();
    Pose2d getInitialPose();
    //State getInitialState();
}

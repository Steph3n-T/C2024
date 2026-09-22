package org.team498.C2024;
import org.team498.C2024.commands.drivetrain.PathPlannerFollower;
import org.team498.lib.util.Trajectories;
import com.pathplanner.lib.path.PathPlannerTrajectory;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;

public class PathLib {
    //public static final PathPlannerTrajectory choreoTest = Trajectories.getChoreoTrajectory("NewPath");
    // public static final PathPlannerTrajectory FifthNoteToSecondNoteUnderStage = Trajectories.getChoreoTrajectory("FifthNoteToSecondNoteUnderStage", Rotation2d.fromRadians(0));
    // public static final PathPlannerTrajectory FirstNoteToFifthNote = Trajectories.getChoreoTrajectory("FirstNoteToFifthNote", Rotation2d.fromRadians(1.056));
    // public static final PathPlannerTrajectory MidSubwooferToFirstNote = Trajectories.getChoreoTrajectory("MidSubwooferToFirstNote", Rotation2d.fromRadians(0));
    // public static final PathPlannerTrajectory GavinsPath = Trajectories.getPathPlannerTrajectory("GavinsPath", Rotation2d.fromDegrees(0));
    // public static final PathPlannerTrajectory SL1toNote1 = Trajectories.getChoreoTrajectory("SL1toNote1", Rotation2d.fromRadians(-3.14));
    // public static final PathPlannerTrajectory Note1toNote2 = Trajectories.getChoreoTrajectory("Note1toNote2", Rotation2d.fromRadians(-3.14));
    // public static final PathPlannerTrajectory Note2Note3 = Trajectories.getChoreoTrajectory("Note2Note3", Rotation2d.fromRadians(-1.695));
    // public static final PathPlannerTrajectory SL1Note1 = Trajectories.getChoreoTrajectory("SL1Note1", Rotation2d.fromDegrees(-90));
    public static final PathPlannerTrajectory SL1Note1 = Trajectories.getChoreoTrajectory("SL1Note1", Rotation2d.fromDegrees(-90));
    public static final PathPlannerTrajectory Note1Note2 = Trajectories.getChoreoTrajectory("Note1toNote2", Rotation2d.fromDegrees(-90));
    public static final PathPlannerTrajectory Note2Note3 = Trajectories.getChoreoTrajectory("Note2Note3", Rotation2d.fromDegrees(-90));
    public static final PathPlannerTrajectory ThirdNoteSL2 = Trajectories.getChoreoTrajectory("ThirdNoteSL2", Rotation2d.fromDegrees(-90));
    public static final PathPlannerTrajectory FourNoteFull = Trajectories.getChoreoTrajectory("FourNoteFull", Rotation2d.fromDegrees(-90));

    public static final PathPlannerTrajectory SixSource1 = Trajectories.getChoreoTrajectory("six_source_1", Rotation2d.fromDegrees(180));
    public static final PathPlannerTrajectory SixSource2 = Trajectories.getChoreoTrajectory("six_source_2", Rotation2d.fromRadians(1.464));
    public static final PathPlannerTrajectory SixSource3 = Trajectories.getChoreoTrajectory("six_source_3", Rotation2d.fromRadians(3.118));
    
    public static final PathPlannerTrajectory Test1 = Trajectories.getChoreoTrajectory("test_1", Rotation2d.fromDegrees(180));

    public static final PathPlannerTrajectory SixAmp1 = Trajectories.getChoreoTrajectory("six_amp_1", Rotation2d.fromDegrees(180));
    public static final PathPlannerTrajectory SixAmp2 = Trajectories.getChoreoTrajectory("six_amp_2", Rotation2d.fromRadians(-2.8510430948694685));
    public static final PathPlannerTrajectory SixAmp3 = Trajectories.getChoreoTrajectory("six_amp_3", Rotation2d.fromDegrees(180));

    public static final PathPlannerTrajectory four_port_1 = Trajectories.getChoreoTrajectory("four_port_1", Rotation2d.fromDegrees(180));
    public static final PathPlannerTrajectory four_port_2 = Trajectories.getChoreoTrajectory("four_port_2", Rotation2d.fromRadians(2.028));
    public static final PathPlannerTrajectory four_port_3 = Trajectories.getChoreoTrajectory("four_port_3", Rotation2d.fromRadians(2.142));

    public static final PathPlannerTrajectory pre_taxi_1 = Trajectories.getChoreoTrajectory("pre_taxi_1", Rotation2d.fromRadians(2.4));
    public static final PathPlannerTrajectory pre_taxi_2 = Trajectories.getChoreoTrajectory("pre_taxi_2", Rotation2d.fromDegrees(180));
    public static final PathPlannerTrajectory pre_taxi_3 = Trajectories.getChoreoTrajectory("pre_taxi_3", Rotation2d.fromRadians(-2.078));

    public static final PathPlannerTrajectory four_wing_1 = Trajectories.getChoreoTrajectory("four_wing_1", Rotation2d.fromDegrees(180));
    public static final PathPlannerTrajectory four_wing_2 = Trajectories.getChoreoTrajectory("four_wing_2", Rotation2d.fromRadians(2.85));
    public static final PathPlannerTrajectory four_wing_3 = Trajectories.getChoreoTrajectory("four_wing_3", Rotation2d.fromRadians(-3.058));

    public static final PathPlannerTrajectory spit = Trajectories.getChoreoTrajectory("spit", Rotation2d.fromDegrees(180));

    public static final PathPlannerTrajectory long_taxi = Trajectories.getChoreoTrajectory("long_taxi", Rotation2d.fromRadians(2.048));

    public static final PathPlannerTrajectory troll = Trajectories.getChoreoTrajectory("troll", Rotation2d.fromRadians(1.565));

    public static final PathPlannerTrajectory outer_wing_1 = Trajectories.getChoreoTrajectory("outer_wing_1", Rotation2d.fromRadians(-0.464));
    public static final PathPlannerTrajectory outer_wing_2 = Trajectories.getChoreoTrajectory("outer_wing_2", Rotation2d.fromRadians(-3.140));
    public static final PathPlannerTrajectory outer_wing_3 = Trajectories.getChoreoTrajectory("outer_wing_3", Rotation2d.fromRadians(0.938));

    public static final PathPlannerTrajectory outer_wing_4 = Trajectories.getChoreoTrajectory("outer_wing_4", Rotation2d.fromDegrees(180));
    public static final PathPlannerTrajectory outer_wing_5 = Trajectories.getChoreoTrajectory("outer_wing_5", Rotation2d.fromDegrees(180));
    public static final PathPlannerTrajectory outer_wing_6 = Trajectories.getChoreoTrajectory("outer_wing_6", Rotation2d.fromDegrees(180));
    public static final PathPlannerTrajectory outer_wing_7 = Trajectories.getChoreoTrajectory("outer_wing_7", Rotation2d.fromRadians(-0.464));


    // public static final Command FourPortOne = new PathPlannerFollower(four_port_1);
    // public static final Command FourPortTwo = new PathPlannerFollower(four_port_2);
    // public static final Command FourPortThree = new PathPlannerFollower(four_port_3);
    // public static final PathPlannerTrajectory Note3Mid5 = Trajectories.getChoreoTrajectory("Note3Mid5");
    // public static final PathPlannerTrajectory Mid5Note3 = Trajectories.getChoreoTrajectory("Mid5Note3");
}
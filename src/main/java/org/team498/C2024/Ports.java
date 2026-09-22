package org.team498.C2024;

public final class Ports {

    public static class DrivetrainPorts {
        public static final int FL_DRIVE = 1;
        public static final int FR_DRIVE = 5;
        public static final int BL_DRIVE = 3;
        public static final int BR_DRIVE = 7;

        public static final int FL_STEER = 2;
        public static final int FR_STEER = 6;
        public static final int BL_STEER = 4;
        public static final int BR_STEER = 8;

        public static final int FL_CANCODER = 11;
        public static final int FR_CANCODER = 13;
        public static final int BL_CANCODER = 12;
        public static final int BR_CANCODER = 14;

        public static final int GYRO = 15;
    }

    public static class ShooterPorts {
        public static final int LEFT_MOTOR = 32;
        public static final int RIGHT_MOTOR = 31;
        public static final int ANGLE_MOTOR = 33;
        public static final int FEED_MOTOR = 34;
        public static final int ANGLE_ENCODER = 35;//DIO
    }

    public static class HopperPorts {
        public static final int TOP_MOTOR = 40;
        public static final int BOTTOM_MOTOR = 42;
        public static final int ENCODER = 41;
        public static final int BACK_BEAM_BREAk = 9; //DIO
        public static final int FRONT_BEAM_BREAk = 6;
    }
    public static class KickerPorts {
        public static final int MOTOR = 43;
        public static final int KICKER_BEAM_BREAK = 7;
    }

    public static class IntakePorts {
        public static final int MOTOR = 50;
        public static final int ANGLE_ENCODER = 8;
        // public static final int ANGLE_ENCODER = 2;
    }

    public static class IntakeRollersPorts {
        public static final int MOTOR = 53;

    }
    

    public static class Accessories {
        public static final int BLINKIN = 0; // PWM
        // public static final int SETUP_SWITCH =0; //DIO
        public static final String DriveBus = "bus"; //Canivore's name
        public static final String RioBus =  "rio"; //Name of the native bus on the roborio
    }
}


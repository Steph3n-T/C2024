package org.team498.C2024;

public enum State {
    IDLE(Shooter.IDLE, Hopper.IDLE, Intake.IDLE, IntakeRollers.IDLE, Kicker.IDLE),
    SOURCE(Shooter.SOURCE, Hopper.IDLE, Intake.INTAKE, IntakeRollers.IDLE, Kicker.IDLE),
    INTAKE(Shooter.IDLE, Hopper.REVERSE, Intake.INTAKE, IntakeRollers.INTAKE, Kicker.IDLE),
    AUTO_SHOT(Shooter.IDLE, Hopper.REVERSE, Intake.AUTO_SHOT, IntakeRollers.INTAKE, Kicker.IDLE),
    OUTTAKE(Shooter.IDLE, Hopper.FORWARD, Intake.OUTTAKE, IntakeRollers.OUTTAKE, Kicker.IDLE),
    AMP(Shooter.AMP, Hopper.AMP, Intake.IDLE, IntakeRollers.IDLE, Kicker.FORWARD),
    SUBWOOFER(Shooter.SUBWOOFER, Hopper.REVERSE, Intake.IDLE, IntakeRollers.IDLE, Kicker.REVERSE),
    FEED(Shooter.FEED, Hopper.REVERSE, Intake.IDLE, IntakeRollers.IDLE, Kicker.REVERSE),
    PODIUM(Shooter.PODIUM, Hopper.REVERSE, Intake.IDLE, IntakeRollers.IDLE, Kicker.REVERSE),
    CRESCENDO(Shooter.CRESCENDO, Hopper.REVERSE, Intake.IDLE, IntakeRollers.IDLE, Kicker.REVERSE),
    CANCEL_AMP(Shooter.IDLE, Hopper.IDLE, Intake.IDLE, IntakeRollers.IDLE, Kicker.REVERSE),
    CANCEL_SPEAKER(Shooter.IDLE, Hopper.IDLE, Intake.IDLE, IntakeRollers.IDLE, Kicker.FORWARD),
    TRACK_NOTE(Shooter.VISION, Hopper.REVERSE, Intake.INTAKE, IntakeRollers.INTAKE, Kicker.IDLE),
    AUTO(Shooter.AUTO, Hopper.REVERSE, Intake.INTAKE, IntakeRollers.INTAKE, Kicker.REVERSE),
    SPIT(Shooter.SPIT, Hopper.REVERSE, Intake.INTAKE, IntakeRollers.INTAKE, Kicker.REVERSE),
    FRONT_PODIUM(Shooter.FRONT_PODIUM, Hopper.REVERSE, Intake.IDLE, IntakeRollers.IDLE, Kicker.REVERSE),
    AMP_SPEAKER(Shooter.AMP_SPEAKER, Hopper.REVERSE, Intake.IDLE, IntakeRollers.IDLE, Kicker.REVERSE),
    OUTREACH(Shooter.OUTREACH, Hopper.REVERSE, Intake.IDLE, IntakeRollers.IDLE, Kicker.REVERSE),
    OUTER_STAGE(Shooter.OUTER_STAGE, Hopper.REVERSE, Intake.IDLE, IntakeRollers.IDLE, Kicker.REVERSE),
    SANGHI(Shooter.SANGHI, Hopper.REVERSE, Intake.IDLE, IntakeRollers.IDLE, Kicker.REVERSE),
    FARSHOT(Shooter.FARSHOT, Hopper.REVERSE, Intake.IDLE, IntakeRollers.IDLE, Kicker.REVERSE);



    public final Shooter shooter;
    public final Hopper hopper;
    public final Intake intake;
    public final IntakeRollers intakeRollers;
    public final Kicker kicker;


    State(Shooter shooter, Hopper hopper, Intake intake, IntakeRollers intakeRollers, Kicker kicker) {
        this.shooter = shooter;
        this.hopper = hopper;
        this.intake = intake;
        this.intakeRollers = intakeRollers;
        this.kicker = kicker;
    }

    //Sets Speed and Angle for Shooter
    public enum Shooter {
        IDLE(0, 0, 40),
        SUBWOOFER(3900, 1000, 54),
        FEED(3300, 1000, 52), //49 //3200
        AMP(0, 0, 32.5),
        PODIUM(3800, 1000, 35), //38
        CRESCENDO(3500, 1000, 40),
        SOURCE(0, 0, 50),
        PREPARE(4500, 1000, 40),
        AUTOINTAKE(3800, 0, 33),
        VISION(0, 0, 40),
        AUTO(4000, 1000, 40),
        CLIMB_UP(0, 0, 70),
        CLIMB_DOWN(0, 0, 30),
        TRAP(3500, 1000, 50),
        SPIT(3800, 1000, 43),
        FRONT_PODIUM(4000, 1000, 37.75), //31.5
        AMP_SPEAKER(3600, 1000, 34),
        OUTREACH(2800, 1000, 35),
        OUTER_STAGE(4500, 1000, 25.5),
        SANGHI(4500, 1000, 29),
        FARSHOT(4500, 1000, 28.25);


        public final double speed;
        public final double feedSpeed;
        public final double angle;

        Shooter(double speed, double feedSpeed, double angle) {
            this.speed = speed;
            this.feedSpeed = feedSpeed;
            this.angle = angle;
        }
    }

    //Sets Speeds for Kicker
    public enum Kicker {
        IDLE(0),
        //REVERSE is for PODIUM, SUBWOOFER, and CRESCENDO
        REVERSE(-1),
        //FORWARD is for AMP
        FORWARD(1);


    public final double speed;
        Kicker(double speed){
            this.speed = speed;
        }
    }

    //Sets Speeds for Hopper
    public enum Hopper {
        IDLE(0),
        FORWARD(0.5),
        REVERSE(-0.5),
        AMP(-0.845);

        public final double speed;

        Hopper(double speed) {
            this.speed = speed;
        }
    }

    //Sets Positions for Intake
    public enum Intake {
        IDLE(1.9), //1.96
        INTAKE(0.05),
        AUTO_SHOT(1),
        // AUTO_INTAKE(0.7),
        OUTTAKE(0.05);

        public final double speed;
        
        Intake(double speed){
            this.speed = speed;
        }
        
    }

    //Sets Speeds for IntakeRollers
    public enum IntakeRollers {
        IDLE(0),
        INTAKE(1),
        OUTTAKE(-1);

        public final double speed;

        IntakeRollers(double speed) {
            this.speed = speed;
        }

    }
}

package org.team498.lib.drivers;
import static org.team498.C2024.Ports.DrivetrainPorts.GYRO;

import java.util.function.Supplier;

import org.team498.C2024.Ports;
import org.team498.lib.util.RotationUtil;

import com.ctre.phoenix6.configs.Pigeon2Configuration;

public class Gyro extends com.ctre.phoenix6.hardware.Pigeon2 {

    /** @return yaw angle in degrees (CCW positive), ranging from -180 to 180 degrees */
    
    public double yaw() {
        return RotationUtil.toSignedDegrees(super.getYaw().getValueAsDouble());             
    }
    public Supplier<Double> getYawSupplier() {
        return super.getYaw().asSupplier();
    }
    
    // public double pitch() {
    //     return super.getPitch().getValueAsDouble();
    // }

    public void setSimAngle(double angle) {
    }

    private Gyro(int CANId, String canbus) {
        super(CANId, canbus);
        Pigeon2Configuration config = new Pigeon2Configuration();
        config.MountPose.MountPosePitch = 1.1374881267547607;
        config.MountPose.MountPoseRoll = -0.8307186365127563;
        this.getConfigurator().apply(config);
        this.optimizeBusUtilization(0.01);
        this.getYaw().setUpdateFrequency(20);
    }

    private static Gyro instance;

    public static Gyro getInstance() {
        if (instance == null) {
            instance = new Gyro(GYRO, Ports.Accessories.DriveBus);
        }
        return instance;
    }

}


/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * Add your docs here.
 */
public class Constants {
    public static final double kSensorUnits = 4096;
    

    //DRIVETRAIN CONSTANTS (TRACKING)
    public static double kDriveP = 0.033;
    public static double kDriveI = 0;
    public static double kDriveD = 0.16;
    public static double kDriveSpeed = -0.8;

    public static int kTimeoutMs = 30;
    public static int kPIDLoopIdx = 0;
    public static int kSlotIdx = 0;
    static final Gains kGains = new Gains(0.2, 0.0, 0.0, 0.2, 0, 1.0);

    //ARM CONSTANTS
    public static final int kArmMaster = 4;
    public static final int kArmSlave = 4;
    public static final int kMaxSensorVelocity = 4;
    public static double kArmP = 0;
    public static double kArmI = 0;
    public static double kArmD = 0;
    public static double kArmF = 0;
}


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
    public static int kSlotIdx = 0;
    static final Gains kGains = new Gains(0.2, 0.0, 0.0, 0.2, 0, 1.0);
    public static int kSwitchBack = 0;
    public static int kSwitchFront = 0;
    public static enum DriveState {
        MANUAL,
        AUTO
    }

    //ARM CONSTANTS
    public static final int kArmMaster = 31;
    public static final int kArmSlave = 32;

    public static final int kTow = 1;

    public static final int kMaxSensorVelocity = 4;
    public static final double kGearRatio = 16;
    public static double kArmP = 15;
    public static double kArmI = 0;
    public static double kArmD = 0;
    public static double kArmF = 10.23;
    public static final Gains kArmGains = new Gains(kArmP, kArmI, kArmD, kArmF, 0, 1);
    public static final int kAllowableError = 0;
    public static enum ArmState {
        INTAKING,
        HATCH,
        CARGO,
        NEUTRAL,
        BACKHATCH,
        BACKCARGO,
        BACKINTAKING,
        FREEHAND,
        TOW,
        TEST
    }

    //CLIMB CONSTANTS
    public static final int kClimbFrontLeft = 1;
    public static final int kClimbFrontRight = 4;
    public static final int kClimbBackLeft = 2;
    public static final int kClimbBackRight = 3;

    //public static double kPitchP = 0.1;
    public static double kPitchI = 0;
    public static double kPitchD = 0;
    public static double kPitchF = 0;

    //public static double kRollP = 0.07;
    public static double kRollI = 0;
    public static double kRollD = 0;
    public static double kRollF = 0;

    public static double kVoltageRamp = 0.254;

    public static final Gains sexyMode = new Gains(0.2, 0, 0.32, 0, 0.8);
    public static final Gains flexMode = new Gains(0.1, 0, 0, 0, 0.4);
    public static final Gains backwards = new Gains(0.2, 0, 0.32, 0, -0.8);
    public static final Gains stayMode = new Gains(0.2, 0, 0, 0, 0);
    public static final Gains threeManMode = new Gains(0.2, 0, 0.32, 0, 0.8);

    public static enum ClimbState {
        SEXYMODE,
        THREEMANCLIMB,
        REVERSE,
        STATIC
    }

    public static final int kTalonWinch = 30;
    public static final int kTalonWinchSlave = 31;

    //INTAKE CONSTANTS
    public static final int kSparkLeft = 2;
    public static final int kSparkRight = 0;

    public static final int kPuck = 6;
    public static final int kPuckOut = 3;

    public static final int kGrab = 0;
    public static final int kGrabOut = 1;

    public static final int kExtendo = 2;
    public static final int kExtendoOut = 7;

    //FORK CONSTANTS
    public static int kForkLFront = 8;
    public static int kForkLBack = 0;
    public static int kForkRFront = 0;
    public static int kForkRBack = 0;

    public static int kResetPos = 0;
    public static int kExpandPos = 0;
    public static int kReleasePos = 0;

    public static int kForkIn = 0;
    public static int kForkOut = 0;
}

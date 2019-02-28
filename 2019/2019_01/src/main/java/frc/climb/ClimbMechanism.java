package frc.climb;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.Gains;
import frc.robot.Constants.ClimbState;

public class ClimbMechanism{
    TalonSRX frontLeft; 
    TalonSRX backLeft; 
    TalonSRX frontRight;
    TalonSRX backRight;

    public AHRS navx = new AHRS(SPI.Port.kMXP);

    double error, deltaError, lastError;
    double errorR, deltaErrorR, lastErrorR;
    double integralError = 0;
    double integralErrorR = 0;

    ClimbState curClimbState;
    Gains currentGains;

    public void init() {
        //towOut = false;
        //tow = new DoubleSolenoid(Constants.kTow, Constants.kTowOut);
        frontLeft = new WPI_TalonSRX(Constants.kClimbFrontLeft);
        backLeft = new WPI_TalonSRX(Constants.kClimbBackLeft);
        frontRight = new WPI_TalonSRX(Constants.kClimbFrontRight);
        backRight = new WPI_TalonSRX(Constants.kClimbBackRight);

        frontLeft.setInverted(true);
        backLeft.setInverted(true);
        frontRight.setInverted(true);
        backRight.setInverted(true);

        frontLeft.clearStickyFaults();
        backLeft.clearStickyFaults();
        frontRight.clearStickyFaults();
        backLeft.clearStickyFaults();

        curClimbState = ClimbState.SEXYMODE;

        frontLeft.configOpenloopRamp(Constants.kVoltageRamp);
        frontLeft.configClosedloopRamp(Constants.kVoltageRamp);

        frontRight.configOpenloopRamp(Constants.kVoltageRamp);
        frontRight.configClosedloopRamp(Constants.kVoltageRamp);

        backLeft.configOpenloopRamp(Constants.kVoltageRamp);
        backLeft.configClosedloopRamp(Constants.kVoltageRamp);
        
        backRight.configOpenloopRamp(Constants.kVoltageRamp);
        backRight.configClosedloopRamp(Constants.kVoltageRamp);
    }

    public void manualClimb(int talon, double driveSpeed){
        //talons[talon].set(ControlMode.PercentOutput, driveSpeed);
        switch(talon){
            case 0: frontLeft.set(ControlMode.PercentOutput, driveSpeed);
            break;
            case 1: frontRight.set(ControlMode.PercentOutput, driveSpeed);
            break;
            case 2: backLeft.set(ControlMode.PercentOutput, driveSpeed);
            break;
            case 3: backRight.set(ControlMode.PercentOutput, driveSpeed);
            break;
        }
    }

    public void manualClimb(double driveSpeed){
        frontLeft.set(ControlMode.PercentOutput, -driveSpeed);
        frontRight.set(ControlMode.PercentOutput, -driveSpeed);
        backLeft.set(ControlMode.PercentOutput, -driveSpeed);
        backRight.set(ControlMode.PercentOutput, -driveSpeed);
    }

    public void setClimbState(ClimbState climbState){
        curClimbState = climbState;
    }

    public void climb(double driveSpeed){
        switch(curClimbState){
            case TWOBOTFAST:
            currentGains = Constants.flexMode;
            break;
            case SEXYMODE:
            currentGains = Constants.sexyMode;
            break;
            case TWOBOTSLOW:
            currentGains = Constants.stayMode;
        }
        error = navx.getPitch();
        errorR = navx.getRoll();
        SmartDashboard.putNumber("error", error);
        SmartDashboard.putNumber("errorR", errorR);
        deltaError = error-lastError;
        deltaErrorR = errorR - lastErrorR;

        SmartDashboard.putNumber("kP", currentGains.kP);
        SmartDashboard.putNumber("kD", currentGains.kD);

        double P = error*currentGains.kP;
        double D = currentGains.kD*deltaError;
        double I = currentGains.kI*integralError;

        double PR = errorR*currentGains.kP;
        double DR = currentGains.kD*deltaErrorR;
        double IR = currentGains.kI*integralErrorR;

        double gain = Math.abs(error)>0.5 ? P+I+D : 0;
        double gainR = Math.abs(errorR)>0.5 ? PR+IR+DR : 0;

        frontLeft.set(ControlMode.PercentOutput, driveSpeed-gain-gainR);
        backLeft.set(ControlMode.PercentOutput, driveSpeed-gain+gainR);
        frontRight.set(ControlMode.PercentOutput, driveSpeed+gain-gainR);
        backRight.set(ControlMode.PercentOutput, driveSpeed+gain+gainR);

        integralError = integralError + (error*0.2);  
        lastError = error;
        integralErrorR = integralErrorR + (errorR*0.2);
        lastErrorR = errorR;
    }
}
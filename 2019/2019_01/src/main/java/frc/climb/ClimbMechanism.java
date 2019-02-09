package frc.climb;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;

public class ClimbMechanism{
    WPI_TalonSRX frontLeft; 
    WPI_TalonSRX backLeft; 
    WPI_TalonSRX frontRight;
    WPI_TalonSRX backRight;
    AHRS navx = new AHRS(Port.kMXP);
    double error, deltaError, lastError;
    double errorR, deltaErrorR, lastErrorR;
    double integralError = 0;
    double integralErrorR = 0;

    public void init() {
        frontLeft = new WPI_TalonSRX(Constants.kClimbFrontLeft);
        backLeft = new WPI_TalonSRX(Constants.kClimbBackLeft);
        frontRight = new WPI_TalonSRX(Constants.kClimbFrontRight);
        backRight = new WPI_TalonSRX(Constants.kClimbBackRight);
        SmartDashboard.putNumber("kP", 0);
        SmartDashboard.putNumber("kI", 0);
        SmartDashboard.putNumber("kD", 0);
        SmartDashboard.putNumber("driveSpeed", 0);
        SmartDashboard.putNumber("kPR", 0);
        SmartDashboard.putNumber("kIR", 0);
        SmartDashboard.putNumber("kDR", 0);
        SmartDashboard.putNumber("voltageRamp", 0);
        Constants.kVoltageRamp = SmartDashboard.getNumber("voltageRamp", 0);
        frontLeft.configOpenloopRamp(Constants.kVoltageRamp);
        frontLeft.configClosedloopRamp(Constants.kVoltageRamp);
        frontRight.configOpenloopRamp(Constants.kVoltageRamp);
        frontRight.configClosedloopRamp(Constants.kVoltageRamp);
        backLeft.configOpenloopRamp(Constants.kVoltageRamp);
        backLeft.configClosedloopRamp(Constants.kVoltageRamp);
        backRight.configOpenloopRamp(Constants.kVoltageRamp);
        backRight.configClosedloopRamp(Constants.kVoltageRamp);
    }

    void climb(double driveSpeed){
        error = navx.getPitch();
        errorR = navx.getRoll();
        SmartDashboard.putNumber("error", error);
        SmartDashboard.putNumber("errorR", errorR);
        deltaError = error-lastError;
        deltaErrorR = errorR - lastErrorR;

        double P = error*Constants.kPitchP;
        double D = Constants.kPitchD*deltaError;
        double I = Constants.kPitchI*integralError;

        double PR = errorR*Constants.kRollP;
        double DR = Constants.kRollD*deltaErrorR;
        double IR = Constants.kRollI*integralErrorR;

        double gain = Math.abs(error)>0.1 ? P+I+D : 0;
        double gainR = Math.abs(errorR)>0.1 ? PR+IR+DR : 0;

        frontLeft.set(driveSpeed-gain-gainR);
        backLeft.set(driveSpeed-gain+gainR);
        frontRight.set(driveSpeed+gain-gainR);
        backRight.set(driveSpeed+gain+gainR);

        integralError = integralError + (error*0.2);  
        lastError = error;
        integralErrorR = integralErrorR + (errorR*0.2);
        lastErrorR = errorR;
    }
}
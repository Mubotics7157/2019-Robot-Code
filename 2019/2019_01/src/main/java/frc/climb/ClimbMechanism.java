package frc.climb;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;

public class ClimbMechanism{
    WPI_TalonSRX mainLeft; 
    WPI_TalonSRX slaveLeft; 
    WPI_TalonSRX mainRight;
    WPI_TalonSRX slaveRight;
    AHRS navx = new AHRS(Port.kMXP);
    double error, deltaError, lastError;
    double errorR, deltaErrorR, lastErrorR;
    double integralError = 0;
    double integralErrorR = 0;
    double voltageRamp;

    public void init() {
        mainLeft = new WPI_TalonSRX(0); //TBD
        slaveLeft = new WPI_TalonSRX(1); //TBD
        mainRight = new WPI_TalonSRX(2); //TBD
        slaveRight = new WPI_TalonSRX(3); //TBD
        //slaveLeft.follow(mainLeft);
        //slaveRight.follow(mainRight);
        SmartDashboard.putNumber("kP", 0);
        SmartDashboard.putNumber("kI", 0);
        SmartDashboard.putNumber("kD", 0);
        SmartDashboard.putNumber("driveSpeed", 0);
        SmartDashboard.putNumber("kPR", 0);
        SmartDashboard.putNumber("kIR", 0);
        SmartDashboard.putNumber("kDR", 0);
        SmartDashboard.putNumber("voltageRamp", 0);
        voltageRamp = SmartDashboard.getNumber("voltageRamp", 0);
        mainLeft.configOpenloopRamp(voltageRamp);
        mainLeft.configClosedloopRamp(voltageRamp);
        mainRight.configOpenloopRamp(voltageRamp);
        mainRight.configClosedloopRamp(voltageRamp);
        slaveLeft.configOpenloopRamp(voltageRamp);
        slaveLeft.configClosedloopRamp(voltageRamp);
        slaveRight.configOpenloopRamp(voltageRamp);
        slaveRight.configClosedloopRamp(voltageRamp);
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

        mainLeft.set(driveSpeed-gain-gainR);
        slaveLeft.set(driveSpeed-gain+gainR);
        mainRight.set(driveSpeed+gain-gainR);
        slaveRight.set(driveSpeed+gain+gainR);

        integralError = integralError + (error*0.2);  
        lastError = error;
        integralErrorR = integralErrorR + (errorR*0.2);
        lastErrorR = errorR;
    }
}
package frc.climb;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ClimbMechanism{
    WPI_TalonSRX mainLeft; 
    WPI_TalonSRX slaveLeft; 
    WPI_TalonSRX mainRight;
    WPI_TalonSRX slaveRight;
    AHRS navx = new AHRS(Port.kMXP);
    double kP, kPR;
    double kI, kIR;
    double kD, kDR;
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
        kP = SmartDashboard.getNumber("kP", 0);
        kI = SmartDashboard.getNumber("kI", 0);
        kD = SmartDashboard.getNumber("kD", 0);
        kPR = SmartDashboard.getNumber("kPR", 0);
        kIR = SmartDashboard.getNumber("kIR", 0);
        kDR = SmartDashboard.getNumber("kDR", 0);
        error = navx.getPitch();
        errorR = navx.getRoll();
        SmartDashboard.putNumber("error", error);
        SmartDashboard.putNumber("errorR", errorR);
        deltaError = error-lastError;
        deltaErrorR = errorR - lastErrorR;

        double P = error*kP;
        double D = kD*deltaError;
        double I = kI*integralError;

        double PR = errorR*kPR;
        double DR = kD*deltaErrorR;
        double IR = kIR*integralErrorR;

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
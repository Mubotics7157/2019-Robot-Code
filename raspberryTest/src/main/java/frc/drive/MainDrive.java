/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.drive;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.autopilot.TrackingHandler;
import frc.robot.OI;

/**
 * Add your docs here.
 */
public class MainDrive {
    public WPI_TalonSRX talon = new WPI_TalonSRX(2);
    public WPI_VictorSPX victor = new WPI_VictorSPX(3);
    public WPI_VictorSPX victor2 = new WPI_VictorSPX(4);
    public PWMVictorSPX PWMVictor = new PWMVictorSPX(9);
    public DifferentialDrive drive = new DifferentialDrive(talon, victor);
    public static TrackingHandler tracking = new TrackingHandler();
    public AHRS navx = new AHRS(Port.kMXP);
    public OI oi = new OI();
    double integralError = 0.0f;

    private double kP, kI, kD, driveSpeed, deltaError, lastError, integralWindup;

    public void initDrive() {
        SmartDashboard.putNumber("kP", 0.1);
        SmartDashboard.putNumber("kI", 0.1);
        SmartDashboard.putNumber("kD", 0.1);
        SmartDashboard.putNumber("driveSpeed", 0.1);
        SmartDashboard.putNumber("integralWindup", 0.1);
        tracking.initTracking();
    }

    public void periodicDrive() {
        PWMVictor.set(talon.getMotorOutputPercent());
        tracking.updateTracking();
    }

    public void driveAutoPilot() {
        kP = SmartDashboard.getNumber("kP", 1);
        kI = SmartDashboard.getNumber("kI", 1);
        kD = SmartDashboard.getNumber("kD", 1);
        driveSpeed = SmartDashboard.getNumber("driveSpeed", 1);
        integralWindup = SmartDashboard.getNumber("integralWindup", 1);
        
        double setpoint = tracking.targetYaw();
        
        double error = setpoint - navx.getYaw();
        deltaError = error - lastError;

        SmartDashboard.putNumber("Error", error);

        if(tracking.getCargoDetected() && Math.abs(error)<integralWindup){
            integralError = integralError + error;   
        }else{
            integralError = 0;
        }

        double P = error*kP;
        double D = kD*deltaError;
        double I = kI*integralError;
        double gain = Math.abs(error)>1 ? P+D+I : 0;

        talon.set(driveSpeed + gain);
        victor.set(-driveSpeed + gain);
        
        lastError = error;
    }

    public void tankDrive() {
        talon.set(oi.controllerR.getRawAxis(1));
        victor.set(-oi.controllerL.getRawAxis(1));
    }
}

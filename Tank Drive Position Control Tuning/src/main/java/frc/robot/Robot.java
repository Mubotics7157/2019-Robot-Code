/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

/**
 * This is a demo program for 7157 to tune drivetrain PID
 */
public class Robot extends TimedRobot {
  private static final int FLdeviceID = 1;
  private static final int FRdeviceID = 2;
  private static final int BLdeviceID = 3;
  private static final int BRdeviceID = 4;

  private static final double Gearing = 10.71;
  
  private CANPIDController leftPid;
  private CANPIDController rightPid;
  private CANSparkMax frontLeft, frontRight, backLeft, backRight;
  private CANEncoder leftEncoder;
  private CANEncoder rightEncoder;
  public double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput;

  private DifferentialDrive drive;

  @Override
  public void robotInit() {
    // initialize motor
    frontLeft = new CANSparkMax(FLdeviceID, MotorType.kBrushless);
    frontRight = new CANSparkMax(FRdeviceID, MotorType.kBrushless);
    backLeft = new CANSparkMax(BLdeviceID, MotorType.kBrushless);
    backRight = new CANSparkMax(BRdeviceID, MotorType.kBrushless);

    backLeft.follow(frontLeft);
    backRight.follow(frontRight);

    /**CANPIDController object
     * is constructed by calling the getPIDController() method on an existing
     * CANSparkMax
     */
    leftPid = frontLeft.getPIDController();
    rightPid = frontRight.getPIDController();

    leftEncoder = frontLeft.getEncoder();
    rightEncoder = frontRight.getEncoder();

    // PID coefficients
    kP = 0.1; 
    kI = 1e-4;
    kD = 1; 
    kIz = 0;
    kFF = 0; 
    kMaxOutput = 1; 
    kMinOutput = -1;

    // set PID coefficients
    leftPid.setP(kP);
    leftPid.setI(kI);
    leftPid.setD(kD);
    leftPid.setIZone(kIz);
    leftPid.setFF(kFF);
    leftPid.setOutputRange(kMinOutput, kMaxOutput);

    rightPid.setP(kP);
    rightPid.setI(kI);
    rightPid.setD(kD);
    rightPid.setIZone(kIz);
    rightPid.setFF(kFF);
    rightPid.setOutputRange(kMinOutput, kMaxOutput);

    // display PID coefficients on SmartDashboard
    SmartDashboard.putNumber("P Gain", kP);
    SmartDashboard.putNumber("I Gain", kI);
    SmartDashboard.putNumber("D Gain", kD);
    SmartDashboard.putNumber("I Zone", kIz);
    SmartDashboard.putNumber("Feed Forward", kFF);
    SmartDashboard.putNumber("Max Output", kMaxOutput);
    SmartDashboard.putNumber("Min Output", kMinOutput);
    SmartDashboard.putNumber("Set Rotations", 0);
  }

  @Override
  public void teleopPeriodic() {
    // read PID coefficients from SmartDashboard
    double p = SmartDashboard.getNumber("P Gain", 0);
    double i = SmartDashboard.getNumber("I Gain", 0);
    double d = SmartDashboard.getNumber("D Gain", 0);
    double iz = SmartDashboard.getNumber("I Zone", 0);
    double ff = SmartDashboard.getNumber("Feed Forward", 0);
    double max = SmartDashboard.getNumber("Max Output", 0);
    double min = SmartDashboard.getNumber("Min Output", 0);
    double rotations = SmartDashboard.getNumber("Set Rotations", 0);

    // if PID coefficients on SmartDashboard have changed, write new values to controller
    if((p != kP)) {
      leftPid.setP(p);
      rightPid.setP(p);
      kP = p; 
    }
    if((i != kI)) {
      leftPid.setI(i);
      rightPid.setI(i);
      kI = i;
    }
    if((d != kD)) {
      leftPid.setD(d);
      rightPid.setD(d);
      kD = d;
    }
    if((iz != kIz)) {
      leftPid.setIZone(iz);
      rightPid.setIZone(iz);
      kIz = iz;}
    if((ff != kFF)) {
      leftPid.setFF(ff);
      rightPid.setFF(ff);
      kFF = ff;
    }
    if((max != kMaxOutput) || (min != kMinOutput)) { 
      leftPid.setOutputRange(min, max); 
      rightPid.setOutputRange(min, max); 
      kMinOutput = min; kMaxOutput = max; 
    }

    /**
     * PIDController objects are commanded to a set point using the 
     * SetReference() method.
     * 
     * The first parameter is the value of the set point, whose units vary
     * depending on the control type set in the second parameter.
     * 
     * The second parameter is the control type can be set to one of four 
     * parameters:
     *  com.revrobotics.ControlType.kDutyCycle
     *  com.revrobotics.ControlType.kPosition
     *  com.revrobotics.ControlType.kVelocity
     *  com.revrobotics.ControlType.kVoltage
     */
    leftPid.setReference(rotations/Gearing, ControlType.kPosition);
    rightPid.setReference(rotations/Gearing, ControlType.kPosition);
    
    SmartDashboard.putNumber("SetPoint", rotations);
    SmartDashboard.putNumber("ProcessVariable", leftEncoder.getPosition());
    SmartDashboard.putNumber("ProcessVariableR", rightEncoder.getPosition());
  }
}

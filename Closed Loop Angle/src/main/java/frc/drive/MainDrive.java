/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.drive;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.SPI;
import frc.robot.Constants;

/**
 * Handles main driving functions
 */
public class MainDrive {
    private CANSparkMax _frontLeft = new CANSparkMax(Constants.kFrontLeftMotorId, MotorType.kBrushless),
                        _frontRight= new CANSparkMax(Constants.kFrontRightMotorId, MotorType.kBrushless),
                        _backRight= new CANSparkMax(Constants.kBackRightMotorId, MotorType.kBrushless),
                        _backLeft = new CANSparkMax(Constants.kBackLeftMotorId, MotorType.kBrushless);

    private CANPIDController leftPID = new CANPIDController(_frontLeft);
    private CANPIDController rightPID = new CANPIDController(_frontRight);

    private CANEncoder leftEncoder = new CANEncoder(_frontLeft);
    private CANEncoder rightEncoder = new CANEncoder(_frontRight);

    private int timeoutMs = 10;
    private AHRS navX = new AHRS(SPI.Port.kMXP);

    double nominalMin = -1;
    double nominalMax = 1;

    public MainDrive() {
        _backRight.follow(_frontRight);
        _backLeft.follow(_frontLeft);
        _frontRight.setCANTimeout(timeoutMs);
        _frontLeft.setCANTimeout(timeoutMs);

        leftPID.setOutputRange(nominalMin, nominalMax);
        rightPID.setOutputRange(nominalMin, nominalMax);

        leftPID.setP(Constants.driveP);
        leftPID.setI(Constants.driveI);
        leftPID.setD(Constants.driveD);
        leftPID.setFF(Constants.driveFF);
        leftPID.setIZone(Constants.driveIZ);

        rightPID.setP(Constants.driveP);
        rightPID.setI(Constants.driveI);
        rightPID.setD(Constants.driveD);
        rightPID.setFF(Constants.driveFF);
        rightPID.setIZone(Constants.driveIZ);
    }

    public double getLeftRevs() {
      return leftEncoder.getPosition();
    }

    public double getRightRevs() {
      return rightEncoder.getPosition();
    }

    public void setPID(double left, double right) {
      leftPID.setReference(left, ControlType.kPosition);
      rightPID.setReference(right, ControlType.kPosition);
    }

    public void stop() {
      _frontLeft.stopMotor();
      _frontRight.stopMotor();
    }

    public void setDirect(double left, double right) {
      _frontLeft.set(left);
      _frontRight.set(right);
    }

    public void tankDrive(double leftSpeed, double rightSpeed, boolean squaredInputs) {
        leftSpeed = applyDeadband(leftSpeed, 0.02);
        
        rightSpeed = applyDeadband(rightSpeed, 0.02);

        // Square the inputs (while preserving the sign) to increase fine control
        // while permitting full power.
        if (squaredInputs) {
          leftSpeed = Math.copySign(leftSpeed * leftSpeed, leftSpeed);
          rightSpeed = Math.copySign(rightSpeed * rightSpeed, rightSpeed);
        }

        _frontLeft.set(leftSpeed);
        _frontRight.set(-rightSpeed);
    }

    protected double applyDeadband(double value, double deadband) {
        if (Math.abs(value) > deadband) {
          if (value > 0.0) {
            return (value - deadband) / (1.0 - deadband);
          } else {
            return (value + deadband) / (1.0 - deadband);
          }
        } else {
          return 0.0;
        }
    }

    public double getNavXAngle() {
      return navX.getYaw();
    }

    public double getNavXAngleRadians() {
      return Math.toRadians(navX.getAngle());
    }

    public void resetAngle() {
      navX.zeroYaw();
      try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void rotateToAngle(double angle) {
      
    }
}

/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.autopilot.TrackingHandler;
import frc.drive.MainDrive;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  public static OI oi = new OI();
  public static MainDrive drive = new MainDrive();
  public Timer timer = new Timer();
  boolean firstTime = false;

  private enum DriveMode {
    Unassisted,
    Assisted
  }

  private DriveMode currDriveMode = DriveMode.Unassisted;


  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
    drive.initDrive();
    timer.start();

    //victor2.follow(victor);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    drive.periodicDrive();
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    double deadband = 0.05;
    if(oi.controllerR.getRawButtonPressed(3)){
      drive.toggleTarget();
    }
    if(oi.controllerL.getRawAxis(1)>deadband || oi.controllerR.getRawAxis(1)>deadband){
      if(oi.controllerL.getRawButton(4) == false){
        System.out.println("Manual Override");
        currDriveMode = DriveMode.Unassisted;
      }
    }
    if (oi.controllerL.getRawButtonPressed(2)) currDriveMode = DriveMode.Unassisted;
    if(oi.controllerL.getRawButton(1) /*&& (timer.get()/.5)%1 == 0*/){

      //drive.acquireTarget();
    }
    if(oi.controllerL.getRawButtonReleased(4)){
      firstTime = true;
    }
    if (oi.controllerL.getRawButton(4)){
      if(drive.tracking.getCargoDetected() || drive.tracking.getTapeDetected()){
        if(firstTime){
          currDriveMode = DriveMode.Assisted;
          drive.navx.zeroYaw();
          drive.acquireTarget();
          firstTime = false;
        }
      }
    }
    

    switch (currDriveMode) {
      case Assisted:
      SmartDashboard.putString("driveMode", "Assisted");
      drive.driveAutoPilot();
      /*if(timer.get()%1==0){
        drive.acquireTarget();
        System.out.println("new setpoint");
      }*/
      break;
      case Unassisted:
      SmartDashboard.putString("driveMode", "Unassisted");
      drive.tankDrive();
      break;
    }
  }

  @Override
  public void teleopInit() {
    
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}

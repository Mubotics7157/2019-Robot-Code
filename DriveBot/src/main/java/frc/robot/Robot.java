/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  public Pixy2Handler version = new Pixy2Handler();
  public CustomDrive drive = new CustomDrive();

  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
    version.init();
    drive.init();
    
  }

  @Override
  public void robotPeriodic() {
    drive.drivePeriodic();
  }

  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    System.out.println("Auto selected: " + m_autoSelected);
  }

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

  @Override
  public void teleopPeriodic() {
    drive.defaultDrive();
    //version.sendRequest(version.CHECKSUM_GETMAINFEATURES);
    
    if(drive.controller1.getRawButtonPressed(1)){
      System.out.println("pressed");
      version.printLocalCache();
    }

    if(drive.controller1.getRawButtonPressed(6)){
      version.toggleLamp();
    }

    if(drive.controller2.getRawButton(1)){
      drive.followLine();
    }
    if(drive.controller2.getRawButtonPressed(1)){
      drive.navx.zeroYaw();
    }
  }

  @Override
  public void teleopInit(){
    
  }

  @Override
  public void testPeriodic() {
  }
}

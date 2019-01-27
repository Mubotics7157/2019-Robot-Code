/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import frc.drive.MainDrive;

public class Robot extends TimedRobot {
  public static MainDrive drive = new MainDrive();
  public static OI oi = new OI();

  private enum DrivingMode {
    Unassisted,
    AssistedAlign,
    AssistedLine
  }

  private DrivingMode currentDrivingState = DrivingMode.Unassisted;

  @Override
  public void robotInit() {

  }

  @Override
  public void robotPeriodic() {
  }

  @Override
  public void autonomousInit() {
    
  }

  @Override
  public void autonomousPeriodic() {

  }

  @Override
  public void teleopPeriodic() {
    //ADD joystick input to switch the currentDrivingState
    switch (currentDrivingState) {
      case Unassisted:
      //Tank Drive normally
      break;
      case AssistedAlign:
      //Add later using pixy
      break;
      case AssistedLine:
      
      break;
    }
  }

  @Override
  public void testPeriodic() {
  }
}

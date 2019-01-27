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
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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

  private NetworkTable chickenVision;
  private NetworkTableInstance instance;

  private double kP = 0.0045;
  private double kI = 0;
  private double kD = 0;
  private double lastError = 0;
  private double deltaError = 0;

  public double turnSpeed = 0f;
  public double driveSpeed = 0f;

  public WPI_TalonSRX talon = new WPI_TalonSRX(2);
  public WPI_VictorSPX victor = new WPI_VictorSPX(3);
  public WPI_VictorSPX victor2 = new WPI_VictorSPX(4);
  public PWMVictorSPX PWMVictor = new PWMVictorSPX(9);
  public Joystick controller1 = new Joystick(0);
  public DifferentialDrive drive = new DifferentialDrive(talon, victor);


  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    SmartDashboard.putBoolean("Tape", false);
    instance = NetworkTableInstance.getDefault();
    chickenVision = instance.getTable("ChickenVision");

    SmartDashboard.putNumber("kP", 0.1);
    SmartDashboard.putNumber("kD", 0.1);
    SmartDashboard.putNumber("driveSpeed", 0.1);
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
    
    PWMVictor.set(talon.getMotorOutputPercent());

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
    kP = SmartDashboard.getNumber("kP", 1);
    kD = SmartDashboard.getNumber("kD", 1);
    driveSpeed = SmartDashboard.getNumber("driveSpeed", 1);

    NetworkTableEntry cargoDetected = chickenVision.getEntry("cargoDetected");
    NetworkTableEntry cargoYaw = chickenVision.getEntry("cargoYaw");
    
    double error = cargoYaw.getDouble(0);
    deltaError = error - lastError;
    double P = error*kP;
    double D = kD*deltaError;
    double gain = Math.abs(error)>1 ? P+D : 0;

    if(controller1.getRawButton(1)){
      System.out.println(cargoYaw.getDouble(0));
      
      if(cargoDetected.getBoolean(false)){
        talon.set(driveSpeed + gain);
        victor.set(-driveSpeed + gain);
      }
    }
    lastError = error;

  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}

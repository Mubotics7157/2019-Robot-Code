/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.climb.ClimbMechanism;
import frc.climb.Winch;
import frc.drive.CustomDrive;
import frc.robot.Constants.ArmState;
import frc.robot.Constants.ClimbState;
import frc.robot.Constants.DriveState;
import frc.arm.Arm;
import frc.arm.CameraManager;
import frc.arm.Intake;
import frc.arm.TowArm;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  public OI oi = new OI();
  public TowArm towArm = new TowArm();
  public CustomDrive customDrive = new CustomDrive();
  public ClimbMechanism climb = new ClimbMechanism();
  public Winch winch = new Winch();

  private ClimbRoutine climbRout = new ClimbRoutine();
  //public Arm arm = new Arm();
  //public Intake intake = new Intake();
  //public Forks forks = new Forks();
  //public CameraManager camManager = new CameraManager();
  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
    //arm.init();
    customDrive.init();
    //towArm.init();
    //customDrive.initTracking();
    climb.init();
    //intake.init();
    //forks.init();
    //camManager.init();
    winch.init();
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
    //arm.periodic();
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
    climbRout.retractTow();
    m_autoSelected = m_chooser.getSelected();
    // autoSelected = SmartDashboard.getString("Auto Selector",
    // defaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    finalControlScheme();
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
    SmartDashboard.putNumber("Pitch", climb.navx.getPitch());
    SmartDashboard.putBoolean("isConnected", climb.navx.isConnected());
    SmartDashboard.putBoolean("isCalibrating", climb.navx.isCalibrating());
    SmartDashboard.putNumber("Roll", climb.navx.getRoll());
  
    finalControlScheme();
    //winch.printLimit();
    //arm.periodic();
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
  public void teleopInit(){
    climbRout.retractTow();
  }

  
  public void finalControlScheme(){
    switch(customDrive.curDriveState){
      case AUTO:
      customDrive.driveAutoPilot();
      
      case MANUAL:
      if(oi.axis(2)<0.2 && !oi.bDown(3, 2)){
        customDrive.drive(oi.axis(3, 1), -oi.axis(2, 1));
      }
    }

    //failsafes
    //winch.setWinch(0);
    //towArm.setSpark(0);

    

    if (oi.bPressed(0, 6)) {
      climbRout.extendTow();
    }

    if (oi.bDown(0, 5)) {
      climbRout.runTow(-0.9);
    }else{
      climbRout.runTow(0);
    }

    if (oi.bDown(0, 2)) {
      //climbRout.firstLegs();
    }

    if (oi.axis(0, 3) == 1) {
      climbRout.retractTow();
    }

    if (oi.bDown(0, 8)) {
      //climbRout.secondLegs();
    }

    if (oi.getPOV() == 0 && !oi.bDown(0, 9) && oi.axis(0, 2) == 0) {
      //climb.climb();
    }

    if(oi.bDown(0, 8)){
      climb.manualClimb(0, -0.8);
      climb.manualClimb(1, -0.8);
    }

    if(oi.bDown(0, 2)){
      climb.manualClimb(2, -0.8);
      climb.manualClimb(3, -0.8);
    }


    if(oi.axis(0, 2) == 1){
      //climb.setClimbState(ClimbState.SEXYMODE);
      climb.climb(ClimbState.SEXYMODE);
      System.out.println("climbing");
    }
    if(oi.bDown(0, 1)){
      //climb.setClimbState(ClimbState.REVERSE);
      climb.climb(ClimbState.REVERSE);
      System.out.println("reversing");
    }
    if(oi.bDown(0, 3)){
      //climb.setClimbState(ClimbState.STATIC);
      climb.climb(ClimbState.STATIC);
      System.out.println("balancing");
    }
    if(oi.bDown(0, 4)){
      //climb.setClimbState(ClimbState.THREEMANCLIMB);
      climb.climb(ClimbState.THREEMANCLIMB);
      System.out.println("three man");
      //climbRout.lowerForks();
    }
    /*if (oi.bDown(0, 10)) {
      if (oi.getPOV() == 0) {
        climb.manualClimb(0, -1);
      }

      if (oi.getPOV() == 90) {
        climb.manualClimb(1, -1);        
      }

      if (oi.getPOV() == 180) {
        climb.manualClimb(2, -1);        
      }

      if (oi.getPOV() == 270) {
        climb.manualClimb(3, -1);        
      }
    }else if(oi.axis(0, 2) == 1){
      if (oi.getPOV() == 0) {
        climb.manualClimb(0, 1);
      }

      if (oi.getPOV() == 90) {
        climb.manualClimb(1, 1);        
      }

      if (oi.getPOV() == 180) {
        climb.manualClimb(2, 1);        
      }

      if (oi.getPOV() == 270) {
        climb.manualClimb(3, 1);        
      }
    }
    else if (oi.getPOV() == 180) {
        //climb.manualClimb(0, -1);
        //climb.manualClimb(1, -1);
        //climb.manualClimb(2, -1);
        //climb.manualClimb(3, -1);
    }*/

    //gamepad controls
    if(oi.axis(1, 2) > 0.5){
      if(oi.bDown(1, 1)){
        climb.manualClimb(0, oi.axis(1, 1));//front left
      }
      if(oi.bDown(1, 2)){
        climb.manualClimb(1, oi.axis(1, 1));//front right
      }
      if(oi.bDown(1, 3)){
        climb.manualClimb(2, oi.axis(1, 1));//back left
      }
      if(oi.bDown(1, 4)){
        climb.manualClimb(3, oi.axis(1, 1));//back right
      }
    }else if(oi.axis(0, 2) != 1 && !oi.bDown(0, 1) && !oi.bDown(0, 2) && !oi.bDown(0, 8) && !oi.bDown(0, 3) && !oi.bDown(0, 4)){
      climb.manualClimb(0, 0);
      climb.manualClimb(1, 0);
      climb.manualClimb(2, 0);
      climb.manualClimb(3, 0);
    }
    if(oi.bPressed(1, 8)){
      climb.jankRecalibrate();
      System.out.println("recalibrated");
    }

    if(oi.axis(1, 3) > 0.5){
      winch.setWinch(oi.axis(1, 5));
    }else{
      winch.setWinch(0);
    }

  }

  public void processInputsFinalDrive(){
    switch(customDrive.curDriveState){
      case AUTO:
      customDrive.driveAutoPilot();
      case MANUAL:
      customDrive.drive(-oi.axis(1), oi.axis(5));
    }  
  }

  public class ClimbRoutine {
    public void lowerForks() {
      winch.setWinch(1);
    }

    public void raiseForks() {
      winch.setWinch(-1);
    }

    public void stopForks(){
      winch.setWinch(0);
    }

    public void climb() {
      //climb.climb();
    }

    public void extendTow() {
      winch.extendTow();
    }

    public void runTow(double speed) {
      winch.setTowWheels(speed);
    }

    public void firstLegs() {
      climb.manualClimb(0, -1);
      climb.manualClimb(1, -1);
    }

    public void retractTow() {
      winch.retractTow();
    }

    public void secondLegs() {
      climb.manualClimb(2, -1);
      climb.manualClimb(3, -1);
    }
  }
}

/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.climb.ClimbMechanism;
import frc.climb.Forks;
import frc.drive.CustomDrive;
import frc.robot.Constants.ArmState;
import frc.arm.Arm;
import frc.arm.Intake;

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
  public CustomDrive customDrive = new CustomDrive();
  public ClimbMechanism climb = new ClimbMechanism();
  public Arm arm = new Arm();
  public Intake intake = new Intake();
  public Forks forks = new Forks();
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
    //customDrive.init();
    //customDrive.initTracking();
    climb.init();
    //intake.init();
    //forks.init();
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
    // autoSelected = SmartDashboard.getString("Auto Selector",
    // defaultAuto);
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
    processInputsClimb();
    SmartDashboard.putNumber("Pitch", climb.navx.getPitch());
    SmartDashboard.putBoolean("isConnected", climb.navx.isConnected());
    SmartDashboard.putBoolean("isCalibrating", climb.navx.isCalibrating());
    SmartDashboard.putNumber("Roll", climb.navx.getRoll());
    
    //arm.moveToState(ArmState.FREEHAND);
    //arm.periodic();
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
  public void teleopInit(){

  }
  public void processInputsTest2(){
    arm.setFreehandInput(oi.axis(1));
  }
  public void processInputsTest(){
    if(oi.axis(2)>0.7){
      climb.manualClimb(oi.axis(1));
    }else{
      climb.manualClimb(0);
    }
  }
  public void processInputsArcade(){
    if(oi.bPressed(10)){
      arm.zeroEncoder();
    }

    if(oi.axis(2)>0.7){
      if(oi.bPressed(2)){
        arm.moveToState(ArmState.INTAKING);
      }
      if(oi.bPressed(3)){
        arm.moveToState(ArmState.HATCH);
      }
      if(oi.bPressed(8)){
        arm.moveToState(ArmState.CARGO);
      }
      if(oi.bPressed(7)){
        arm.moveToState(ArmState.NEUTRAL);
      }
    }

    if(oi.axis(3)>0.7){
      if(oi.bPressed(1)){
        arm.moveToState(ArmState.BACKINTAKING);
      }
      if(oi.bPressed(4)){
        arm.moveToState(ArmState.BACKHATCH);
      }
      if(oi.bPressed(6)){
        arm.moveToState(ArmState.BACKCARGO);
      }
      if(oi.bPressed(5)){
        arm.moveToState(ArmState.NEUTRAL);
      }
    }

  }
  public void processInputsClimb(){
    if(oi.axis(3)>0.7){
      if(oi.bDown(1)){
        climb.manualClimb(0, oi.axis(1)); //frontLeft
      }
      if(oi.bDown(2)){
        climb.manualClimb(1, oi.axis(1)); //backLeft
      }
      if(oi.bDown(3)){
        climb.manualClimb(2, oi.axis(1)); //frontRight
      }
      if(oi.bDown(4)){
        climb.manualClimb(3, oi.axis(1)); //backRight
      }
    }
    if(oi.axis(2)>0.7){
      climb.manualClimb(oi.axis(1));
    }
    if(oi.axis(2)<0.7 && oi.axis(3)<0.7){
      climb.manualClimb(0);
    }
    if(oi.bDown(5) && oi.bDown(6)){
      climb.climb(0.6);
    }
    //climb.manualClimb(oi.axis(1));
  }

  public void processInputsGamepad(){
    /*switch(customDrive.curDriveState){
      case MANUAL:
      customDrive.drive(oi.axis(1,1), oi.axis(2,1));
      case AUTO:
      customDrive.driveAutoPilot();
    }*/

    //forks.setServo(oi.axis(5));
   
    if(oi.bPressed(6)){
      arm.zeroEncoder();
    }
    if(Math.abs(oi.axis(1))>0.3){
      arm.moveToState(ArmState.FREEHAND);
    }
    if(oi.axis(2)>0.7){
      if(oi.bPressed(1)){
        arm.moveToState(ArmState.INTAKING);
      }
      if(oi.bPressed(2)){
        arm.moveToState(ArmState.HATCH);
      }
      if(oi.bPressed(3)){
        arm.moveToState(ArmState.CARGO);
      }
      if(oi.bPressed(4)){
        arm.moveToState(ArmState.NEUTRAL);
      }
    }
    if(oi.axis(3)>0.7){
      if(oi.bPressed(1)){
        arm.moveToState(ArmState.BACKINTAKING);
      }
      if(oi.bPressed(2)){
        arm.moveToState(ArmState.BACKHATCH);//-30
      }
      if(oi.bPressed(3)){
        arm.moveToState(ArmState.BACKCARGO);//
      }
      if(oi.bPressed(4)){
        arm.moveToState(ArmState.NEUTRAL);
      }
      if(oi.bPressed(5)){
        arm.moveToState(ArmState.FREEHAND);
      }
    }
    //intake.intake(oi.controller1.getRawAxis(1));
    arm.setFreehandInput(oi.axis(1));

    /*if(oi.bDown(1,1)&&oi.bDown(1,2)&&oi.bDown(1,3)&&oi.bDown(1,4)&&oi.bDown(1,5)&&oi.bDown(1,6)&&oi.bDown(1,7)&&oi.bDown(1,8)){
      forks.eatDinner();
    }*/
  }
}

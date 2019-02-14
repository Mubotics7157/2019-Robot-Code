/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.arm;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import frc.robot.Constants;

/**
 * Add your docs here.
 */
public class Intake {
    Spark sparkLeft;
    Spark sparkRight;
    DoubleSolenoid extendo;
    DoubleSolenoid grab;
    DoubleSolenoid puck;
    boolean intakeOut;
    boolean intakeClosed;
    boolean puckOut;

    public void init(){
        intakeOut = false;
        intakeClosed = false;
        puckOut = false;
        //sparkLeft = new Spark(Constants.kSparkLeft);
        //sparkRight = new Spark(Constants.kSparkRight);
        //extendo = new DoubleSolenoid(Constants.kExtendo, Constants.kExtendoOut);
        grab = new DoubleSolenoid(Constants.kGrab, Constants.kGrabOut);
        //puck = new DoubleSolenoid(Constants.kPuck, Constants.kPuckOut);
    }
    public void togglePuck(){
        if(puckOut==false){
            puck.set(Value.kForward);
            puckOut = true;
        }else{
            puck.set(Value.kReverse);
            puckOut = false;
        }
    }
    public void toggleIntake(){
        if(intakeClosed==false){
            grab.set(Value.kForward);
            intakeClosed = true;
        }else{
            grab.set(Value.kReverse);
            intakeClosed = false;
        }
    }
    public void toggleExtend(){
        if(intakeOut==false){
            extendo.set(Value.kForward);
            intakeOut = true;
        }else{
            extendo.set(Value.kReverse);
            intakeOut = false;
        }
    }
    public void intake(double speed){
        sparkLeft.set(speed);
        sparkRight.set(-speed);
    }
}

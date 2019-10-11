/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.climb;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import frc.robot.Constants;

/**
 * Add your docs here.
 */
public class Winch {

    //TalonSRX talon;
    //DigitalInput limitSwitchFar;
    //DigitalInput limitSwitchNear;
    TalonSRX winch;
    TalonSRX winchSlave;
    DoubleSolenoid towArm;
    Spark towWheels;

    public void init(){
        winch = new TalonSRX(31);
        winchSlave = new TalonSRX(32);
        //limitSwitchFar = new DigitalInput(0);
        winch.configOpenloopRamp(0.2);
        winchSlave.configOpenloopRamp(0.2);
        towArm = new DoubleSolenoid(5, 4);
        towWheels = new Spark(0);
        //limitSwitchNear = new DigitalInput(1);
        winchSlave.follow(winch);
        //towArm.set(Value.kForward);
    }

    public int getLimitStatus(){
        /*if(!limitSwitchFar.get()){
            return 1;
        }//else if(limitSwitchNear.get()){
            //return 2;
        else{
            return 0;
        }*/
        return 0;
    }

    public void setTowWheels(double speed){
        //System.out.println("setting speed " + speed);
        towWheels.set(speed);
    }

    public void extendTow(){
        towArm.set(Value.kForward);
    }

    public void retractTow(){
        towArm.set(Value.kReverse);
    }

    public void printLimit(){
        /*if(limitSwitchFar.get()){
            //System.out.println("true");
        }else if(!limitSwitchFar.get()){
            //System.out.println("false");
        }else{
            System.out.println(limitSwitchFar.get());
        }*/
    }
    /*public int getLimitStatus(){
        return 0;
    }*/
    public void setWinch(double speed){
        winch.set(ControlMode.PercentOutput, speed);
        /*switch(getLimitStatus()){
            case 1:
                if(speed < 0){
                    winch.set(ControlMode.PercentOutput, 0);
                }
                System.out.println("FAR LIMIT REACHED. SETTING WINCH OUTPUT TO 0.");
                break;
            case 2:
                winch.set(ControlMode.PercentOutput, 0);
                System.out.println("NEAR LIMIT REACHED. SETTING WINCH OUTPUT TO 0.");
                break;
            case 0:
                winch.set(ControlMode.PercentOutput, speed);
                

                break;
    }*/
    }
    
}

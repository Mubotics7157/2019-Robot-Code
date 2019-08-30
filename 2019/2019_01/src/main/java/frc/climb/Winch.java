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
import frc.robot.Constants;

/**
 * Add your docs here.
 */
public class Winch {

    //TalonSRX talon;
    DigitalInput limitSwitchFar;
    DigitalInput limitSwitchNear;
    TalonSRX winch;
    TalonSRX winchSlave;

    public void init(){
        winch = new TalonSRX(Constants.kTalonWinch);
        winchSlave = new TalonSRX(Constants.kTalonWinchSlave);
        limitSwitchFar = new DigitalInput(0);
        limitSwitchNear = new DigitalInput(1);
        winchSlave.follow(winch);
    }

    public int getLimitStatus(){
        if(limitSwitchFar.get()){
            return 1;
        }else if(limitSwitchNear.get()){
            return 2;
        }else{
            return 0;
        }
    }

    public void setWinch(double speed){
        switch(getLimitStatus()){
            case 1:
                winch.set(ControlMode.PercentOutput, 0);
                System.out.println("FAR LIMIT REACHED. SETTING WINCH OUTPUT TO 0.");
                break;
            case 2:
                winch.set(ControlMode.PercentOutput, 0);
                System.out.println("NEAR LIMIT REACHED. SETTING WINCH OUTPUT TO 0.");
                break;
            case 0:
                winch.set(ControlMode.PercentOutput, speed);
                break;
        }
    }
}

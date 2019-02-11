/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.climb;

import edu.wpi.first.wpilibj.Servo;
import frc.robot.Constants;

//forks class omegalul

public class Forks {
    Servo forkL;
    Servo forkR;
    public void init(){
        forkL = new Servo(Constants.kForkLeft);
        forkR = new Servo(Constants.kForkRight);
    }
    public void dropForks(){
        forkL.set(1);
        forkR.set(1);
    }
    public void reset(){
        forkL.set(0);
        forkR.set(0);
    }

    public void eatDinner(){
        System.out.println("mmmm so delicious...");
    }
}


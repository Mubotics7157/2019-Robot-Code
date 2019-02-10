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
}


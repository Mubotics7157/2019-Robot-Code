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
    Servo forkLFront;
    Servo forkLBack;
    Servo forkRFront;
    Servo forkRBack;
    double position = 0;
    public void init(){
        forkLFront = new Servo(Constants.kForkLFront);
        forkLBack = new Servo(Constants.kForkLBack);
        forkRFront = new Servo(Constants.kForkRFront);
        forkRBack = new Servo(Constants.kForkRBack);
    }
    public void setForks(double pos){
        forkLFront.set(pos);
        forkLBack.set(pos);
        forkRFront.set(pos);
        forkRBack.set(pos);
    }
    public void setLeft(double pos){
        forkLFront.set(pos);
        forkLBack.set(pos);
    }
    public void setRight(double pos){
        forkRFront.set(pos);
        forkRFront.set(pos);
    }
    public void drop(){
        setForks(Constants.kReleasePos);
    }
    public void reset(){
        setForks(Constants.kResetPos);
    }
    public void expand(){
        setLeft(Constants.kExpandPos);
        setRight(-Constants.kExpandPos);
    }
    public void setServo(double delta){
        position = position + delta;
        forkLFront.set(delta);
        //forkL.set
    }

    public void eatDinner(){
        System.out.println("mmmm so delicious...");
        System.out.println("weef bellington for dinner");
    }
}


/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.arm;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class TowArm {
    boolean isOn = true;
    boolean isOut = false;
    Compressor comp;
    Spark towSpark;
    DoubleSolenoid extendo;

    public void init(){
        comp = new Compressor();
        towSpark = new Spark(0);
        extendo = new DoubleSolenoid(0, 1);
    }

    public void setSpark(double speed){
        towSpark.set(speed);
    }
    public void extend(){
        extendo.set(Value.kForward);
    }

    public void reverse(){
        extendo.set(Value.kReverse);
    }

    public void toggleExtend(){
        if(isOut){
            isOut = false;
            extendo.set(Value.kReverse);
        }else{
            isOut = true;
            extendo.set(Value.kForward);
        }

    }

    public void compressorOff(){
        comp.stop();
    }

    public void compressorOn(){
        comp.start();
    }

    public void toggleCompressor(){
        if(isOn){
            isOn = false;
            comp.stop();
        }else{
            isOn = true;
            comp.start();
        }

    }


}

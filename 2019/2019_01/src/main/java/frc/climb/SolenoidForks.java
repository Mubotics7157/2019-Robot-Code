/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.climb;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import frc.robot.Constants;
/**
 * Add your docs here.
 */
public class SolenoidForks {

    DoubleSolenoid solenoid;

    public void init(){
        solenoid = new DoubleSolenoid(Constants.kForkIn, Constants.kForkOut);
    }

    public void set(Value value){
        solenoid.set(value);
    }
}

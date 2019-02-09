package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class Arm{

    WPI_TalonSRX main = new WPI_TalonSRX(0);

    public void init(){
        main.clearStickyFaults();
    }

    public void setPos(double position){
        main.set(ControlMode.Position, position);
    }
}
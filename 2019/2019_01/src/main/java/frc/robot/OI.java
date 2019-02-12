package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

public class OI{

    public Joystick controller1 = new Joystick(0);
    public Joystick controller2 = new Joystick(1);

    public Joystick[] controllers = {
        controller1,
        controller2
    };
    //methods for EASE OF USE. NO MORE LONG ASS INPUT STATEMENTS (to be implemented after everything is working properly)
    public boolean bDown(int controller, int id){
        return controllers[controller].getRawButton(id);
    }
    public boolean bPressed(int controller, int id){
        return controllers[controller].getRawButtonPressed(id);
    }
    public boolean bReleased(int controller, int id){
        return controllers[controller].getRawButtonReleased(id);
    }
    public double axis(int controller, int id){
        return controllers[controller].getRawAxis(id);
    }
}
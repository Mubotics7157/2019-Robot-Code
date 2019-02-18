package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

public class OI{

    public Joystick controller1 = new Joystick(0);
    public Joystick controller2 = new Joystick(1);
    public boolean[] c1FirstPress = {
        true, true, true, true, true, true,
        true, true, true, true, true, true,
        true, true, true, true, true, true
    };
    boolean[] c2FirstPress = {
        true, true, true, true, true, true,
        true, true, true, true, true, true,
        true, true, true, true, true, true
    };

    public Joystick[] controllers = {
        controller1,
        controller2
    };
    //methods for EASE OF USE. NO MORE LONG ASS INPUT STATEMENTS (to be implemented after everything is working properly)
    public boolean bDown(int controller, int id){
        return controllers[controller].getRawButton(id);
    }
    public boolean bDown(int id){
        c1FirstPress[id] = false;
        return controller1.getRawButton(id);
    }

    
    public int getPOV(){
        return controller1.getPOV();
    }


    public boolean bPressed(int controller, int id){
        switch(controller){
            case 0:
            c1FirstPress[id] = false;
            case 1:
            c2FirstPress[id] = false;
        }
        return controllers[controller].getRawButtonPressed(id);
    }
    public boolean bPressed(int id){
        c1FirstPress[id] = false;
        return controller1.getRawButtonPressed(id);
    }


    public boolean bReleased(int controller, int id){
        return controllers[controller].getRawButtonReleased(id);
    }
    public boolean bReleased(int id){
        return controller1.getRawButtonReleased(id);
    }


    public double axis(int controller, int id){
        return controllers[controller].getRawAxis(id);
    }
    public double axis(int id){
        return controller1.getRawAxis(id);
    }

    public boolean firstPressed(int controller, int id){
        switch(controller){
            case 0:
            return c1FirstPress[id];
            case 1:
            return c2FirstPress[id];
            default:
            return c1FirstPress[id];
        }
    }
    public boolean firstPressed(int id){
        return c1FirstPress[id];
    }

    public void resetButton(int controller, int id){
        switch(controller){
            case 0:
            c1FirstPress[id] = true;
            case 1:
            c2FirstPress[id] = true;
            default:
            c1FirstPress[id] = true;
        }
        
    }
}
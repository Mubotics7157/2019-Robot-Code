package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

class CustomDrive {
    public WPI_TalonSRX talon = new WPI_TalonSRX(2);
    public WPI_VictorSPX victor = new WPI_VictorSPX(3);
    public WPI_VictorSPX victor2 = new WPI_VictorSPX(4);
    public PWMVictorSPX PWMVictor = new PWMVictorSPX(9);
    public Pixy2Handler pixy = new Pixy2Handler();
    public Joystick controller1 = new Joystick(0);
    public Joystick controller2 = new Joystick(1);
    public AHRS navx = new AHRS(Port.kMXP);

    int x0 = pixy.localCache[8];
    int y0 = pixy.localCache[9];
    int x1 = pixy.localCache[10];
    int y1 = pixy.localCache[11];
    int index = pixy.localCache[12];
    int flags = pixy.localCache[13];
    int xI = 39;
    int yI = 25;
    double angle = 0;

    double deadband = 10;
    double angleDeadband = 10;
    double turnSpeed = 0.3;
    double driveSpeed = 0.3;
    double gain;

    double kP;
    double kD;
    double kI;
    double deltaError;
    double lastError;
    double setpoint = 90;

    public void init(){
        //victor2.follow(victor);
        SmartDashboard.putNumber("deadband", 10);
        SmartDashboard.putNumber("angleDeadband", 10);
        SmartDashboard.putNumber("turnSpeed", 0.2);
        SmartDashboard.putNumber("driveSpeed", 0.3);
        SmartDashboard.putNumber("kP", 0.01);
        SmartDashboard.putNumber("kD", 0);
        victor2.follow(victor);
    }
    public void drivePeriodic(){
        updateVars();
        PWMVictor.set(talon.getMotorOutputPercent());
        deadband = SmartDashboard.getNumber("deadband", 0);
        angleDeadband = SmartDashboard.getNumber("angleDeadband", 10);
        turnSpeed = SmartDashboard.getNumber("turnSpeed", 0.3);
        driveSpeed = SmartDashboard.getNumber("driveSpeed", 0.3);
    }

    public void updateVars(){
        pixy.sendRequest(pixy.CHECKSUM_GETMAINFEATURES);
        x0 = pixy.localCache[8];
        y0 = pixy.localCache[9];
        x1 = pixy.localCache[10];
        y1 = pixy.localCache[11];
        index = pixy.localCache[12];
        flags = pixy.localCache[13];
    }

    public void defaultDrive(){
        victor.set(controller2.getRawAxis(1));
        talon.set(-controller1.getRawAxis(1));
    }

    public void driveVariable(double lSpeed, double rSpeed){
        victor.set(-lSpeed);
        talon.set(rSpeed);
    }
    
    public void followLine(){
        //align
        updateVars();
        System.out.println(x0 + " | " + y0 + " | " + x1 + " | " + y1);
        if(Math.abs((x1-x0))>0.001){
            angle = (Math.toDegrees(Math.atan((y1-y0)/(x0-x1))));
            if(angle<0){
                angle = angle + 180; 
            }
        }
        
        kP = SmartDashboard.getNumber("kP", 0);
        kI = SmartDashboard.getNumber("kI", 0);
        kD = SmartDashboard.getNumber("kD", 0);
        driveSpeed = SmartDashboard.getNumber("driveSpeed", 0);

        SmartDashboard.putNumber("NavX", navx.getYaw());
        SmartDashboard.putNumber("Setpoint", setpoint);

        double error = setpoint-angle;
        deltaError = error - lastError;

        SmartDashboard.putNumber("Error", error);

        double P = error*kP;
        double D = kD*deltaError;
        gain = Math.abs(error)>0.1 ? P+D : 0;
        System.out.println("Error: " + error);
        System.out.println("Gain: " + gain);
        System.out.println("Angle: " + angle);
        //System.out.println("NavX: " + navx.getYaw());
        driveVariable(driveSpeed - gain, driveSpeed + gain);
        lastError = error;
    }
    public double getGain(){
        return gain;
    }       
    public boolean vectorDetected(){
        return pixy.vectorDetected;
    }
}

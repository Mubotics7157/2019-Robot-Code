package frc.drive;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.Constants.DriveState;
import frc.tracking.TrackingHandler;

public class CustomDrive{

    CANSparkMax leftSpark;
    //CANSparkMax leftSpark2;
    CANSparkMax rightSpark;
    CANSparkMax rightSpark2;
    CANPIDController left;
    CANPIDController right;
    public DriveState curDriveState;
    double kPl = 0, kIl = 0, kDl = 0, kFl = 0, kFFl = 0;
    double kPr = 0, kIr = 0, kDr = 0, kFr = 0, kFFr = 0;

    public void init(){
        //leftSpark.restoreFactoryDefaults();
        //leftSpark2.restoreFactoryDefaults();
        //rightSpark2.restoreFactoryDefaults();
        //rightSpark.restoreFactoryDefaults();
        leftSpark = new CANSparkMax(11, MotorType.kBrushless);
        //leftSpark2 = new CANSparkMax(10, MotorType.kBrushless);
        //left = new CANPIDController(leftSpark);

        //leftSpark.follow(null);
        rightSpark = new CANSparkMax(21, MotorType.kBrushless);
        rightSpark2 = new CANSparkMax(20, MotorType.kBrushless);
        //right = new CANPIDController(rightSpark);
        //rightSpark2.follow(rightSpark);
        //leftSpark2.follow(leftSpark);

        leftSpark.setIdleMode(IdleMode.kCoast);
        //leftSpark2.setIdleMode(IdleMode.kCoast);
        rightSpark.setIdleMode(IdleMode.kCoast);
        //rightSpark2.setIdleMode(IdleMode.kCoast);
        
        
        //rightSpark.follow(rightSpark);
        curDriveState = DriveState.MANUAL;
        //initPID();
    } 

    public void initPID(){
        left.setP(kPl);
        left.setI(kIl);
        left.setD(kDl);
        left.setFF(kFFl);
        right.setP(kPr);
        right.setI(kIr);
        right.setD(kDr);
        right.setFF(kFFr);
    }

    public void clearStickies(){
        leftSpark.clearFaults();
        rightSpark.clearFaults();
    }

    public void driveL(double speed){
        leftSpark.set(speed);
    }

    
    public void driveR(double speed){
        rightSpark.set(speed);
    }

    public void driveSL(double speed){
        //leftSpark2.set(speed);
    }
    
    public void driveSR(double speed){
        rightSpark2.set(speed);
    }
    public void drive(double lSpeed, double rSpeed){
        leftSpark.set(Math.abs(lSpeed) > 0.02 ? lSpeed : 0);   
        rightSpark.set(Math.abs(rSpeed) > 0.02 ? rSpeed : 0);
    }

    public void printEncoders(){
        System.out.println("Velocity: " + Math.round(leftSpark.getEncoder().getVelocity()) + " Position:" + leftSpark.getEncoder().getPosition()/10.71);
    }
    //-----------TRACKING DRIVE (CARGO/TAPE)---------------

    private double kP = Constants.kDriveP, kI = Constants.kDriveI, kD = Constants.kDriveD, 
    driveSpeed, deltaError, lastError, integralWindup, setpoint;
    public AHRS navx;
    boolean testing = false;
    TrackingHandler tracking;
    double integralError = 0.0f;

    public void initTracking() {
        navx = new AHRS(Port.kMXP);
        tracking = new TrackingHandler();
        if (testing) {
            SmartDashboard.putNumber("kP", 0.033);
            SmartDashboard.putNumber("kI", 0);
            SmartDashboard.putNumber("kD", 0.16);
            SmartDashboard.putNumber("driveSpeed", 0);
            SmartDashboard.putNumber("integralWindup", 0);
        }
        tracking.initTracking();
    }

    public void driveInACircle(){
        drive(1, 1);
    }

    public void driveAutoPilot() {
        double error = setpoint - navx.getYaw();
        deltaError = error - lastError;
        if (testing) {
        kP = SmartDashboard.getNumber("kP", 0);
        kI = SmartDashboard.getNumber("kI", 0);
        kD = SmartDashboard.getNumber("kD", 0);
        driveSpeed = SmartDashboard.getNumber("driveSpeed", 0);
        integralWindup = SmartDashboard.getNumber("integralWindup", 0);

        SmartDashboard.putNumber("Error", error);
        SmartDashboard.putNumber("NavX", navx.getYaw());
        SmartDashboard.putNumber("Setpoint", setpoint);
        }

        if(tracking.getCargoDetected() && Math.abs(error)<integralWindup){
            integralError = integralError + (error*0.2);   
        }else{
            integralError = 0;
        }

        double P = error*kP;
        double D = kD*deltaError;
        double I = kI*integralError;
        double gain = Math.abs(error)>0.1 ? P+I+D : 0;

        drive(driveSpeed + gain, driveSpeed - gain);
        lastError = error;
    }

    public void switchTarget(){
        tracking.switchTarget();
    }
}
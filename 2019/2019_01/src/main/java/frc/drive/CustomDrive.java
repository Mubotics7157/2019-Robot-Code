package frc.robot;

import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class CustomDrive{

    CANSparkMax leftSpark;
    CANSparkMax leftSpark2;
    CANSparkMax rightSpark;
    CANSparkMax rightSpark2;
    CANPIDController left;
    CANPIDController right;
    DifferentialDrive drive;

    double kPl = 0, kIl = 0, kDl = 0, kFl = 0, kFFl = 0;
    double kPr = 0, kIr = 0, kDr = 0, kFr = 0, kFFr = 0;

    public void init(){
        leftSpark = new CANSparkMax(10, MotorType.kBrushless);
        leftSpark2 = new CANSparkMax(11, MotorType.kBrushless);
        left = new CANPIDController(leftSpark);
        leftSpark2.follow(leftSpark);
        rightSpark = new CANSparkMax(20, MotorType.kBrushless);
        rightSpark2 = new CANSparkMax(21, MotorType.kBrushless);
        right = new CANPIDController(rightSpark);
        rightSpark2.follow(rightSpark);
        drive = new DifferentialDrive(leftSpark, rightSpark);
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

    public void drive(double lSpeed, double rSpeed){
        drive.tankDrive(lSpeed, rSpeed);
    }

    public void printEncoders(){
        System.out.println("Velocity: " + Math.round(leftSpark.getEncoder().getVelocity()) + " Position:" + leftSpark.getEncoder().getPosition()/10.71);
    }
}
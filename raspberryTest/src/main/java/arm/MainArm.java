/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package arm;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.Constants;

/**
 * Add your docs here.
 */
public class MainArm {
    private TalonSRX master;
    private TalonSRX slave;
    private DigitalInput limitswitch;

    private boolean firstHit = true;

    public static enum ArmState {
        Neutral,
        Intaking,
        Hatch,
        Cargo
    }

    public ArmState currArmState = ArmState.Neutral;

    public void InitArm() {
        limitswitch = new DigitalInput(1);
        master = new TalonSRX(Constants.kArmMaster);
        slave = new TalonSRX(Constants.kArmSlave);
        master.configMotionCruiseVelocity(Constants.kMaxSensorVelocity/2);
        master.configMotionAcceleration(Constants.kMaxSensorVelocity/2);
        master.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
        master.config_kP(0, Constants.kArmP);
        master.config_kI(0, Constants.kArmI);
        master.config_kD(0, Constants.kArmD);
        master.config_kF(0, Constants.kArmF);

        slave.follow(master);
    }

    public void ArmPeriodic() {
        if (limitswitch.get()) {
            if (firstHit) {
                master.setSelectedSensorPosition(0);
                firstHit = false;
            }
        } else firstHit = true;

        double setpoint = 0;
        switch (currArmState) {
            case Neutral:
            setpoint = 50;
            break;
        }
		/* calculate targets from gamepad inputs */
		double target_sensorUnits = setpoint * Constants.kSensorUnits;
		double feedFwdTerm = 0.25;	// Percent to add to Closed Loop Output (A * COS(O))

        master.set(ControlMode.MotionMagic, target_sensorUnits,
         DemandType.ArbitraryFeedForward, feedFwdTerm);
    }
}
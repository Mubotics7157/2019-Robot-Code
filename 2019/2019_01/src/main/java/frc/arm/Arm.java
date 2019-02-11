package frc.arm;
import frc.robot.Constants;
import frc.robot.Constants.ArmState;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Arm {
    TalonSRX master;
	TalonSRX slave;
	ArmState curArmState = ArmState.NEUTRAL;
	double setpoint = 0;
	double input = 0;

    public void init() {
        master = new TalonSRX(10);
        slave = new TalonSRX(20);
        slave.follow(master);
        /**
		 * Configure Talon SRX Output and Sesnor direction accordingly
		 * Invert Motor to have green LEDs when driving Talon Forward / Requesting Postiive Output
		 * Phase sensor to have positive increment when driving Talon Forward (Green LED)
		 */
		master.setSensorPhase(true);
		slave.setInverted(false);

		/* Set relevant frame periods to be at least as fast as periodic rate */
		//master.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, Constants.kTimeoutMs);
		//master.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, Constants.kTimeoutMs);

		/* Set the peak and nominal outputs */
		master.configNominalOutputForward(0, Constants.kTimeoutMs);
		master.configNominalOutputReverse(0, Constants.kTimeoutMs);
		master.configPeakOutputForward(1, Constants.kTimeoutMs);
		master.configPeakOutputReverse(-1, Constants.kTimeoutMs);

		/* Set Motion Magic gains in slot0 - see documentation */
		master.selectProfileSlot(Constants.kSlotIdx, 0);
		master.config_kF(Constants.kSlotIdx, Constants.kArmGains.kF, Constants.kTimeoutMs);
		master.config_kP(Constants.kSlotIdx, Constants.kArmGains.kP, Constants.kTimeoutMs);
		master.config_kI(Constants.kSlotIdx, Constants.kArmGains.kI, Constants.kTimeoutMs);
		master.config_kD(Constants.kSlotIdx, Constants.kArmGains.kD, Constants.kTimeoutMs);

		/* Set acceleration and vcruise velocity - see documentation */
		master.configMotionCruiseVelocity(15000, Constants.kTimeoutMs);
		master.configMotionAcceleration(6000, Constants.kTimeoutMs);
    }

    public void periodic(){
		
		switch (curArmState) {
			case NEUTRAL:
			setpoint = 90;
			break;
			case INTAKING:
			setpoint = 0;
			break;
			case BACKINTAKING:
			setpoint = 180;
			break;
			case CARGO:
			break;
			case BACKCARGO:
			break;
			case HATCH:
			break;
			case BACKHATCH:
			break;
			case FREEHAND:
			break;
		}
		double targetPos = setpoint * Constants.kSensorUnits;
		double arbFeedForward = 0; //COS (O) * kFA
		if(curArmState == ArmState.FREEHAND){
			master.set(ControlMode.PercentOutput, input);
		}else{
			master.set(ControlMode.MotionMagic, arbFeedForward, DemandType.ArbitraryFeedForward, targetPos);
		}
	}

	public void setFreehandInput(double speed){
		input = speed;
	}
	
	public void moveToState(ArmState toMove) {
		curArmState = toMove;
	}
}
package frc.arm;
import frc.robot.Constants;
import frc.robot.Constants.ArmState;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Arm {
    TalonSRX master;
	VictorSPX slave;
	ArmState curArmState = ArmState.FREEHAND;
	double setpoint = 111;
	double input = 0;//2572
	int limit = -38336;

    public void init() {
        master = new TalonSRX(2);
        slave = new VictorSPX(4);
        slave.follow(master);
        /**
		 * Configure Talon SRX Output and Sesnor direction accordingly
		 * Invert Motor to have green LEDs when driving Talon Forward / Requesting Postiive Output
		 * Phase sensor to have positive increment when driving Talon Forward (Green LED)
		 */
		master.setSensorPhase(false);
		slave.setInverted(false);
		master.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
		master.setSelectedSensorPosition(0);
		master.selectProfileSlot(0, 0);

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
		master.configMotionCruiseVelocity(2572, Constants.kTimeoutMs);
		master.configMotionAcceleration(2572, Constants.kTimeoutMs);
		master.configForwardSoftLimitEnable(true);
		master.configForwardSoftLimitThreshold(0, Constants.kTimeoutMs);
		master.configReverseSoftLimitEnable(true);
		master.configReverseSoftLimitThreshold(limit, Constants.kTimeoutMs);
    }

    public void periodic(){
		
		switch (curArmState) {
			
			case NEUTRAL:
			setpoint = -90;
			break;
			case INTAKING:
			setpoint = 10;
			break;
			case BACKINTAKING:
			setpoint = -190;
			break;
			case CARGO:
			setpoint = -40;
			break;
			case BACKCARGO:
			setpoint = -120;
			break;
			case HATCH:
			setpoint = 0;
			break;
			case BACKHATCH:
			setpoint = -180;
			break;
			case FREEHAND:
			break;
			case TEST:
			setpoint = 0;
			break;
		}
		double targetPos = setpoint * Constants.kSensorUnits * Constants.kGearRatio / 360;
		double armAngle = master.getSelectedSensorPosition()/Constants.kSensorUnits/Constants.kGearRatio*360;
		double arbFeedForward = getFeedForward(armAngle);
		SmartDashboard.putNumber("targetPOS", targetPos);
		SmartDashboard.putNumber("setpoint", setpoint);
		SmartDashboard.putNumber("armAngle", armAngle);
		SmartDashboard.putNumber("motorOutputPercent", master.getMotorOutputPercent());
		printEncoder();
		if(curArmState == ArmState.FREEHAND){
			master.set(ControlMode.PercentOutput, input);
		}else{
			master.set(ControlMode.MotionMagic, targetPos, DemandType.ArbitraryFeedForward, arbFeedForward);
		}/*else{
			master.set(ControlMode.MotionMagic, targetPos, DemandType.ArbitraryFeedForward, arbFeedForward);
		}*/
	}
	public double getFeedForward(double angle){
		return Math.cos(angle)*0.086;
	}
	public void toggleLimits(boolean bool){
		master.configForwardSoftLimitEnable(bool);
		master.configForwardSoftLimitEnable(bool);
	}
	public void printEncoder(){
		SmartDashboard.putNumber("Master Encoder", master.getSelectedSensorPosition());
	}

	public void setFreehandInput(double speed){
		input = speed;
	}
	
	public void moveToState(ArmState toMove) {
		curArmState = toMove;
	}
	public void zeroEncoder(){
		master.setSelectedSensorPosition(0);
	}
}
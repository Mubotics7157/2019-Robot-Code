package frc.arm;
import frc.robot.Constants;
import frc.robot.Constants.ArmState;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Arm {
    TalonSRX master;
	TalonSRX slave;

	Spark tow;
	DigitalInput frontSwitch;
	DigitalInput backSwitch;

	ArmState curArmState = ArmState.FREEHAND;
	
	double setpoint = 111;
	double input = 0;
	double towInput = 0;

	int farLimit = -38336;
	int nearLimit = 0;

    public void init() {
        master = new TalonSRX(Constants.kArmMaster);
		slave = new TalonSRX(Constants.kArmSlave);
		tow = new Spark(Constants.kTow);
		//frontSwitch = new DigitalInput(Constants.kSwitchFront);
		//backSwitch = new DigitalInput(Constants.kSwitchBack);
		
        slave.follow(master);
        /**
		 * Configure Talon SRX Output and Sesnor direction accordingly
		 * Invert Motor to have green LEDs when driving Talon Forward / Requesting Postiive Output
		 * Phase sensor to have positive increment when driving Talon Forward (Green LED)
		 */
		master.setSensorPhase(false);
		slave.setInverted(false);
		master.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
		master.selectProfileSlot(0, 0);

		/* Set relevant frame periods to be at least as fast as periodic rate */
		//master.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, Constants.kTimeoutMs);
		//master.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, Constants.kTimeoutMs);

		/* Set the peak and nominal outputs */
		master.configNominalOutputForward(0, Constants.kTimeoutMs);
		master.configNominalOutputReverse(0, Constants.kTimeoutMs);
		master.configPeakOutputForward(0.9, Constants.kTimeoutMs);
		master.configPeakOutputReverse(-0.9, Constants.kTimeoutMs);

		/* Set Motion Magic gains in slot0 - see documentation */
		master.selectProfileSlot(Constants.kSlotIdx, 0);
		master.config_kF(Constants.kSlotIdx, Constants.kArmGains.kF, Constants.kTimeoutMs);
		master.config_kP(Constants.kSlotIdx, Constants.kArmGains.kP, Constants.kTimeoutMs);
		master.config_kI(Constants.kSlotIdx, Constants.kArmGains.kI, Constants.kTimeoutMs);
		master.config_kD(Constants.kSlotIdx, Constants.kArmGains.kD, Constants.kTimeoutMs);

		/* Set acceleration and vcruise velocity - see documentation */
		master.configMotionCruiseVelocity(100, Constants.kTimeoutMs);
		master.configMotionAcceleration(100, Constants.kTimeoutMs);
		master.configForwardSoftLimitEnable(false);
		master.configForwardSoftLimitThreshold(0, Constants.kTimeoutMs);
		master.configReverseSoftLimitEnable(false);
		master.configReverseSoftLimitThreshold(farLimit, Constants.kTimeoutMs);

		master.configAllowableClosedloopError(Constants.kSlotIdx, Constants.kAllowableError);
    }

    public void periodic(){
		
		switch (curArmState) {
			
			case NEUTRAL:
			setpoint = -90;
			SmartDashboard.putString("ArmState", "NEUTRAL");
			break;
			case INTAKING:
			setpoint = 10;
			SmartDashboard.putString("ArmState", "INTAKING");
			break;
			case BACKINTAKING:
			setpoint = -160;
			SmartDashboard.putString("ArmState", "BACKINTAKING");
			break;
			case CARGO:
			setpoint = -40;
			SmartDashboard.putString("ArmState", "CARGO");
			break;
			case BACKCARGO:
			setpoint = -120;
			SmartDashboard.putString("ArmState", "BACKCARGO");
			break;
			case HATCH:
			setpoint = 0;
			SmartDashboard.putString("ArmState", "HATCH");
			break;
			case BACKHATCH:
			setpoint = -150;
			SmartDashboard.putString("ArmState", "BACKHATCH");
			break;
			case FREEHAND:
			SmartDashboard.putString("ArmState", "FREEHAND");
			break;
			case TOW:
			setpoint = 0;
			SmartDashboard.putString("ArmState", "TOW");
			case TEST:
			setpoint = 0;
			SmartDashboard.putString("ArmState", "TEST");
			break;
		}
		double targetPos = setpoint * Constants.kSensorUnits / 360;
		double armAngle = master.getSelectedSensorPosition()/Constants.kSensorUnits*360;
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
		}
		if(curArmState == ArmState.TOW){
			tow.set(towInput);
		}
	}
	public double getFeedForward(double angle){
		return Math.cos(angle)*0.086;
	}
	public void setTow(double speed){
		tow.set(speed);
	}
	public void toggleLimits(boolean bool){
		master.configForwardSoftLimitEnable(bool);
		master.configForwardSoftLimitEnable(bool);
	}
	public void printEncoder(){
		SmartDashboard.putNumber("Master Encoder", master.getSelectedSensorPosition());
	}
	public void printArmAngle(){
		SmartDashboard.putNumber("Arm Angle", master.getSelectedSensorPosition()/Constants.kSensorUnits*360);
	}
	public double getArmAngle(){
		return master.getSelectedSensorPosition()/Constants.kSensorUnits*360;
	}

	public void setFreehandInput(double speed){
		input = speed;
	}

	public void setTowInput(double speed){
		towInput = speed;
	}
	
	public void moveToState(ArmState toMove) {
		curArmState = toMove;
	}
	public void zeroEncoder(){
		master.setSelectedSensorPosition(0);
	}
}
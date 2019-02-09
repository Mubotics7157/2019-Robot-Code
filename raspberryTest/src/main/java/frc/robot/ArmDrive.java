package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Joystick;

public class ArmDrive{
    //*** */TEST CLASS RIPPED FROM CTRE DOCUMENTATION***
    
    WPI_TalonSRX _talon;
    WPI_TalonSRX slaveArm;
    OI oi;

    public void init(){
        _talon = new WPI_TalonSRX(10);
        slaveArm = new WPI_TalonSRX(20);
        oi = new OI();
        slaveArm.follow(_talon);

        /**
		 * Configure Talon SRX Output and Sesnor direction accordingly
		 * Invert Motor to have green LEDs when driving Talon Forward / Requesting Postiive Output
		 * Phase sensor to have positive increment when driving Talon Forward (Green LED)
		 */
		_talon.setSensorPhase(true);
		_talon.setInverted(false);

		/* Set relevant frame periods to be at least as fast as periodic rate */
		//_talon.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, Constants.kTimeoutMs);
		//_talon.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, Constants.kTimeoutMs);

		/* Set the peak and nominal outputs */
		_talon.configNominalOutputForward(0, Constants.kTimeoutMs);
		_talon.configNominalOutputReverse(0, Constants.kTimeoutMs);
		_talon.configPeakOutputForward(1, Constants.kTimeoutMs);
		_talon.configPeakOutputReverse(-1, Constants.kTimeoutMs);

		/* Set Motion Magic gains in slot0 - see documentation */
		_talon.selectProfileSlot(Constants.kSlotIdx, Constants.kPIDLoopIdx);
		_talon.config_kF(Constants.kSlotIdx, Constants.kGains.kF, Constants.kTimeoutMs);
		_talon.config_kP(Constants.kSlotIdx, Constants.kGains.kP, Constants.kTimeoutMs);
		_talon.config_kI(Constants.kSlotIdx, Constants.kGains.kI, Constants.kTimeoutMs);
		_talon.config_kD(Constants.kSlotIdx, Constants.kGains.kD, Constants.kTimeoutMs);

		/* Set acceleration and vcruise velocity - see documentation */
		_talon.configMotionCruiseVelocity(15000, Constants.kTimeoutMs);
		_talon.configMotionAcceleration(6000, Constants.kTimeoutMs);

		/* Zero the sensor */
		_talon.setSelectedSensorPosition(0, Constants.kPIDLoopIdx, Constants.kTimeoutMs);
        
    }

    public void periodic(){
        double motorOutput = _talon.getMotorOutputPercent();

        if (oi.controllerL.getRawButton(1)) {
			/* Motion Magic */ 
			
			/*4096 ticks/rev * 10 Rotations in either direction */
			double targetPos = oi.controllerL.getRawAxis(1) * 4096 * 10.0;
            _talon.set(ControlMode.MotionMagic, targetPos);
            System.out.println("_talon: " + motorOutput);

		} else {
			/* Percent Output */

			_talon.set(ControlMode.PercentOutput, oi.controllerR.getRawAxis(1));
		}
    }


}
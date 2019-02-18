/**
 *  Class that organizes gains used when assigning values to slots
 */
package frc.robot;

public class Gains {
	public final double kP;
	public final double kI;
	public final double kD;
	public final double kF;
	public final int kIzone;
	public final double kPeakOutput;
	public final double driveSpeed;
	
	public Gains(double _kP, double _kI, double _kD, double _kF, int _kIzone, double _kPeakOutput){
		kP = _kP;
		kI = _kI;
		kD = _kD;
		kF = _kF;
		kIzone = _kIzone;
		kPeakOutput = _kPeakOutput;
		driveSpeed = 0;
	}
	public Gains(double _kP, double _kI, double _kD, double _kF, double _driveSpeed){
		kP = _kP;
		kI = _kI;
		kD = _kD;
		kF = _kF;
		kIzone = 0;
		kPeakOutput = 1;
		driveSpeed = _driveSpeed;
	}
}
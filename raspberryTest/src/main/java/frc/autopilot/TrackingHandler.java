/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.autopilot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;

/**
 * Add your docs here.
 */
public class TrackingHandler {
    private boolean is_enabled, driverVision, tapeVision, cargoVision, lastDetected;
	private NetworkTableEntry tapeDetected, cargoDetected, tapeYaw, cargoYaw, driveWanted, tapeWanted, cargoWanted, videoTimestamp;
	private double targetAngle, timestamp;
	NetworkTableInstance instance;
    NetworkTable chickenVision;
    
  public void initTracking() {
    instance = NetworkTableInstance.getDefault();
    chickenVision = instance.getTable("ChickenVision");

    tapeDetected = chickenVision.getEntry("tapeDetected");
    cargoDetected = chickenVision.getEntry("cargoDetected");
    tapeYaw = chickenVision.getEntry("tapeYaw");
    cargoYaw = chickenVision.getEntry("cargoYaw");

    driveWanted = chickenVision.getEntry("Driver");
    tapeWanted = chickenVision.getEntry("Tape");
    cargoWanted = chickenVision.getEntry("Cargo");

    videoTimestamp = chickenVision.getEntry("VideoTimestamp");

    tapeVision = false;
    cargoVision = true;

    driverVision = true;
  }

  	public void updateTracking() {
			driverVision = false;

			driveWanted.setBoolean(driverVision);
			tapeWanted.setBoolean(tapeVision);
			cargoWanted.setBoolean(cargoVision);
			SmartDashboard.putBoolean("Cargo detected", cargoDetected.getBoolean(false));
            SmartDashboard.putString("Currently Tracking", tapeVision ? "Tape" : "Cargo");

            if(tapeDetected.getBoolean(false) && tapeVision) {
				targetAngle = tapeYaw.getDouble(0);
				SmartDashboard.putNumber("Tape Yaw", targetAngle);
			} else {
				targetAngle = 0;
            }
			if(cargoDetected.getBoolean(false) && cargoVision) {
                if (lastDetected != true) {
                    Robot.drive.navx.zeroYaw();
                    System.out.println("New Cargo Detected");
                }
                lastDetected = true;
				targetAngle = cargoYaw.getDouble(0);
				SmartDashboard.putNumber("Cargo Yaw", targetAngle);
			} else {
                lastDetected = false;
				targetAngle = 0;
            }
            
		SmartDashboard.putNumber("Cargo Yaw", targetAngle);

	}
	
	public double targetYaw() {
		return targetAngle;
    }
    
    public void targetTape() {
        tapeVision = true;
        cargoVision = false;
    }

    public void targetCargo() {
        tapeVision = false;
        cargoVision = true;
    }

    public boolean getCargoDetected(){
        return cargoDetected.getBoolean(false);
    }
}

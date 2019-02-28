/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.arm;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSink;
import edu.wpi.first.cameraserver.CameraServer;

/**
 * Add your docs here.
 */
public class CameraManager {
    UsbCamera camera1;
    UsbCamera camera2;
    UsbCamera camera3;
    VideoSink server;

    public void init(){
        camera1 = CameraServer.getInstance().startAutomaticCapture(0);
        camera2 = CameraServer.getInstance().startAutomaticCapture(1);
        camera3 = CameraServer.getInstance().startAutomaticCapture(2);
        server = CameraServer.getInstance().getServer();
    }

    public void setCamera(int camera){
        switch(camera){
            case 1: server.setSource(camera1);
            case 2: server.setSource(camera2);
            case 3: server.setSource(camera3);
        }
    }
}


/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.drive;

import jaci.pathfinder.PathfinderFRC;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.followers.EncoderFollower;

/**
 * Add your docs here.
 */
public class PathFollower {
    private final int k_ticks_per_rev = 1024;
    private final double k_wheel_diameter = 4.0 / 12.0;
    private final double k_max_velocity = 10;
  
    private final int k_left_channel = 0;
    private final int k_right_channel = 1;

    private final int k_left_encoder_port_a = 0;
    private final int k_left_encoder_port_b = 1;
    private final int k_right_encoder_port_a = 2;
    private final int k_right_encoder_port_b = 3;
  
    private final String k_path_name = "example";

    private EncoderFollower leftFollower;
    private EncoderFollower rightFollower;

    public void PathInit() {
        Trajectory left_trajectory = PathfinderFRC.getTrajectory(k_path_name + ".left");
        Trajectory right_trajectory = PathfinderFRC.getTrajectory(k_path_name + ".right");
        leftFollower = new EncoderFollower(left_trajectory);
        leftFollower = new EncoderFollower(left_trajectory);
    }

    public void followPath() {
        
    }
}

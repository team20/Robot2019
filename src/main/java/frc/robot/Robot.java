/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.AutoClasses.AutoModes;
import frc.robot.Controls.DriverControls;
import frc.robot.Controls.OperatorControls;
import frc.robot.Subsystems.Drivetrain;
import frc.robot.Subsystems.Elevator;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */

  AutoModes auto;

  Drivetrain drive;
  Elevator elevator;

  DriverControls driver;
  OperatorControls operator;

  boolean autoSet;

  @Override
  public void robotInit() {
    auto = new AutoModes();

    drive = new Drivetrain();
    elevator = new Elevator();

    driver = new DriverControls();
    operator = new OperatorControls();

    autoSet = false;
  }

  @Override
  public void autonomousInit() {

  }

  @Override
  public void autonomousPeriodic() {
    if(!autoSet){
      auto.crossLine();
      autoSet = true;
    }
    auto.runAuto();
  }

  @Override
  public void teleopInit() {
  }

  @Override
  public void teleopPeriodic() {
    driver.driverControls();
    operator.operatorControls();
  }

  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
  }

}

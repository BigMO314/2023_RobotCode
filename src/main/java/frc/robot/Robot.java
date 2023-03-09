// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.TimedRobot;
import frc.molib.buttons.ButtonManager;
import frc.robot.period.Autonomous;
import frc.robot.period.Disabled;
import frc.robot.period.Teleoperated;
import frc.robot.period.Test;
import frc.robot.subsystem.Chassis;
import frc.robot.subsystem.Elevator;
import frc.robot.subsystem.Manipulator;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
    public static final NetworkTable tblMain = NetworkTableInstance.getDefault().getTable("MO Data");
    public static final NetworkTable tblPeriod = tblMain.getSubTable("Period");
    public static final NetworkTable tblSubsystem = tblMain.getSubTable("Subsystem");

    /**
     * This function is run when the robot is first started up and should be used for any
     * initialization code.
     */
    @Override
    public void robotInit() {
        //TODO: Add console logging of each step
        //Wait for NetworkTables connection4
        while(!NetworkTableInstance.getDefault().isConnected());

        //Initialize Dashboard values
        Chassis.initDashboard();
        Elevator.initDashboard();
        Manipulator.initDashboard();
        Disabled.initDashboard();
        Autonomous.initDashboard();
        Teleoperated.initDashboard();
        Test.initDashboard();

        Chassis.init();
        Elevator.init();
        Manipulator.init();
    }

    @Override
    public void robotPeriodic() {
        ButtonManager.updateValues();
        /*Autonomous.pushDashboardValues();
        Disabled.pushDashboardValues();
        Teleoperated.pushDashboardValues(); */
        Test.pushDashboardValues();
        Chassis.pushDashboardValues();
        Elevator.pushDashboardValues();
        Manipulator.pushDashboardValues();
    }

    @Override
    public void autonomousInit() {
        Autonomous.init();
    }

    @Override
    public void autonomousPeriodic() {
        Autonomous.periodic();
    }

    @Override
    public void teleopInit() {
        Teleoperated.init();
    }

    @Override
    public void teleopPeriodic() {
        Teleoperated.periodic();
    }

    @Override
    public void disabledInit() {
        Disabled.init();
    }

    @Override
    public void disabledPeriodic() {
        Disabled.periodic();
    }

    @Override
    public void testInit() {
        Test.init();
    }

    @Override
    public void testPeriodic() {
        Test.periodic();
    }
}

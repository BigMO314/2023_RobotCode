package frc.robot.period;

import com.ctre.phoenix.motorcontrol.NeutralMode;

import frc.robot.subsystem.Chassis;
import frc.robot.subsystem.Elevator;
import frc.robot.subsystem.Manipulator;

/**
 * The Disabled Period
 * <p>
 * Sets chassis to coast when robot disabled.
 * @author Celia Peters
 */
public class Disabled {
    //TODO: Create Network Table
    //TODO: Create Dashboard Entries

    //TODO: Create components

    public static void init() {
        Chassis.setDriveNeutralMode(NeutralMode.Coast);

        Chassis.disablePIDs();
        Elevator.disablePIDs();
        Manipulator.closeGrip();
        Manipulator.retractArm();
    }

    public static void initDashboard() {
        
    }

    public static void periodic() {
        //TODO: Update subsystems
    }
}

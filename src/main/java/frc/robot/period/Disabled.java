package frc.robot.period;

import com.ctre.phoenix.motorcontrol.NeutralMode;

import frc.molib.buttons.Button;
import frc.molib.hid.XboxController;
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
    private static final XboxController ctlDriver = new XboxController(0);

    private static final Button btnChassis_Brake = new Button(){
        @Override public boolean get() { return ctlDriver.getLeftTrigger(); }
    };

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
        if(btnChassis_Brake.get())
            Chassis.setDriveNeutralMode(NeutralMode.Brake);
        else
            Chassis.setDriveNeutralMode(NeutralMode.Coast);
    }
}

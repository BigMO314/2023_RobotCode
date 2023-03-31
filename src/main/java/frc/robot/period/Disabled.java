package frc.robot.period;

import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj.Timer;
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

    public static final Timer tmrBrake = new Timer();
    public static Boolean mIsBrakeTimerEnabled = false;

    private static final Button btnChassis_Brake = new Button(){
        @Override public boolean get() { return ctlDriver.getLeftTrigger(); }
    };

    public static void init() {
        Chassis.setDriveNeutralMode(NeutralMode.Coast);

        tmrBrake.reset();
        tmrBrake.start();

        Chassis.disablePIDs();
        Elevator.disablePIDs();
        Manipulator.closeGrip();
        Manipulator.retractArm();
    }

    public static void initDashboard() {
        
    }

    public static void setBreakTimerEnabled(boolean isEnabled) {
        mIsBrakeTimerEnabled = isEnabled;
    }

    public static void periodic() {
        if(btnChassis_Brake.get() || (tmrBrake.get() < 20.00 && mIsBrakeTimerEnabled)){
            Chassis.setDriveNeutralMode(NeutralMode.Brake);
        } else{
            Chassis.setDriveNeutralMode(NeutralMode.Coast);
        }
    }
}

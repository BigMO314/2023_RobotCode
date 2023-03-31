package frc.robot.period;

import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.networktables.NetworkTable;
import frc.molib.dashboard.Entry;
import frc.molib.hid.XboxController;
import frc.robot.Robot;
import frc.robot.subsystem.Chassis;
import frc.robot.subsystem.Elevator;
import frc.robot.subsystem.Manipulator;

/**
 * The Test Period
 * <p>
 * Allows for basic robot manipulation to test robot systems.
 * @author Celia Peters
 * @author Tavares Mance
 */
@SuppressWarnings("unused")
public class Test {
    
    //Network table
    private static NetworkTable tblTest = Robot.tblPeriod.getSubTable("Test");
    private static NetworkTable tblElevatorPID = tblTest.getSubTable("Elevator PID");

    //Create dashboard entries if necessary
    
    //Xbox controllers
    private static XboxController ctlTester = new XboxController(0);

    /**
     * Prevent instances of test class
     */
    private Test(){}

    /**
     * Runs once at start of test program
     */
    public static void init() {
        Chassis.setDriveNeutralMode(NeutralMode.Brake);

        Disabled.setBreakTimerEnabled(false);
    }

    /**
     * Push dropdowns to dashboard if necessary
     */
    public static void initDashboard() {
    }

    public static void pushDashboardValues(){
    }

    public static void periodic() {

        //controller extend/retract arm simulation to test pneumatics

        //controller open/close grip simulation to test pneumatics

        if(ctlTester.getLeftBumper()){
            Chassis.resetAngle();
        }

        if(ctlTester.getRightBumper()){
            Chassis.resetDistance();
        }

        if(ctlTester.getXButtonPressed()){
            Chassis.resetDistance();
            Chassis.goToDistance(120.00);
        }

        if(ctlTester.getAButtonPressed()){
            Chassis.resetAngle();
            Chassis.goToAngle(90.00);
        }

        Chassis.periodic();
        Elevator.periodic();
        Manipulator.periodic();
    }
}

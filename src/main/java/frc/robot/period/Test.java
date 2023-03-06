package frc.robot.period;

import edu.wpi.first.networktables.NetworkTable;
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

    //Create dashboard entries if necessary
    
    //Xbox controllers
    private static XboxController ctlTester = new XboxController(1);

    /**
     * Prevent instances of test class
     */
    private Test(){}

    /**
     * Runs once at start of test program
     */
    public static void init() {
    }

    /**
     * Push dropdowns to dashboard if necessary
     */
    public static void initDashboard() {
        
    }

    public static void periodic() {
        //controller drive chassis basic test version
        if(ctlTester.getRightBumper()){
            Elevator.setLiftPower(0.25);
        } else if (ctlTester.getRightTrigger()){
            Elevator.setLiftPower(-0.25);
        } else{
            Elevator.setLiftPower(0.00);
        }

        //controller extend/retract arm simulation to test pneumatics
        if(ctlTester.getAButton()){
            Manipulator.extendArm();
        } else if (ctlTester.getBButton()){
            Manipulator.retractArm();
        }

        //controller open/close grip simulation to test pneumatics
        if(ctlTester.getXButton()){
            Manipulator.openGrip();
        } else if (ctlTester.getYButton()){
            Manipulator.closeGrip();
        }

        if(ctlTester.getLeftBumper()){
            Chassis.resetAngle();
        }

    
    
        Chassis.periodic();
        Elevator.periodic();
        Manipulator.periodic();
    }
}

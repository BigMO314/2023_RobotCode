package frc.robot.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import frc.molib.PIDController;
import frc.molib.dashboard.Entry;
import frc.molib.sensors.MagEncoder;
import frc.robot.Robot;


/**
 * The Manipulator Subsystem
 * <p>
 * Sets up manipulator solenoids.
 * Components relating to game element manipulator. Opening and closing of grip,
 * extension and retraction of arm.
 * @author Celia Peters
 */
@SuppressWarnings("unused")
public class Manipulator {

    //Network Table setup
    private static NetworkTable tblManipulator = Robot.tblSubsystem.getSubTable("Manipulator");
    //Dashboard Entries setup
    private static Entry<Double> entManipulator_Position = new Entry<Double>(tblManipulator, "Manipulator Position");

    //Creates components (motor and solenoid)
    private static Solenoid solArm = new Solenoid(PneumaticsModuleType.CTREPCM, 0);
    private static Solenoid solGrip = new Solenoid(PneumaticsModuleType.CTREPCM, 1);
    
    //Set buffer/starting values so nothing goes zoom zoom until its told to zoom zoom
    private static boolean mArmIsExtended = false;
    private static boolean mGripIsOpen = false;

    //Prevent external changes to Manipulator class
    private Manipulator(){}

    public static void init() {

    }

    public static void initDashboard() {
        
    }

    /** Push new values to dashboard objects */
    public static void pushDashboardValues(){

    }

    /**
     * Read whether Arm is extended
     * @return True is Arm is extended
     */
    public static boolean isArmExtended() {
        return mArmIsExtended;
    }

    /**
     * Read whether Grip is open
     * @return True is Grip is open
     */
    public static boolean isGripOpen() {
        return mGripIsOpen;
    }

    /**
     * Sets Arm state (extended/retracted)
     * @param armIsExtended Arm state
     */
    public static void setArmIsExtended(boolean armIsExtended){
        mArmIsExtended = armIsExtended;
    }

    /**
     * Sets Grip state (open/closed)
     * @param gripIsOpen Grip state
     */
    public static void setGripIsOpen(boolean gripIsOpen){
        mGripIsOpen = gripIsOpen;
    }

    /**
     * Use pneumatics to retract Arm fully
     */
    public static void retractArm(){
        setArmIsExtended(false);
    }

    /**
     * Use pneumatics to extend Arm fully
     */
    public static void extendArm(){
        setArmIsExtended(true);
    }

    /**
     * Use pneumatics to open Grip fully
     */
    public static void openGrip(){
        setGripIsOpen(true);
    }

    /**
     * Use pneumatics to close Grip fully
     */
    public static void closeGrip(){
        setGripIsOpen(false);
    }

    /**
     * Placeholder disable all components
     */
    public static void disable(){

    }

    /**
     *Values are updated and runs the functions.
     */
    public static void periodic() {
        //Update components
        solArm.set(mArmIsExtended);
        solGrip.set(mGripIsOpen);
    }
}

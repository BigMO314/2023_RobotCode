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


/**Systems relating to game element manipulator. Opening and closing of grip, extension and retraction of arm*/
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
    private static boolean mGripIsClosed = false;

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
     * Read whether Grip is closed
     * @return True is Grip is closed
     */
    public static boolean isGripClosed() {
        return mGripIsClosed;
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
     * @param gripIsClosed Grip state
     */
    public static void setGripIsClosed(boolean gripIsClosed){
        mGripIsClosed = gripIsClosed;
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
        setGripIsClosed(false);
    }

    /**
     * Use pneumatics to close Grip fully
     */
    public static void closeGrip(){
        setGripIsClosed(true);
    }

    /**
     * Placeholder disable all components
     */
    public static void disable(){

    }

    /**
     * Runs on a periodic loop that updates values and runs functions
     * according to human input, autonomus input, and game conditions.
     * Can set solenoid position
     */
    public static void periodic() {
        //Update components
        solArm.set(mArmIsExtended);
        solGrip.set(mGripIsClosed);
    }
}

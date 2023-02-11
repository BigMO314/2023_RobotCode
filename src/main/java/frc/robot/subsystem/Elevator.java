package frc.robot.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.networktables.NetworkTable;
import frc.molib.PIDController;
import frc.molib.dashboard.Entry;
import frc.molib.sensors.MagEncoder;
import frc.robot.Robot;

/**
 * Enumerations are a list of things, one by one. 
 */
public class Elevator {

    /**
     * Presets the height the elevator will go up to. 
     */
    enum Height {
        LOW(20.0),
        MIDDLE(35.0),
        HIGH(50.0);

        public final double height;
        private Height(double position) {
            height = position;
        }
    }
    
    /*
     * Network Table is the communication between the robot and the driver station. 
     */
    private static NetworkTable tblElevator = Robot.tblSubsystem.getSubTable("Elevator");

    //Create Dashboard Entries
    private static Entry<Double> entLift_Height = new Entry<Double>(tblElevator, "Lift Height");

    /*
     * //Create the Falcons, PID, and Encoder for the Elevator.
     */
    
    private static WPI_TalonFX mtrElevator = new WPI_TalonFX(4);

    private static PIDController pidElevator_Height = new PIDController(0.0, 0.0, 0.0);

    private static MagEncoder encElevator = new MagEncoder(mtrElevator);

    

    //Create buffer variables
    private static double mElevatorPower = 0.0; 

    /**
     * Constructor: Special type of class that is used to create a new object.
     */
    private Elevator(){}

    /**
     * Runs at the very beginning. 
     */
    public static void init() {
        

    /*
    //Motors, Sensors. 
    */
        mtrElevator.setInverted(false); 

    }

    public static void initDashboard() {

    }

    //Create setLiftPower(double), goToHeight(double), and disableLift() functions
    
    /**
     * Starts the elevator power. 
     * @param power
     */
    public static void setLiftPower(double power){
        mElevatorPower = power;
    } 


    /**
     * Enabling pid and giving it its target.
     * @param height in inches, target in inches.
     */
    public static void goToHeight(double height){
        pidElevator_Height.setSetpoint(height);
        pidElevator_Height.enable();
    }

    /**
     * Enables pid and gives the elevator a certain position to move to. 
     * @param position inches away from target. 
     */
    public static void goToHeight(Height position) {
        goToHeight(position.height);
    }

    /**
     *  Elevator moves up until it reaches said height. 
     * @return
     */
    public static boolean isAtHeight(){
        return pidElevator_Height.atSetpoint();
    }

    /**
     * Elevator stops moving up after reaching height. 
     */
    public static void disableHeightPID(){
        pidElevator_Height.disable();
    }

    /**
     * Elevator is completely disabled. 
     */
    public static void disableLift(){
        setLiftPower(0.0);
    }
    
    /**
     * Updates motors, automatic system.
     */
    public static void periodic() {

        if(pidElevator_Height.isEnabled()){
            setLiftPower(pidElevator_Height.calculate(encElevator.getDistance()));
        }

    //Update components
        mtrElevator.set(ControlMode.PercentOutput, mElevatorPower);
    }

}
package frc.robot.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.networktables.NetworkTable;
import frc.molib.PIDController;
import frc.molib.dashboard.Entry;
import frc.molib.sensors.DigitalInput;
import frc.molib.sensors.MagEncoder;
import frc.robot.Robot;


 /**
 * The Elevator Subsystem
 * <p>
 * Sets up elevator motor, elevator PIDs, and elevator sensors.
 * Components relating to elevator, raising and lowering of elevator.
 * @author Tavares Mance
 */
 public class Elevator {

    /**
     * Preset heights the elevator will go up to. 
     */
    public enum Height {
        LOW(20.0),
        MIDDLE(35.0),
        PORTAL(40.0),
        HIGH(50.0);

        public final double height;
        private Height(double position) {
            height = position;
        }
    }
    
    // Network Table is the communication between the robot and the driver station. 
    
    private static NetworkTable tblElevator = Robot.tblSubsystem.getSubTable("Elevator");

    //Create Dashboard Entries.
    private static Entry<Double> entLift_Height = new Entry<Double>(tblElevator, "Lift Height");
    private static Entry<Boolean> entLift_Bottom = new Entry<Boolean>(tblElevator, "Lift Bottom");
    private static Entry<Boolean> entLift_Top = new Entry<Boolean>(tblElevator, "Lift Top");

    //Create the Falcons, PID, and Encoder for the Elevator.
    
    private static WPI_TalonFX mtrLift = new WPI_TalonFX(6);

    private static PIDController pidLift_Height = new PIDController(0.0, 0.0, 0.0);

    private static MagEncoder encLift = new MagEncoder(mtrLift);

    private static final DigitalInput phoLift_L = new DigitalInput(0, false);
    private static final DigitalInput phoLift_U = new DigitalInput(1, false);    
    

    //Create buffer variables
    private static double mLiftPower = 0.0; 

    /**
     * Constructor: Private so that it cannot instantiate.
     */
    private Elevator(){}

    /**
     * Runs at the very beginning. 
     */
    public static void init() {
        
        //Configuring the motors. 
        mtrLift.setInverted(false); 
        mtrLift.setNeutralMode(NeutralMode.Brake);

        mtrLift.configReverseSoftLimitThreshold(0.0);
        mtrLift.configForwardSoftLimitThreshold(0.0);
        mtrLift.configReverseSoftLimitEnable(false);
        mtrLift.configForwardSoftLimitEnable(false);
        
        //Configure Sensors FIXME: Determine Lift distancePerPulse.
        encLift.configDistancePerPulse(((1.0 / 2048.0) / 16.0) * (1.800 * Math.PI) * 2.0);

        //Configure PIDS
        pidLift_Height.setTolerance(0.25);
        pidLift_Height.configAtSetpointTime(0.125);
        pidLift_Height.configOutputRange(-0.25, 0.25);
    }

    // Initializing dashboard options. 
    public static void initDashboard() {
    
    }

    /**Push new values to dashboard objects.*/
    public static void pushDashboardValues(){
        //Push Sensor Values.
        entLift_Height.set(getLiftHeight());
        entLift_Bottom.set(isLiftAtBottom());
        entLift_Top.set(isLiftAtTop());
    }

    /** Reset Lift encoder distance.*/
    public static void resetLiftHeight() {encLift.reset(); }

    /**
     * Read the height of the Lift since last reset.
     * @param power Height in inches
     */
    public static double getLiftHeight() {return encLift.getDistance(); }

    /**
     * Read whether the Lift is at its bottom limit.
     * @param power True if the bottom photoeye is tripped. 
     */
    public static boolean isLiftAtBottom() { return phoLift_L.get(); }

    /**
     * Read whether the Lift is at its top limit.
     * @param power True if the top photoeye is tripped. 
     */
    public static boolean isLiftAtTop() { return phoLift_U.get(); }

    /**
     * Sets the Elevator power. 
     * @param power [-1.0. 1.0] Power to the Lift.
     */
    public static void setLiftPower(double power){
        mLiftPower = power;
    } 


    /**
     * Enabling PID and gives the Elevator a certain position to move to.
     * @param height target in inches.
     */
    public static void goToHeight(double height){
        pidLift_Height.setSetpoint(height);
        pidLift_Height.enable();
    }

    /**
     * Enables PID and gives the Elevator a certain position to move to. 
     * @param position target position. 
     */
    public static void goToHeight(Height position) {
        goToHeight(position.height);
    }

    /**
     * Checks itself to see if the elevator is at target height. 
     * @return true if elevator is at set height
     */
    public static boolean isAtHeight(){
        return pidLift_Height.atSetpoint();
    }

    /**
     * Disables PID control. 
     */
    public static void disableHeightPID(){
        pidLift_Height.disable();
    }

    /** Disable all PID control of the Elevator */
    public static void disablePIDs(){
        disableHeightPID();
    }
    
    /** Disable all PID control of the Elevator and stop the Lift motors. */
    public static void disable(){
        disablePIDs();
        disableLift();
    }

    /**
     * Lift motor is turned off. 
     */
    public static void disableLift(){
        setLiftPower(0.0);
    }
    
    /**
     * Updates motors, automatic system.
     */
    public static void periodic() {
        
        //PID is override. 
        if(pidLift_Height.isEnabled()){
            setLiftPower(pidLift_Height.calculate(encLift.getDistance()));
        }

        //Safety Checks
        if(isLiftAtBottom()){
            mLiftPower = MathUtil.clamp(mLiftPower, 0.0, Double.POSITIVE_INFINITY);
            resetLiftHeight();
        }
        if(isLiftAtTop())
            mLiftPower = MathUtil.clamp(mLiftPower, Double.NEGATIVE_INFINITY, 0.0);

        
        

    //Update components
        mtrLift.set(ControlMode.PercentOutput, mLiftPower);
    }
}

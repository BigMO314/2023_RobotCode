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
    
    /**Enumerations of positions for arm and assignment of angle values to said positions.
     * Extended is 85.0 degrees, retracted is 0.0 degrees*/
    enum ArmPosition {
        EXTENDED(85.0),
        RETRACTED(0.0);

        public final double angle;

        private ArmPosition(double angle){
            this.angle = angle;
        }
    }

    //Network Table setup
    private static NetworkTable tblManipulator = Robot.tblSubsystem.getSubTable("Manipulator");
    //Dashboard Entries setup
    private static Entry<Double> entManipulator_Position = new Entry<Double>(tblManipulator, "Manipulator Position");

    //Creates components (motor and solenoid)
    private static WPI_TalonFX mtrArm = new WPI_TalonFX(5);
    private static Solenoid solGrip = new Solenoid(PneumaticsModuleType.CTREPCM, 0);

    //Allows you to pull encoder data for use with PID
    private static MagEncoder encArm = new MagEncoder(mtrArm);

    //Where ya set the P and I and D for PID
    private static PIDController pidArm_Angle = new PIDController(0.0, 0.0, 0.0);
    
    //Set buffer/starting values so nothing goes zoom zoom until its told to zoom zoom
    private static double mArmPower = 0.0;
    private static boolean mGripIsClosed = false;

    //Prevent external changes to Manipulator class
    private Manipulator(){}

    /**Set motors as true or false, restrict PID output range, 
     * set time at position before accepting in terms of seconds,
     * and set tolerance for error in terms of degrees.
     * Runs once at start of program*/
    public static void init() {
        //Configure motor
        mtrArm.setInverted(false);

        //PID setup
        pidArm_Angle.configOutputRange(-1.0, 1.0);
        pidArm_Angle.configAtSetpointTime(0.125);
        pidArm_Angle.setTolerance(0.0);

        /*PID notes
        pidArm_Angle.atSetpoint();
        pidArm_Angle.isEnabled();
         */
    }

    public static void initDashboard() {
        
    }

    /**
     * Sets arm power (-1.0, 1.0)
     * @param armPower power of arm motor
     */
    public static void setArmPower(double armPower){
        mArmPower = armPower;
    }

    /**
     * Sets grip state (open/closed)
     * @param gripIsClosed grip state
     */
    public static void setGripIsClosed(boolean gripIsClosed){
        mGripIsClosed = gripIsClosed;
    }

    /**
     * Extend arm to allow manipulator to access game elements on the floor of the field
     */
    public static void extendArm(){
        goToAngle(ArmPosition.EXTENDED);
    }

    /**
     * Retract arm to allow manipulator to protect game elements
     */
    public static void retractArm(){
        goToAngle(ArmPosition.RETRACTED);
    }

    /**
     * Use pneumatics to open grip fully
     */
    public static void openGrip(){
        setGripIsClosed(false);
    }

    /**
     * Use pneumatics to close grip fully
     */
    public static void closeGrip(){
        setGripIsClosed(true);
    }

    /**
     * Use PID to move arm to desired angle
     * @param angle Desired arm angle
     */
    public static void goToAngle(double angle){
        pidArm_Angle.setSetpoint(angle);
        pidArm_Angle.enable();
    }

    /**
     * Plugs position into the other goToAngle function
     * @param position Arm position
     */
    public static void goToAngle(ArmPosition position){
        goToAngle(position.angle);
    }

    /**
     * Finds and returns current angle of arm
     * @return Angle of the arm according to PID
     */
    public static boolean isAtAngle(){
        return pidArm_Angle.atSetpoint();
    }

    /**
     * Disables/stops PID
     */
    public static void disableAnglePID(){
        pidArm_Angle.disable();
    }

    /**
     * Runs on a periodic loop that updates values and runs functions
     * according to human input, autonomus input, and game conditions.
     * Can run arm to desired angle based on PID, set arm power,
     * and set solenoid position
     */
    public static void periodic() {
        if(pidArm_Angle.isEnabled()){
            setArmPower(pidArm_Angle.calculate(encArm.getDistance()));
        }
        
        //Update components
        mtrArm.set(ControlMode.PercentOutput, mArmPower);
        solGrip.set(mGripIsClosed);
    }
}

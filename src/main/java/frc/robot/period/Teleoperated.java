package frc.robot.period;

import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.molib.buttons.Button;
import frc.molib.hid.XboxController;
import frc.robot.Robot;
import frc.robot.subsystem.Chassis;
import frc.robot.subsystem.Elevator;
import frc.robot.subsystem.Manipulator;

/**
 * The Teleoperated Period
 * <p>
 * Reads options from the Dashboard to adjust driver control specifics,
 * allows for driver input to control robot in real time, implements robot
 * protection safety systems.
 * @author Tavares Mance
 * @author Celia Peters
 */
@SuppressWarnings("unused")
public class Teleoperated {

    /** Chassis power options (precision and boost values included) */
    enum ChassisPower{
        TORTOISE("Tortoise - 20%", 0.10, 0.20, 0.20, 0.25),
        LOW("Low - 50%", 0.10, 0.20, 0.50, 0.70),
        MEDIUM("Medium - 65%", 0.10, 0.20, 0.65, 0.75),
        HIGH("High - 90%", 0.10, 0.20, 0.90, 1.0);
        
        public final String label;
        public final double precision;
        public final double slow;
        public final double standard;
        public final double boost;
        private ChassisPower(String label, double precision, double slow, double standard, double boost){
            this.label = label;
            this.precision = precision;
            this.slow = slow;
            this.standard = standard;
            this.boost = boost;
        }

        @Override public String toString(){
            return label;
        }
    }

    /**
     * Drive mode options
     */
    enum DriveMode{
        TANK_DRIVE("Tank drive"),
        CHEEZY_DRIVE("Cheezy drive"),
        ARCADE_DRIVE("Arcade drive");
        
        public final String label;
        private DriveMode(String label){
            this.label = label;
        }

        @Override public String toString(){
            return label;
        }
    }

    //Network Table
    private static NetworkTable tblTeleoperated = Robot.tblPeriod.getSubTable("Teleoperated");

    //Dashboard Entries
    private static SendableChooser<ChassisPower> chsChassisPower = new SendableChooser<ChassisPower>();
    private static SendableChooser<DriveMode> chsDriveMode = new SendableChooser<DriveMode>();

    //Xbox controllers
    private static XboxController ctlDriver = new XboxController(0);
    private static XboxController ctlOperator = new XboxController(1);

    //Buttons
    private static final Button btnElevator_Bottom = new Button() {
        @Override public boolean get() { return ctlOperator.getAButton();}
    };

    private static final Button btnElevator_Low = new Button() {
        @Override public boolean get() { return ctlOperator.getBButton();}
    };

    private static final Button btnElevator_Middle = new Button() {
        @Override public boolean get() { return ctlOperator.getXButton();}
    };

    private static final Button btnElevator_High = new Button() {
        @Override public boolean get() { return ctlOperator.getYButton();}
    };

    private static final Button btnElevator_Up = new Button() {
        @Override public boolean get() { return ctlOperator.getPOV() == 0; }
    };

    private static final Button btnElevator_Down = new Button() {
        @Override public boolean get() { return ctlOperator.getPOV() == 180;}
    };

    private static final Button btnManipulator_Extend = new Button() {
        @Override public boolean get() { return ctlOperator.getRightTrigger();}
    };

    private static final Button btnManipulator_Retract = new Button() {
        @Override public boolean get() { return ctlOperator.getLeftTrigger();}
    };

    private static final Button btnManipulator_Open = new Button() {
        @Override public boolean get() { return ctlOperator.getRightBumper();}
    };

    private static final Button btnManipulator_Close = new Button() {
    @Override public boolean get() { return ctlOperator.getLeftBumper();}
    };

    private static final Button btnChassis_Precision = new Button() {
        @Override public boolean get() { return ctlDriver.getRightBumper();}
    };

    private static final Button btnChassis_Slow = new Button() {
        @Override public boolean get() { return ctlDriver.getLeftBumper();}
    };

    private static final Button btnChassis_Boost = new Button() {
        @Override public boolean get() { return ctlDriver.getRightTrigger();}
    };

    private static final Button btnChassis_Brake = new Button (){
        @Override public boolean get() { return ctlDriver.getLeftTrigger();}
    };

    //Buffer variables
    private static ChassisPower mSelectedChassisPower;
    private static DriveMode mSelectedDriveMode;

    /**
     * Prevents creation of new instance of Teleoperated
     */
    private Teleoperated(){}

    /**
     * Runs once at start of teleop, checks selected drive mode and power
     */
    public static void init() {
        mSelectedDriveMode = chsDriveMode.getSelected();
        mSelectedChassisPower = chsChassisPower.getSelected();

        btnElevator_Bottom.getPressed();
        btnElevator_Low.getPressed();
        btnElevator_Middle.getPressed();
        btnElevator_High.getPressed();

        Chassis.disablePIDs();
        Elevator.disablePIDs();
        Manipulator.closeGrip();
        Manipulator.retractArm();
    }

    /**
     * Add and push options to dashboard
     */
    public static void initDashboard() {
        chsChassisPower.addOption(ChassisPower.TORTOISE.label, ChassisPower.TORTOISE);
        chsChassisPower.addOption(ChassisPower.LOW.label, ChassisPower.LOW);
        chsChassisPower.addOption(ChassisPower.MEDIUM.label, ChassisPower.MEDIUM);
        chsChassisPower.addOption(ChassisPower.HIGH.label, ChassisPower.HIGH);

        chsChassisPower.setDefaultOption(ChassisPower.LOW.label, ChassisPower.LOW);

        SmartDashboard.putData("Period/Teleoperated/Chassis Power", chsChassisPower);
        
        chsDriveMode.addOption(DriveMode.ARCADE_DRIVE.label, DriveMode.ARCADE_DRIVE);
        chsDriveMode.addOption(DriveMode.CHEEZY_DRIVE.label, DriveMode.CHEEZY_DRIVE);
        chsDriveMode.addOption(DriveMode.TANK_DRIVE.label, DriveMode.TANK_DRIVE);

        chsDriveMode.setDefaultOption(DriveMode.ARCADE_DRIVE.label, DriveMode.ARCADE_DRIVE);

        SmartDashboard.putData("Period/Teleoperated/Drive Mode", chsDriveMode);
    }


    /**
     * Sets up tank drive style of chassis control to recieve and process inputs
     * @param powerLeft [-1.0 to 1.0] Power to left side of chassis
     * @param powerRight [-1.0 to 1.0] Power to right side of chassis
     */
    public static void setTankDrive(double powerLeft, double powerRight){
        //Chassis.setDrive(Math.signum(powerLeft) * (powerLeft * powerLeft), Math.signum(powerRight) * (powerRight * powerRight));
        Chassis.setDrive(powerLeft, powerRight);
    }

    /**
     * Sets up arcade or cheezy drive style of chassis control to recieve and process inputs
     * @param throttle [-1.0 to 1.0] Throttles. (Speed increase/decrease)
     * @param steering [-1.0 to 1.0] Steers. (Left/right)
     */
    public static void setArcadeDrive(double throttle, double steering){
        setTankDrive(throttle + steering, throttle - steering);
    }


    public static void periodic() {

        double speedMultiplier = mSelectedChassisPower.standard;

        //Chassis precision and boost buttons (precision slows for minor adjustments, boost accelerates for efficient travel)
        if (btnChassis_Precision.get()){
            speedMultiplier = mSelectedChassisPower.precision;
        } else if (btnChassis_Slow.get()){
            speedMultiplier = mSelectedChassisPower.slow;
        } else if (btnChassis_Boost.get()){
            speedMultiplier = mSelectedChassisPower.boost;
        }

        //Apply selected drive mode
        if (mSelectedDriveMode == DriveMode.ARCADE_DRIVE){
            setArcadeDrive(Math.signum(ctlDriver.getLeftY()) * (ctlDriver.getLeftY() * ctlDriver.getLeftY()) * speedMultiplier, (Math.signum(ctlDriver.getLeftX())) * Math.abs(ctlDriver.getLeftX() * ctlDriver.getLeftX() * ctlDriver.getLeftX()) * speedMultiplier);
        } else if (mSelectedDriveMode == DriveMode.CHEEZY_DRIVE){
            setArcadeDrive(Math.signum(ctlDriver.getLeftY()) * (ctlDriver.getLeftY() * ctlDriver.getLeftY()) * speedMultiplier, (Math.signum(ctlDriver.getRightX())) * Math.abs(ctlDriver.getRightX() * ctlDriver.getRightX()) * speedMultiplier);
        } else {
            setTankDrive(Math.signum(ctlDriver.getLeftY()) * (ctlDriver.getLeftY() * ctlDriver.getLeftY()) * speedMultiplier, (Math.signum(ctlDriver.getRightY())) * (ctlDriver.getRightY() * ctlDriver.getRightY()) * speedMultiplier);
        }

        if(btnChassis_Brake.get())
            Chassis.setDriveNeutralMode(NeutralMode.Brake);
        else
            Chassis.setDriveNeutralMode(NeutralMode.Coast/*, NeutralMode.Brake*/);

        //ELEVATOR
        //TEMPORARY NUMBERS!!!
        if(btnElevator_Up.get()){
            Elevator.disableHeightPID();
            Elevator.setLiftPower(0.20);
        } else if(btnElevator_Down.get()){
            Elevator.disableHeightPID();
            Elevator.setLiftPower(-0.20);
        } else if(btnElevator_Bottom.getPressed()){
            Elevator.goToHeight(Elevator.Height.BOTTOM);
        } else if(btnElevator_Low.getPressed()){
            Elevator.goToHeight(Elevator.Height.LOW);
        } else if(btnElevator_Middle.getPressed()){
            Elevator.goToHeight(Elevator.Height.MID);
        } else if(btnElevator_High.getPressed()){
            Elevator.goToHeight(Elevator.Height.HIGH);
        } else {
            Elevator.setLiftPower(0.0);
        }

        //MANIPULATOR
        if(btnManipulator_Extend.getPressed()){
            Manipulator.extendArm();
        } else if(btnManipulator_Retract.getPressed()){
            Manipulator.retractArm();
        }

        if(btnManipulator_Open.getPressed()){
            Manipulator.openGrip();
        } else if(btnManipulator_Close.getPressed()){
            Manipulator.closeGrip();
        }

        //Update subsystems
        Chassis.periodic();
        Elevator.periodic();
        Manipulator.periodic();
    }
}

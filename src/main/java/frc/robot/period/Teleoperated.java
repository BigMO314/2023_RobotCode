package frc.robot.period;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.molib.buttons.Button;
import frc.molib.hid.XboxController;
import frc.robot.Robot;
import frc.robot.subsystem.Chassis;
import frc.robot.subsystem.Elevator;
import frc.robot.subsystem.Manipulator;

public class Teleoperated {

    /**
     * Chassis power options (precision and boost values included)
     */
    enum ChassisPower{
        TORTOISE("Tortoise", 0.15, 0.20, 0.25),
        LOW("Low", 0.20, 0.35, 0.50),
        MEDIUM("Medium", 0.30, 0.65, 0.75),
        HIGH("High", 0.30, 0.90, 1.0);
        
        public final String label;
        public final double precision;
        public final double standard;
        public final double boost;
        private ChassisPower(String label, double precision, double standard, double boost){
            this.label = label;
            this.precision = precision;
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
    private static XboxController ctlDriver = new XboxController(1);
    private static XboxController ctlOperator = new XboxController(2);

    //Buttons
    private static final Button btnElevator_Top = new Button() {
        @Override public boolean get() { return super.get();/* conditions for true here, tbd */}
    };

    private static final Button btnElevator_Middle = new Button() {
        @Override public boolean get() { return super.get();/* conditions for true here,tbd */}
    };

    private static final Button btnElevator_Bottom = new Button() {
        @Override public boolean get() { return super.get();/* conditions for true here, tbd */}
    };

    private static final Button btnElevator_Up = new Button() {
        @Override public boolean get() { return super.get();/* conditions for true here, tbd */}
    };

    private static final Button btnElevator_Down = new Button() {
        @Override public boolean get() { return super.get();/* conditions for true here, tbd */}
    };

    private static final Button btnManipulator_Extend = new Button() {
        @Override public boolean get() { return super.get();/* conditions for true here, tbd */}
    };

    private static final Button btnManipulator_Open = new Button() {
        @Override public boolean get() { return super.get();/* conditions for true here, tbd */}
    };

    private static final Button btnChassis_Precision = new Button() {
        @Override public boolean get() { return ctlDriver.getRightBumper();}
    };

    private static final Button btnChassis_Boost = new Button() {
        @Override public boolean get() { return ctlDriver.getRightTrigger();}
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
    }

    /**
     * Add and push options to dashboard
     */
    public static void initDashboard() {
        chsChassisPower.addOption(ChassisPower.TORTOISE.label, ChassisPower.TORTOISE);
        chsChassisPower.addOption(ChassisPower.LOW.label, ChassisPower.LOW);
        chsChassisPower.addOption(ChassisPower.MEDIUM.label, ChassisPower.MEDIUM);
        chsChassisPower.setDefaultOption(ChassisPower.HIGH.label, ChassisPower.HIGH);

        SmartDashboard.putData("Period/Teleoperated/Chassis Power", chsChassisPower);
        
        chsDriveMode.addOption(DriveMode.ARCADE_DRIVE.label, DriveMode.ARCADE_DRIVE);
        chsDriveMode.addOption(DriveMode.CHEEZY_DRIVE.label, DriveMode.CHEEZY_DRIVE);
        chsDriveMode.setDefaultOption(DriveMode.TANK_DRIVE.label, DriveMode.TANK_DRIVE);

        SmartDashboard.putData("Period/Teleoperated/Drive Mode", chsDriveMode);
    }


    /**
     * Sets up tank drive style of chassis control to recieve and process inputs
     * @param powerLeft [-1.0 to 1.0] Power to left side of chassis
     * @param powerRight [-1.0 to 1.0] Power to right side of chassis
     */
    public static void setTankDrive(double powerLeft, double powerRight){
        Chassis.setDrive(powerLeft, powerRight);
    }

    /**
     * Sets up arcade or cheezy drive style of chassis control to recieve and process inputs
     * @param throttle [-1.0 to 1.0] Throttles. (Speed increase/decrease)
     * @param steering [-1.0 to 1.0] Steers. (Left/right)
     */
    public static void setArcadeDrive(double throttle, double steering){
        Chassis.setDrive(throttle + steering, throttle - steering);
    }


    public static void periodic() {

        double speedMultiplier = mSelectedChassisPower.standard;

        //Chassis precision and boost buttons (precision slows for minor adjustments, boost accelerates for efficient travel)
        if (btnChassis_Precision.get()){
            speedMultiplier = mSelectedChassisPower.precision;
        } else if (btnChassis_Boost.get()){
            speedMultiplier = mSelectedChassisPower.boost;
        }

        //Apply selected drive mode
        if (mSelectedDriveMode == DriveMode.ARCADE_DRIVE){
            setArcadeDrive(ctlDriver.getLeftY()*speedMultiplier, ctlDriver.getLeftX()*speedMultiplier);
        } else if (mSelectedDriveMode == DriveMode.CHEEZY_DRIVE){
            setArcadeDrive(ctlDriver.getLeftY()*speedMultiplier, ctlDriver.getRightX()*speedMultiplier);
        } else {
            setTankDrive(ctlDriver.getLeftY()*speedMultiplier, ctlDriver.getRightY()*speedMultiplier);
        }

        //ELEVATOR
        //TEMPORARY NUMBERS!!!
        if(btnElevator_Up.get()){
            Elevator.disableHeightPID();
            Elevator.setLiftPower(0.20);
        } else if(btnElevator_Down.get()){
            Elevator.disableHeightPID();
            Elevator.setLiftPower(0.20);
        } else if(btnElevator_Top.getPressed()){
            Elevator.goToHeight(Elevator.Height.HIGH);
        } else if(btnElevator_Middle.getPressed()){
            Elevator.goToHeight(Elevator.Height.MIDDLE);
        } else if(btnElevator_Bottom.getPressed()){
            Elevator.goToHeight(Elevator.Height.LOW);
        } else {
            Elevator.setLiftPower(0.0);
        }

        //MANIPULATOR
        if(btnManipulator_Extend.get()){
            Manipulator.extendArm();
        } else{
            Manipulator.retractArm();
        }

        if(btnManipulator_Open.getPressed()){
            if(Manipulator.isGripClosed()){
                Manipulator.openGrip();
            } else{
                Manipulator.closeGrip();
            }
        }

        //Update subsystems
        Chassis.periodic();
        Elevator.periodic();
        Manipulator.periodic();
    }
}

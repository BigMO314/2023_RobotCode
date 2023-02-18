package frc.robot.period;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.molib.hid.XboxController;
import frc.robot.Robot;
import frc.robot.subsystem.Chassis;

public class Teleoperated {
    enum ChassisPower{
        LOW("Low", 0.15),
        HIGH("High", 1.0);
        
        public final String label;
        public final double multiplier;
        private ChassisPower(String label, double multiplier){
            this.label = label;
            this.multiplier = multiplier;
        }

        @Override public String toString(){
            return label;
        }
    }

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

    //Xbox controller
    private static XboxController ctlDriver = new XboxController(0);

    private static ChassisPower mSelectedChassisPower;
    private static DriveMode mSelectedDriveMode;

    /**
     * Prevents creation of new instance of Teleoperated
     */
    private Teleoperated(){}

    //TODO: Comment here
    public static void init() {
        mSelectedDriveMode = chsDriveMode.getSelected();
        mSelectedChassisPower = chsChassisPower.getSelected();
    }

    //TODO: Comment here
    public static void initDashboard() {
        chsChassisPower.addOption(ChassisPower.LOW.label, ChassisPower.LOW);
        chsChassisPower.setDefaultOption(ChassisPower.HIGH.label, ChassisPower.HIGH);

        SmartDashboard.putData("Chassis Power", chsChassisPower);
        
        chsDriveMode.addOption(DriveMode.ARCADE_DRIVE.label, DriveMode.ARCADE_DRIVE);
        chsDriveMode.addOption(DriveMode.CHEEZY_DRIVE.label, DriveMode.CHEEZY_DRIVE);
        chsDriveMode.setDefaultOption(DriveMode.TANK_DRIVE.label, DriveMode.TANK_DRIVE);

        SmartDashboard.putData("Drive Mode", chsDriveMode);
    }


    /**
     * Sets up tank drive style of chassis control to recieve and process inputs
     * @param powerLeft Power to left side of chassis (-1.0 to 1.0)
     * @param powerRight Power to right side of chassis (-1.0 to 1.0)
     */
    public static void setTankDrive(double powerLeft, double powerRight){
        Chassis.setDrive(powerLeft, powerRight);
    }

    /**
     * Sets up arcade or cheezy drive style of chassis control to recieve and process inputs
     * @param throttle Throttles. (Speed increase/decrease) (-1.0 to 1.0)
     * @param steering Steers. (Left/right) (-1.0 to 1.0)
     */
    public static void setArcadeDrive(double throttle, double steering){
        Chassis.setDrive(throttle + steering, throttle - steering);
    }

    public static void periodic() {
        if (ctlDriver.getAButtonPressed()){
            Chassis.resetDistance();
        }
        //TODO: Interpret HID
        if (mSelectedDriveMode == DriveMode.ARCADE_DRIVE){
            setArcadeDrive(ctlDriver.getLeftY()*mSelectedChassisPower.multiplier, ctlDriver.getLeftX()*mSelectedChassisPower.multiplier);
        } else if (mSelectedDriveMode == DriveMode.CHEEZY_DRIVE){
            setArcadeDrive(ctlDriver.getLeftY()*mSelectedChassisPower.multiplier, ctlDriver.getRightX()*mSelectedChassisPower.multiplier);
        } else {
            setTankDrive(ctlDriver.getLeftY()*mSelectedChassisPower.multiplier, ctlDriver.getRightY()*mSelectedChassisPower.multiplier);
        }

        //TODO: Update Subsystems
        Chassis.periodic();
    }
}

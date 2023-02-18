package frc.robot.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.networktables.NetworkTable;
import frc.molib.dashboard.Entry;
import frc.molib.sensors.MagEncoder;
import frc.robot.Robot;

@SuppressWarnings("unused")
public class Chassis {

    //Create Network Table
    private static NetworkTable tblChassis = Robot.tblSubsystem.getSubTable("Chassis");
    //Create Dashboard Entries
    private static Entry<Double> entDrive_Distance = new Entry<Double>(tblChassis, "Drive Distance");

    //Create drive motors
    private static WPI_TalonFX mtrDrive_L1 = new WPI_TalonFX(0);
    private static WPI_TalonFX mtrDrive_L2 = new WPI_TalonFX(1);
    private static WPI_TalonFX mtrDrive_R1 = new WPI_TalonFX(2);
    private static WPI_TalonFX mtrDrive_R2 = new WPI_TalonFX(3);

    //Create Encoders to measure drive motor
    private static MagEncoder encDrive_L = new MagEncoder(mtrDrive_L1);
    private static MagEncoder encDrive_R = new MagEncoder(mtrDrive_R1);

    //TODO: Create Gyro to measure Chassis angle (skip)

    //Create buffer variables
    private static double mChassisPower_L = 0.0;
    private static double mChassisPower_R = 0.0;
    
    private Chassis(){}

    public static void init() {
        //sets motors to follow other motors
        mtrDrive_L2.follow(mtrDrive_L1);
        mtrDrive_R2.follow(mtrDrive_R1);

        //Configure motor inversions
        mtrDrive_L1.setInverted(true);
        mtrDrive_L2.setInverted(true);
        mtrDrive_R1.setInverted(false);
        mtrDrive_R2.setInverted(false);

        //TODO: Configure Encoders (skip(math))
        encDrive_L.configDistancePerPulse(((1.00/(5.8226))/2048.00)*(4.00*Math.PI));
        encDrive_L.reset();
    }

    public static void initDashboard() {
        
    }

    public static void pushDashboardValues() {
        //Update Dashborad Entries
        entDrive_Distance.set(encDrive_L.getDistance());
    }

    public static void resetDistance(){
        encDrive_L.reset();
    }

    public static double getDistance(){
        return encDrive_L.getDistance();
    }


    //Create a setDrive function
    public static void setDrive(double powerLeft, double powerRight){
        mChassisPower_L = powerLeft;
        mChassisPower_R = powerRight;
    }

    public static void periodic() {
        //Update components
        mtrDrive_L1.set(ControlMode.PercentOutput, mChassisPower_L);
        //mtrDrive_L2.set(ControlMode.PercentOutput, mChassisPower_L);
        mtrDrive_R1.set(ControlMode.PercentOutput, mChassisPower_R);
        //mtrDrive_R2.set(ControlMode.PercentOutput, mChassisPower_R);
    }
}

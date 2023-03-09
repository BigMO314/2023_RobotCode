package frc.robot.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import frc.molib.PIDController;
import frc.molib.dashboard.Entry;
import frc.molib.sensors.MagEncoder;
import frc.robot.Robot;

/**
 * The Chassis Subsystem
 * <p>
 * Sets up chassis motors, chassis PIDs, chassis gyros, and chassis sensors.
 * Components relating to chassis, manuvering chassis.
 * @author Tavares Mance
 * @author Celia Peters
 */
@SuppressWarnings("unused")
public class Chassis {

    //Create Network Table
    private static NetworkTable tblChassis = Robot.tblSubsystem.getSubTable("Chassis");

    //Create drive motors
    private static WPI_TalonFX mtrDrive_L1 = new WPI_TalonFX(1);
    private static WPI_TalonFX mtrDrive_L2 = new WPI_TalonFX(2);
    private static WPI_TalonFX mtrDrive_R1 = new WPI_TalonFX(3);
    private static WPI_TalonFX mtrDrive_R2 = new WPI_TalonFX(4);

    //Create Encoders to measure drive motor
    private static MagEncoder encDrive = new MagEncoder(mtrDrive_L1);
    private static ADXRS450_Gyro gyrDrive = new ADXRS450_Gyro();

    //PID Controllers
    private static PIDController pidDrive_Distance = new PIDController(0.0, 0.0, 0.0);
    private static PIDController pidDrive_Angle = new PIDController(0.0, 0.0, 0.0);

    //Dashboard Entries
    private static Entry<Double> entDrive_Distance = new Entry<Double>(tblChassis, "Drive Distance");
    private static Entry<Double> entDrive_Speed = new Entry<Double>(tblChassis, "Drive Speed");
    private static Entry<Double> entDrive_Angle = new Entry<Double>(tblChassis, "Drive Angle");

    //Buffer variables
    private static double mChassisPower_L = 0.0;
    private static double mChassisPower_R = 0.0;
    /** Do not make an instance of the chassis.  */
    private Chassis(){}

    /**  Sets up the motors, PIDs, and sensors.*/
    public static void init() {
        //sets motors to follow other motors
        mtrDrive_L2.follow(mtrDrive_L1);
        mtrDrive_R2.follow(mtrDrive_R1);

        //Configure motor inversions
        mtrDrive_L1.setInverted(true);
        mtrDrive_L2.setInverted(true);
        mtrDrive_R1.setInverted(false);
        mtrDrive_R2.setInverted(false);

        //Limits
        mtrDrive_L1.configReverseSoftLimitEnable(false);
        mtrDrive_L1.configForwardSoftLimitEnable(false);

        mtrDrive_L2.configReverseSoftLimitEnable(false);
        mtrDrive_L2.configForwardSoftLimitEnable(false);

        mtrDrive_R1.configReverseSoftLimitEnable(false);
        mtrDrive_R1.configForwardSoftLimitEnable(false);

        mtrDrive_R2.configReverseSoftLimitEnable(false);
        mtrDrive_R2.configForwardSoftLimitEnable(false);

        //Moter NeutralModes
        mtrDrive_L1.setNeutralMode(NeutralMode.Coast);
        mtrDrive_L2.setNeutralMode(NeutralMode.Coast);
        mtrDrive_R1.setNeutralMode(NeutralMode.Coast);
        mtrDrive_R2.setNeutralMode(NeutralMode.Coast);

        //Configure Encoders
        encDrive.configDistancePerPulse(((1.0000/(5.8226))/2048.0000)*(4.0000*Math.PI));
        encDrive.reset();

        //Configure PIDs
        pidDrive_Distance.setTolerance(0.25);
        pidDrive_Distance.configAtSetpointTime(0.125);
        pidDrive_Distance.configOutputRange(-1.0, 1.0);

        pidDrive_Angle.setTolerance(5.0);
        pidDrive_Angle.configAtSetpointTime(0.125);
        pidDrive_Angle.configOutputRange(-1.0, 1.0);

        //Calibtrate sensors
        gyrDrive.calibrate();

        //Reset sensors
        resetDistance();
        resetAngle();
    }

    /** Initializing dashboard options. */
    public static void initDashboard() {
        
    }

    /** Push new values to the dashboard options. */
    public static void pushDashboardValues() {
        //Update Dashboard Entries
        entDrive_Distance.set(encDrive.getDistance());
        entDrive_Speed.set(encDrive.getRate());
        entDrive_Angle.set(gyrDrive.getAngle());
    }

    /** Resetting the distance. */
    public static void resetDistance(){
        encDrive.reset();
    }
    
    /**
     * Reading from the encoder.
     * @return Distance is given in inches.
     */
    public static double getDistance(){
        return encDrive.getDistance();
    }

    /**
     * Reading from the encoder.
     * @return Speed is given in inches per second.
     */
    public static double getSpeed(){
        return encDrive.getRate();
    }

    /** Resetting the angle. */
    public static void resetAngle(){
        gyrDrive.reset();
    }

    /**
     * Reading from the encoder.
     * @return Angle is given in degree.
     */
    public static double getAngle() {
        return gyrDrive.getAngle();
    }

    /**
     * Set in which the NeutralMode the Drive motors should be in. 
     * @param mode Mode to set the motors to.
     */
    public static void setDriveNeutralMode (NeutralMode mode){
        mtrDrive_L1.setNeutralMode(mode);
        mtrDrive_L2.setNeutralMode(mode);
        mtrDrive_R1.setNeutralMode(mode);
        mtrDrive_R2.setNeutralMode(mode);
    }

    /**
     * Set the drive power to the left and right side of the chassis. 
     * @param powerLeft [-1.0, 1.0] Power to the left side of the chassis.
     * @param powerRight [-1.0, 1.0] Power to the right side of the chassis. 
     */
    public static void setDrive(double powerLeft, double powerRight){
        mChassisPower_L = powerLeft;
        mChassisPower_R = powerRight;
    }

    /** Drive power is disabled.  */
    public static void disableDrive() {
        setDrive(0.0, 0.0);
    }

    /**
     * PID control for the Drive Distance of the Chassis.
     * @param distance Target distance to drive. 
     */
    public static void goToDistance(double distance){
        pidDrive_Distance.setSetpoint(distance);
        pidDrive_Distance.enable();
    }

    /**
     * Read whatever or not the Drive Distance PID reached its distance. 
     * @return Target distance is reached. 
     */
    public static boolean isAtDistance(){
        return pidDrive_Distance.atSetpoint();
    }

    /**
     * Drive Distance PID has been disabled. 
     */
    public static void disableDistancePID(){
        pidDrive_Distance.disable();
    }

    /**
     * PID control for the Drive Angle Distance. 
     * @param angle Target angle to turn. 
     */
    public static void goToAngle(double angle){
        pidDrive_Angle.setSetpoint(angle);
        pidDrive_Angle.enable();
    }

    /**
     * Read whatever or no the Drive Angle PID reached its distance. 
     * @return Angle distance is reached. 
     */
    public static boolean isAtAngle(){
        return pidDrive_Angle.atSetpoint();
    }

    /**
     * Drive Angle PID is disabled. 
     */
    public static void disableAnglePID(){
        pidDrive_Angle.disable();
    }

    /**
     * PIDs on the chassis are disabled. 
     */
    public static void disablePIDs(){
        disableDistancePID();
        disableAnglePID();
    }

    /**
     * PIDs and and Drive motors are disabled. 
     */
    public static void disable(){
        disablePIDs();
        disableDrive();
    }

    /**
     * Systems and components are updated. 
     */
    public static void periodic() {
        //PID Override
        if(pidDrive_Distance.isEnabled()){
            setDrive(pidDrive_Distance.calculate(getDistance()), pidDrive_Distance.calculate(getDistance()));
        } else if(pidDrive_Angle.isEnabled()){
            setDrive(pidDrive_Angle.calculate(getAngle()), -pidDrive_Angle.calculate(getAngle()));
        }
        
        //Update components
        mtrDrive_L1.set(ControlMode.PercentOutput, mChassisPower_L);
        //mtrDrive_L2.set(ControlMode.PercentOutput, mChassisPower_L);
        mtrDrive_R1.set(ControlMode.PercentOutput, mChassisPower_R);
        //mtrDrive_R2.set(ControlMode.PercentOutput, mChassisPower_R);
    }
}

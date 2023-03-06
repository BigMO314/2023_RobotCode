package frc.robot.period;

import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.molib.utilities.Console;
import frc.robot.Robot;
import frc.robot.subsystem.Chassis;
import frc.robot.subsystem.Elevator;
import frc.robot.subsystem.Manipulator;

/**
 * The Autonomous Period
 * <p>
 * Reads options from the Dashboard to run various pre-programmed sequences
 * at the start of the match, free of driver input.
 * @author Celia Peters
 * @author Tavares Mance
 */
@SuppressWarnings("unused")
public class Autonomous {
    /** Options for starting position */
    private enum StartingPosition{
        WALL("Wall Side"),
        CHARGE_STATION("Charge Station"),
        LOADING("Loading Side");

        private final String label;
        private StartingPosition(String label){
            this.label = label;
        }
        @Override public String toString(){
            return label;
        }
    }

    /**
     * Options for auton sequences
     */
    private enum Sequence{
        /** Do absolutely nothing. do not move or score. Just sit there and look pretty. */
        NOTHING("Nothing"){
            @Override public void run(){
                switch(mStage){
                    case 0:
                        Console.logMsg("Starting Sequence \"" + Sequence.NOTHING.toString() + "\"");
                        mStage++;
                        break;
                    case 1:
                        Console.logMsg("Sequence Complete \"" + Sequence.NOTHING.toString() + "\"");
                        mStage++;
                        break;
                    default:
                        //Disable all Subsystems.
                        Chassis.disable();
                        Elevator.disable();
                        Manipulator.disable();
                }

            }
        },
        JUST_DRIVE("Just Drive"){
            @Override public void run(){
                switch(mStartingPosition){
                    case WALL:
                        switch(mStage){
                            case 0:
                                Console.logMsg("Starting Sequence \"" + Sequence.JUST_DRIVE.toString() + "\" - " + StartingPosition.WALL.toString());
                                mStage++;
                                break;
                            case 1:
                                Console.logMsg("Starting drive forward... \"");
                                Chassis.setDrive(0.25, 0.25);
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 2:
                                if(tmrStageTimeOut.get() > 2.0) mStage++;                             
                                break;
                            case 3:
                                Console.logMsg("Time reached. Stopping drive... \"");
                                Chassis.disable();
                                mStage++;
                                break;
                            case 4:
                                Console.logMsg("Sequence Complete \"" + Sequence.JUST_DRIVE.toString() + "\" - " + StartingPosition.WALL.toString());
                                mStage++;
                                break;
                            default:
                                //Disable all Subsystems.
                                Chassis.disable();
                                Elevator.disable();
                                Manipulator.disable();
                    }
                        break;
                    case CHARGE_STATION:
                        switch(mStage){
                            case 0:
                                Console.logMsg("Starting Sequence \"" + Sequence.JUST_DRIVE.toString() + "\" - " + StartingPosition.CHARGE_STATION.toString());
                                mStage++;
                                break;
                            case 1:
                                Console.logMsg("Starting drive forward... \"");
                                Chassis.setDrive(0.25, 0.25);
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 2:
                                if(tmrStageTimeOut.get() > 2.0) mStage++;                             
                                break;
                            case 3:
                                Console.logMsg("Time reached. Stopping drive... \"");
                                Chassis.disable();
                                mStage++;
                                break;
                            case 4:
                                Console.logMsg("Sequence Complete \"" + Sequence.JUST_DRIVE.toString() + "\" - " + StartingPosition.CHARGE_STATION.toString());
                                mStage++;
                                break;
                            default:
                                //Disable all Subsystems.
                                Chassis.disable();
                                Elevator.disable();
                                Manipulator.disable();
                    }
                        break;

                    case LOADING:
                        switch(mStage){
                            case 0:
                                Console.logMsg("Starting Sequence \"" + Sequence.JUST_DRIVE.toString() + "\" - " + StartingPosition.LOADING.toString());
                                mStage++;
                                break;
                            case 1:
                                Console.logMsg("Starting drive forward... \"");
                                Chassis.setDrive(0.25, 0.25);
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 2:
                                if(tmrStageTimeOut.get() > 1.0) mStage++;                             
                                break;
                            case 3:
                                Console.logMsg("Time reached. Stopping drive... \"");
                                Chassis.disable();
                                mStage++;
                                break;
                            case 4:
                                Console.logMsg("Sequence Complete \"" + Sequence.JUST_DRIVE.toString() + "\" - " + StartingPosition.LOADING.toString());
                                mStage++;
                                break;
                            default:
                                //Disable all Subsystems.
                                Chassis.disable();
                                Elevator.disable();
                                Manipulator.disable();
                    }
                        break;
                    default:
                        //Disable all Subsystems.
                        Chassis.disable();
                        Elevator.disable();
                        Manipulator.disable();
                }
            }
        },
        SIMPLE_SCORE("Simple Score"){
            @Override public void run(){

            }
        };

        //Timers
        public static final Timer tmrStageTimeOut = new Timer();

        //Static Variables 
        private static int mStage = 0;
        
        private final String label;
        private Sequence(String label){
            this.label = label;
        }
        @Override public String toString(){
            return label;
        }

        /**Initialization of the sequence. Overide if any additional actions are needed. */
        public void init(){
            mStage = 0;
            tmrStageTimeOut.start();
            tmrStageTimeOut.reset();
        }

        /** The actual code for running the auton sequence. */
        public abstract void run();
    }


    
    //Network Table
    private static final NetworkTable tblAutonomous = Robot.tblPeriod.getSubTable("Autonomous");

    //Dashboard Entries
    private static SendableChooser<StartingPosition> chsStartingPosition = new SendableChooser<StartingPosition>();
    private static SendableChooser<Sequence> chsSequence = new SendableChooser<Sequence>();

    //Components
    private static StartingPosition mStartingPosition;
    private static Sequence mSequence;

    /** Prevent instances of autonomous class */
    private Autonomous(){}

    /**
     * Runs once at start of autonomous program, pulls starting position 
     * and sequence from those selected on dashboard
     */
    public static void init() {
        mStartingPosition = chsStartingPosition.getSelected();
        mSequence = chsSequence.getSelected();

        mSequence.init();

        Chassis.setDriveNeutralMode(NeutralMode.Brake);
    }

    /**
     * Create options and push dropdowns to dashboard
     */
    public static void initDashboard() {
        //Push dropdowns to dashboard
        chsStartingPosition.addOption(StartingPosition.LOADING.label, StartingPosition.LOADING);
        chsStartingPosition.addOption(StartingPosition.CHARGE_STATION.label, StartingPosition.CHARGE_STATION);
        chsStartingPosition.setDefaultOption(StartingPosition.WALL.label, StartingPosition.WALL);
        
        SmartDashboard.putData("Period/Autonomous/Starting Position", chsStartingPosition);

        chsSequence.addOption(Sequence.SIMPLE_SCORE.label, Sequence.SIMPLE_SCORE);
        chsSequence.addOption(Sequence.JUST_DRIVE.label, Sequence.JUST_DRIVE);
        chsSequence.setDefaultOption(Sequence.NOTHING.label, Sequence.NOTHING);
        
        SmartDashboard.putData("Period/Autonomous/Sequence", chsSequence);
    }

     /**
     * Runs on a periodic loop that updates values and runs functions
     * according to human input, autonomus input, and game conditions.
     */
    public static void periodic() {
        mSequence.run();

        Chassis.periodic();
        Elevator.periodic();
        Manipulator.periodic();
    }
}

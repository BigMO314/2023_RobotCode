package frc.robot.period;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.molib.utilities.Console;
import frc.robot.Robot;
import frc.robot.subsystem.Chassis;
import frc.robot.subsystem.Elevator;
import frc.robot.subsystem.Manipulator;

public class Autonomous {
    /**
     * Options for starting position
     */
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
                        Console.logMsg("Starting Sequence \"" + Sequence.NOTHING.toString() + "\"");
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
        public static void init(){
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

    public static void periodic() {
        //TODO: Update subsystems
    }
}

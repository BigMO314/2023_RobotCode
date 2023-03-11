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
        SPIN("Spin"){
            @Override public void run(){
                switch(mStage){
                    case 0:
                        Console.logMsg("Starting Sequence \"" + Sequence.SPIN.toString() + "\" - " + mStartingPosition.toString());
                        mStage++;
                        break;
                    case 1:
                        Console.logMsg("Starting clockwise spin... \"");
                        Chassis.setDrive(0.35, -0.35);
                        tmrStageTimeOut.reset();
                        mStage++;
                        break;
                    case 2:
                        if(tmrStageTimeOut.get() > 0.75) mStage++;                             
                        break;
                    case 3:
                        Console.logMsg("Time reached. Stopping drive... \"");
                        Chassis.disable();
                        mStage++;
                        break;
                    case 4:
                        Console.logMsg("Sequence Complete \"" + Sequence.SPIN.toString() + "\" - " + mStartingPosition.toString());
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
                switch(mStage){
                    case 0:
                        Console.logMsg("Starting Sequence \"" + Sequence.JUST_DRIVE.toString() + "\" - " + mStartingPosition.toString());
                        mStage++;
                        break;
                    case 1:
                        Console.logMsg("Starting drive backward... \"");
                        if(mStartingPosition != StartingPosition.CHARGE_STATION)
                            Chassis.setDrive(-0.35, -0.35);
                        tmrStageTimeOut.reset();
                        mStage++;
                        break;
                    case 2:
                        if(tmrStageTimeOut.get() > 2.5) mStage++;                             
                        break;
                    case 3:
                        Console.logMsg("Time reached. Stopping drive... \"");
                        Chassis.disable();
                        mStage++;
                        break;
                    case 4:
                        Console.logMsg("Sequence Complete \"" + Sequence.JUST_DRIVE.toString() + "\" - " + mStartingPosition.toString());
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
        LOW_SCORE("Low Score"){
            @Override public void run(){
                switch(mStage){
                    case 0:
                        Console.logMsg("Starting Sequence \"" + Sequence.LOW_SCORE.toString() + "\" - " + mStartingPosition.toString());
                        mStage++;
                        break;
                    case 1:
                        Console.logMsg("Extending arm... \"");
                        Manipulator.extendArm();
                        tmrStageTimeOut.reset();
                        mStage++;
                        break;
                    case 2:
                        if(tmrStageTimeOut.get() > 1.0) mStage++;
                        break;
                    case 3:
                        Console.logMsg("Opening claw and retracting arm... \"");
                        Manipulator.retractArm();
                        Manipulator.openGrip();
                        tmrStageTimeOut.reset();
                        mStage++;
                    case 4:
                        if(tmrStageTimeOut.get() > 1.5) mStage++;
                        break;
                    case 5:
                        Console.logMsg("Starting drive backwards and closing grip... \"");
                        if(mStartingPosition != StartingPosition.CHARGE_STATION)
                            Chassis.setDrive(-0.35, -0.35);
                        Manipulator.closeGrip();
                        tmrStageTimeOut.reset();
                        mStage++;
                        break;
                    case 6:
                        if(tmrStageTimeOut.get() > 2.5) mStage++;                             
                        break;
                    case 7:
                        Console.logMsg("Time reached. Stopping drive... \"");
                        Chassis.disable();
                        mStage++;
                        break;
                    case 8:
                        Console.logMsg("Sequence Complete \"" + Sequence.LOW_SCORE.toString() + "\" - " + mStartingPosition.toString());
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
        HIGH_SCORE("High Score"){
            @Override public void run(){
                switch(mStage){
                    case 0:
                        Console.logMsg("Starting Sequence \"" + Sequence.HIGH_SCORE.toString() + "\" - " + mStartingPosition.toString());
                        mStage++;
                        break;
                    case 1:
                        Console.logMsg("Elevator to high position... \"");
                        Elevator.goToHeight(Elevator.Height.HIGH);
                        tmrStageTimeOut.reset();
                        mStage++;
                        break;
                    case 2:
                        if(Elevator.isAtHeight() || tmrStageTimeOut.get() > 1.5) mStage++;
                        break;
                    case 3:
                        Console.logMsg("Extending arm... \"");
                        Manipulator.extendArm();
                        tmrStageTimeOut.reset();
                        mStage++;
                        break;
                    case 4:
                        if(tmrStageTimeOut.get() > 1.0) mStage++;
                        break;
                    case 5:
                        Console.logMsg("Opening claw and retracting arm... \"");
                        Manipulator.retractArm();
                        Manipulator.openGrip();
                        tmrStageTimeOut.reset();
                        mStage++;
                    case 6:
                        if(tmrStageTimeOut.get() > 1.5) mStage++;
                        break;
                    case 7:
                        Console.logMsg("Returning elevator to bottom... \"");
                        Elevator.goToHeight(Elevator.Height.BOTTOM);
                        tmrStageTimeOut.reset();
                        mStage++;
                    case 8:
                        if(Elevator.isLiftAtBottom() || tmrStageTimeOut.get() > 2.0) mStage++;
                        break;
                    case 9:
                        Console.logMsg("Starting drive backwards and closing grip... \"");
                        if(mStartingPosition != StartingPosition.CHARGE_STATION)
                            Chassis.setDrive(-0.35, -0.35);
                        Manipulator.closeGrip();
                        tmrStageTimeOut.reset();
                        mStage++;
                        break;
                    case 10:
                        if(tmrStageTimeOut.get() > 2.5) mStage++;                             
                        break;
                    case 11:
                        Console.logMsg("Time reached. Stopping drive... \"");
                        Chassis.disable();
                        mStage++;
                        break;
                    case 12:
                        Console.logMsg("Starting counterclockwise spin... \"");
                        Chassis.setDrive(-0.35, 0.35);
                        tmrStageTimeOut.reset();
                        mStage++;
                        break;
                    case 13:
                        if(tmrStageTimeOut.get() > 0.75) mStage++;                             
                        break;
                    case 14:
                        Console.logMsg("Time reached. Stopping drive... \"");
                        Chassis.disable();
                        mStage++;
                        break;
                    case 15:
                        Console.logMsg("Sequence Complete \"" + Sequence.HIGH_SCORE.toString() + "\" - " + mStartingPosition.toString());
                        mStage++;
                        break;
                    default:
                        //Disable all Subsystems.
                        Chassis.disable();
                        Elevator.disable();
                        Manipulator.disable();
                }
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

        Chassis.disablePIDs();
        Elevator.disablePIDs();
        Manipulator.closeGrip();
        Manipulator.retractArm();
    }

    /**
     * Create options and push dropdowns to dashboard
     */
    public static void initDashboard() {
        //Push dropdowns to dashboard
        chsStartingPosition.addOption(StartingPosition.LOADING.label, StartingPosition.LOADING);
        chsStartingPosition.addOption(StartingPosition.CHARGE_STATION.label, StartingPosition.CHARGE_STATION);
        chsStartingPosition.addOption(StartingPosition.WALL.label, StartingPosition.WALL);
        
        chsStartingPosition.setDefaultOption(StartingPosition.WALL.label, StartingPosition.WALL);
        
        SmartDashboard.putData("Period/Autonomous/Starting Position", chsStartingPosition);

        chsSequence.addOption(Sequence.NOTHING.label, Sequence.NOTHING);
        chsSequence.addOption(Sequence.SPIN.label, Sequence.SPIN);
        chsSequence.addOption(Sequence.LOW_SCORE.label, Sequence.LOW_SCORE);
        chsSequence.addOption(Sequence.HIGH_SCORE.label, Sequence.HIGH_SCORE);
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

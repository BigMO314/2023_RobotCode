package frc.robot.period;

import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
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
        /** Do absolutely nothing. Do not move or score. Just sit there and look pretty. */
        NOTHING("Do Nothing"){
            @Override public void run() {
                switch(mStage){
                    case 0:
                        Chassis.disable();
                        Elevator.disable();
                        Manipulator.disable();
                        mStage++;
                        break;
                    default:
                        Chassis.disable();
                        Elevator.disable();
                        Manipulator.disable();
                }
            }
        },
        JUST_DRIVE("Just Drive"){
            @Override public void run() {
                switch(mStartingPosition){
                    case WALL:
                        switch(mStage){
                            case 0:
                                Console.logMsg("Starting Sequence \"" + Sequence.JUST_DRIVE.toString() + "\" - " + StartingPosition.WALL.toString());
                                mStage++;
                                break;
                            case 1:
                                Console.logMsg("Starting drive backward...");
                                Chassis.goToDistance(mWallReverseDistance);
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 2:
                                if(Chassis.isAtDistance() || tmrStageTimeOut.get() > 2.5) mStage++;                             
                                break;
                            case 3:
                                Console.logMsg("Time reached. Stopping drive...");
                                Chassis.disable();
                                mStage++;
                                break;
                            case 4:
                                Console.logMsg("Sequence Complete \"" + Sequence.JUST_DRIVE.toString() + "\" - " + StartingPosition.WALL.toString());
                                mStage++;
                                break;
                            default:
                                Chassis.disable();
                                Elevator.disable();
                                Manipulator.disable();
                                } break;
                    case CHARGE_STATION:
                        switch(mStage){
                            case 0:
                                Console.logMsg("Starting Sequence \"" + Sequence.JUST_DRIVE.toString() + "\" - " + StartingPosition.CHARGE_STATION.toString());
                                mStage++;
                                break;
                            case 1:
                                Console.logMsg("Waiting...");
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 2:
                            //FIXME: Make this an actual time
                                if(tmrStageTimeOut.get() > 0.0){
                                    mStage++;
                                };
                                break;
                            case 3:
                                Console.logMsg("Starting drive backward...");
                                Chassis.goToDistance(mChargeBalanceDistance);
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 4:
                                if(Chassis.isAtDistance() || tmrStageTimeOut.get() > 2.5) mStage++;                             
                                break;
                            case 5:
                                Console.logMsg("Sequence Complete \"" + Sequence.JUST_DRIVE.toString() + "\" - " + StartingPosition.CHARGE_STATION.toString());
                                mStage++;
                                break;
                            default:
                                Elevator.disable();
                                Manipulator.disable();
                        } break;
                    case LOADING:
                        switch(mStage){
                            case 0:
                                Console.logMsg("Starting Sequence \"" + Sequence.JUST_DRIVE.toString() + "\" - " + StartingPosition.LOADING.toString());
                                mStage++;
                                break;
                            case 1:
                                Console.logMsg("Starting drive backward...");
                                Chassis.goToDistance(mLoadingReverseDistance);
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 2:
                                if(Chassis.isAtDistance() || tmrStageTimeOut.get() > 2.5) mStage++;                             
                                break;
                            case 3:
                                Console.logMsg("Time reached. Stopping drive...");
                                Chassis.disable();
                                mStage++;
                                break;
                            case 4:
                                Console.logMsg("Sequence Complete \"" + Sequence.JUST_DRIVE.toString() + "\" - " + StartingPosition.LOADING.toString());
                                mStage++;
                                break;
                            default:
                                Chassis.disable();
                                Elevator.disable();
                                Manipulator.disable();
                        } break;
                    default:
                        Chassis.disable();
                        Elevator.disable();
                        Manipulator.disable();
                }
            }
        },
        LOW_SCORE("Low Score"){
            @Override public void run() {
                switch(mStartingPosition){
                    case WALL:
                        switch(mStage){
                            case 0:
                                Console.logMsg("Starting Sequence \"" + Sequence.LOW_SCORE.toString() + "\" - " + StartingPosition.WALL.toString());
                                mStage++;
                                break;
                            case 1:
                                Console.logMsg("Extending arm...");
                                Manipulator.extendArm();
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 2:
                                if(tmrStageTimeOut.get() > 1.0) mStage++;
                                break;
                            case 3:
                                Console.logMsg("Opening claw...");
                                Manipulator.openGrip();
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 4:
                                if(tmrStageTimeOut.get() > 1.0) mStage++;
                                break;
                            case 5:
                                Console.logMsg("Retracting arm...");
                                Manipulator.retractArm();
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 6:
                                if(tmrStageTimeOut.get() > 1.0) mStage++;
                                break;
                            case 7:
                                Console.logMsg("Starting drive backwards...");
                                Chassis.goToDistance(mWallReverseDistance);
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 8:
                                if(Chassis.isAtDistance() || tmrStageTimeOut.get() > 3.0) mStage++;                             
                                break;
                            case 9:
                                Console.logMsg("Distance reached. Starting turn...");
                                if (mAlliance == Alliance.Red){
                                    Chassis.goToAngle(-168.00);
                                } else {
                                    Chassis.goToAngle(168.00);
                                }
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 10:
                                if(Chassis.isAtAngle() || tmrStageTimeOut.get() > 3.0) mStage++;                             
                                break;
                            case 11:
                                Console.logMsg("Angle reached. Extending arm...");
                                Manipulator.extendArm();
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 12:
                                if(tmrStageTimeOut.get() > 1.0) mStage++;                             
                                break;
                            case 13:
                                Console.logMsg("Starting drive forward...");
                                Chassis.goToDistance(24.00);
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 14:
                                if(Chassis.isAtDistance() || tmrStageTimeOut.get() > 1.0) mStage++;                             
                                break;
                            case 15:
                                Console.logMsg("Distance reached. Stopping drive...");
                                Chassis.disable();
                                mStage++;
                                break;
                            case 16:
                                Console.logMsg("Sequence Complete \"" + Sequence.LOW_SCORE.toString() + "\" - " + StartingPosition.WALL.toString());
                                mStage++;
                                break;
                            default:
                                Chassis.disable();
                                Elevator.disable();
                                Manipulator.disable();
                        } break;
                    case CHARGE_STATION:
                        switch(mStage){
                            case 0:
                                Console.logMsg("Starting Sequence \"" + Sequence.LOW_SCORE.toString() + "\" - " + StartingPosition.CHARGE_STATION.toString());
                                mStage++;
                                break;
                            case 1:
                                Console.logMsg("Extending arm...");
                                Manipulator.extendArm();
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 2:
                                if(tmrStageTimeOut.get() > 1.0) mStage++;
                                break;
                            case 3:
                                Console.logMsg("Opening claw...");
                                Manipulator.openGrip();
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 4:
                                if(tmrStageTimeOut.get() > 1.0) mStage++;
                                break;
                            case 5:
                                Console.logMsg("Retracting arm...");
                                Manipulator.retractArm();
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 6:
                                if(tmrStageTimeOut.get() > 1.0) mStage++;
                                break;
                            case 7:
                                Console.logMsg("Waiting...");
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 8:
                            //FIXME: Make this an actual time
                                if(tmrStageTimeOut.get() > 0.0){
                                    mStage++;
                                };
                                break;
                            case 9:
                                Console.logMsg("Starting drive backwards to park...");
                                Chassis.goToDistance(mChargeBalanceDistance);
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 10:
                                if(Chassis.isAtDistance() || tmrStageTimeOut.get() > 5.0) mStage++;                             
                                break;
                            case 11:
                                Console.logMsg("Sequence Complete \"" + Sequence.LOW_SCORE.toString() + "\" - " + StartingPosition.CHARGE_STATION.toString());
                                mStage++;
                                break;
                            default:
                                Elevator.disable();
                                Manipulator.disable();
                        } break;
                    case LOADING:
                        switch(mStage){
                            case 0:
                                Console.logMsg("Starting Sequence \"" + Sequence.LOW_SCORE.toString() + "\" - " + StartingPosition.LOADING.toString());
                                mStage++;
                                break;
                            case 1:
                                Console.logMsg("Extending arm...");
                                Manipulator.extendArm();
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 2:
                                if(tmrStageTimeOut.get() > 1.0) mStage++;
                                break;
                            case 3:
                                Console.logMsg("Opening claw...");
                                Manipulator.openGrip();
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 4:
                                if(tmrStageTimeOut.get() > 1.0) mStage++;
                                break;
                            case 5:
                                Console.logMsg("Retracting arm...");
                                Manipulator.retractArm();
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 6:
                                if(tmrStageTimeOut.get() > 1.0) mStage++;
                                break;
                            case 7:
                                Console.logMsg("Starting drive backwards...");
                                Chassis.goToDistance(mLoadingReverseDistance);
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 8:
                                if(Chassis.isAtDistance() || tmrStageTimeOut.get() > 3.0) mStage++;                             
                                break;
                            case 9:
                                Console.logMsg("Distance reached. Starting turn...");
                                if (mAlliance == Alliance.Red){
                                    Chassis.goToAngle(168.00);
                                } else {
                                    Chassis.goToAngle(-168.00);
                                }
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 10:
                                if(Chassis.isAtAngle() || tmrStageTimeOut.get() > 3.0) mStage++;                             
                                break;
                            case 11:
                                Console.logMsg("Angle reached. Extending arm...");
                                Manipulator.extendArm();
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 12:
                                if(tmrStageTimeOut.get() > 1.0) mStage++;                             
                                break;
                            case 13:
                                Console.logMsg("Starting drive forward...");
                                Chassis.goToDistance(24.00);
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 14:
                                if(Chassis.isAtDistance() || tmrStageTimeOut.get() > 1.0) mStage++;                             
                                break;
                            case 15:
                                Console.logMsg("Distance reached. Stopping drive...");
                                Chassis.disable();
                                mStage++;
                                break;
                            case 16:
                                Console.logMsg("Sequence Complete \"" + Sequence.LOW_SCORE.toString() + "\" - " + StartingPosition.LOADING.toString());
                                mStage++;
                                break;
                            default:
                                Chassis.disable();
                                Elevator.disable();
                                Manipulator.disable();
                        } break;
                    default:
                        Chassis.disable();
                        Elevator.disable();
                        Manipulator.disable();
                }
            }
        },
        HIGH_SCORE_CUBE("High Score Cube"){
            @Override public void run() {
                switch(mStartingPosition){
                    case WALL:
                        switch(mStage){
                            case 0:
                                Console.logMsg("Starting Sequence \"" + Sequence.HIGH_SCORE_CUBE.toString() + "\" - " + StartingPosition.WALL.toString());
                                mStage++;
                                break;
                            case 1:
                                Console.logMsg("Elevator to high position...");
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
                                Console.logMsg("Starting drive backwards, opening claw, retracting arm...");
                                Chassis.resetDistance();
                                Manipulator.retractArm();
                                Manipulator.openGrip();
                                Chassis.setDrive(-.15, -.15);
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 6:
                                if(tmrStageTimeOut.get() > .5) mStage++;                             
                                break;
                            case 7:
                                Console.logMsg("Returning elevator to bottom... \"");
                                Elevator.goToHeight(Elevator.Height.BOTTOM);
                                Chassis.disable();
                                tmrStageTimeOut.reset();
                                mStage++;
                            case 8:
                                if(Elevator.isLiftAtBottom() || tmrStageTimeOut.get() > 2.0) mStage++;
                                break;
                            case 9:
                                Console.logMsg("Starting drive backwards and closing grip...");
                                Chassis.goToDistance(mWallReverseDistance - Chassis.getDistance());
                                Manipulator.closeGrip();
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 10:
                                if(Chassis.isAtDistance() || tmrStageTimeOut.get() > 3.0) mStage++;                             
                                break;
                            case 11:
                                Console.logMsg("Distance reached. Starting turn...");
                                if (mAlliance == Alliance.Red){
                                    Chassis.goToAngle(180.00);
                                } else {
                                    Chassis.goToAngle(-180.00);
                                }
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 12:
                                if(Chassis.isAtAngle() || tmrStageTimeOut.get() > 3.0) mStage++;                             
                                break;
                            case 13:
                                Console.logMsg("Angle reached. Opening claw and extending arm...");
                                Manipulator.openGrip();
                                Manipulator.extendArm();
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 14:
                                if(tmrStageTimeOut.get() > 1.0) mStage++;                             
                                break;
                            case 15:
                                Console.logMsg("Starting drive forward...");
                                Chassis.goToDistance(24.00);
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 16:
                                if(Chassis.isAtDistance() || tmrStageTimeOut.get() > 1.0) mStage++;                             
                                break;
                            case 17:
                                Console.logMsg("Distance reached. Stopping drive...");
                                Chassis.disable();
                                mStage++;
                                break;
                            case 18:
                                Console.logMsg("Sequence Complete \"" + Sequence.HIGH_SCORE_CUBE.toString() + "\" - " + StartingPosition.WALL.toString());
                                mStage++;
                                break;
                            default:
                                Chassis.disable();
                                Elevator.disable();
                                Manipulator.disable();
                        } break;
                    case CHARGE_STATION:
                        switch(mStage){
                            case 0:
                                Console.logMsg("Starting Sequence \"" + Sequence.HIGH_SCORE_CUBE.toString() + "\" - " + StartingPosition.CHARGE_STATION.toString());
                                mStage++;
                                break;
                            case 1:
                                Console.logMsg("Elevator to high position...");
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
                                Console.logMsg("Starting drive backwards, opening claw, retracting arm...");
                                Chassis.resetDistance();
                                Manipulator.retractArm();
                                Manipulator.openGrip();
                                Chassis.setDrive(0.0, 0.0);
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 6:
                                if(tmrStageTimeOut.get() > .5) mStage++;                             
                                break;
                            case 7:
                                Console.logMsg("Returning elevator to bottom... \"");
                                Elevator.goToHeight(Elevator.Height.BOTTOM);
                                Chassis.disable();
                                tmrStageTimeOut.reset();
                                mStage++;
                            case 8:
                                if(Elevator.isLiftAtBottom() || tmrStageTimeOut.get() > 2.0) mStage++;
                                break;
                            case 9:
                                Console.logMsg("Waiting...");
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 10:
                            //FIXME: Make this an actual time
                                if(tmrStageTimeOut.get() > 4.0){
                                    mStage++;
                                };
                                break;
                            case 11:
                                Console.logMsg("Starting drive backwards to park and closing grip...");
                                if(DriverStation.getAlliance() == DriverStation.Alliance.Red){
                                    Chassis.goToDistance(mChargeBalanceDistance - Chassis.getDistance() + 3.00);
                                } else {
                                    Chassis.goToDistance(mChargeBalanceDistance - Chassis.getDistance());
                                }
                                Manipulator.closeGrip();
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 12:
                                if(Chassis.isAtDistance() || tmrStageTimeOut.get() > 5.0) mStage++;                             
                                break;
                            case 13:
                                Console.logMsg("Sequence Complete \"" + Sequence.HIGH_SCORE_CUBE.toString() + "\" - " + StartingPosition.CHARGE_STATION.toString());
                                mStage++;
                                break;
                            default:
                                Elevator.disable();
                                Manipulator.disable();
                        } break;
                    case LOADING:
                        switch(mStage){
                            case 0:
                                Console.logMsg("Starting Sequence \"" + Sequence.HIGH_SCORE_CUBE.toString() + "\" - " + StartingPosition.LOADING.toString());
                                mStage++;
                                break;
                            case 1:
                                Console.logMsg("Elevator to high position...");
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
                                Console.logMsg("Starting drive backwards, opening claw, retracting arm...");
                                Chassis.resetDistance();
                                Manipulator.retractArm();
                                Manipulator.openGrip();
                                Chassis.setDrive(-.15, -.15);
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 6:
                                if(tmrStageTimeOut.get() > .5) mStage++;                             
                                break;
                            case 7:
                                Console.logMsg("Returning elevator to bottom... \"");
                                Elevator.goToHeight(Elevator.Height.BOTTOM);
                                Chassis.disable();
                                tmrStageTimeOut.reset();
                                mStage++;
                            case 8:
                                if(Elevator.isLiftAtBottom() || tmrStageTimeOut.get() > 2.0) mStage++;
                                break;
                            case 9:
                                Console.logMsg("Starting drive backwards and closing grip...");
                                Chassis.goToDistance(mLoadingReverseDistance - Chassis.getDistance());
                                Manipulator.closeGrip();
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 10:
                                if(Chassis.isAtDistance() || tmrStageTimeOut.get() > 3.0) mStage++;                             
                                break;
                            case 11:
                                Console.logMsg("Distance reached. Starting turn...");
                                if (mAlliance == Alliance.Red){
                                    Chassis.goToAngle(180.00);
                                } else {
                                    Chassis.goToAngle(-180.00);
                                }
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 12:
                                if(Chassis.isAtAngle() || tmrStageTimeOut.get() > 3.0) mStage++;                             
                                break;
                            case 13:
                                Console.logMsg("Angle reached. Opening claw and extending arm...");
                                Manipulator.openGrip();
                                Manipulator.extendArm();
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 14:
                                if(tmrStageTimeOut.get() > 1.0) mStage++;                             
                                break;
                            case 15:
                                Console.logMsg("Starting drive forward...");
                                Chassis.goToDistance(24.00);
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 16:
                                if(Chassis.isAtDistance() || tmrStageTimeOut.get() > 1.0) mStage++;                             
                                break;
                            case 17:
                                Console.logMsg("Distance reached. Stopping drive...");
                                Chassis.disable();
                                mStage++;
                                break;
                            case 18:
                                Console.logMsg("Sequence Complete \"" + Sequence.HIGH_SCORE_CUBE.toString() + "\" - " + StartingPosition.LOADING.toString());
                                mStage++;
                                break;
                            default:
                                Chassis.disable();
                                Elevator.disable();
                                Manipulator.disable();
                        } break;
                    default:
                        Chassis.disable();
                        Elevator.disable();
                        Manipulator.disable();
                }
            }
        },
        FIX_THAT_WHOLE_BALANCE_THING("Fix that whole balance thing"){
            @Override public void run() {
                switch(mStage){
                    case 0:
                        Console.logMsg("Starting Sequence \"" + Sequence.FIX_THAT_WHOLE_BALANCE_THING.toString() + "\" - " + StartingPosition.CHARGE_STATION.toString());
                        mStage++;
                        break;
                    case 1:
                        Console.logMsg("Starting drive backward...");
                        Chassis.goToDistance(mChargeBalanceDistance);
                        tmrStageTimeOut.reset();
                        mStage++;
                        break;
                    case 2:
                        if((Chassis.isAtDistance() && tmrStageTimeOut.get() > 2.5) || tmrStageTimeOut.get() > 3.0) mStage++;                             
                        break;
                    case 3:
                        Console.logMsg("Checking balance status...");
                        Chassis.disableDistancePID();
                        Chassis.resetDistance();
                        tmrStageTimeOut.reset();
                        mCorrectionCount++;
                        mStage++;
                        break;
                    case 4:
                        if(tmrStageTimeOut.get() > 1.0) mStage++;
                        break;
                    case 5:
                        if(Math.abs(Chassis.getDistance()) <= 0.3 || mCorrectionCount > 5){
                            Console.logMsg("Correction complete...");
                            Chassis.goToDistance(0.0);
                            mStage++;
                            break;
                        } else {
                            Console.logMsg("Correcting...");
                            //FIXME: do correct maths
                            Chassis.goToDistance((Math.abs(Chassis.getDistance()) + 1.0) * -Math.signum(Chassis.getDistance()));
                            mStage = 3;
                            break;
                        }
                    case 6:
                        Console.logMsg("Sequence Complete \"" + Sequence.JUST_DRIVE.toString() + "\" - " + StartingPosition.CHARGE_STATION.toString());
                        mStage++;
                        break;
                    default:
                        Elevator.disable();
                        Manipulator.disable();
                }
            }
        },
        HIGH_SCORE_CONE("High Score Cone"){
            @Override public void run() {
                switch(mStartingPosition){
                    case WALL:
                        switch(mStage){
                            case 0:
                                Console.logMsg("Starting Sequence \"" + Sequence.HIGH_SCORE_CONE.toString() + "\" - " + StartingPosition.WALL.toString());
                                mStage++;
                                break;
                            case 1:
                                Console.logMsg("Elevator to high position...");
                                Elevator.goToHeight(Elevator.Height.HIGH);
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 2:
                                if(Elevator.isAtHeight() || tmrStageTimeOut.get() > 1.5) mStage++;
                                break;
                            case 3:
                                Console.logMsg("Extending arm...");
                                Manipulator.extendArm();
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 4:
                                if(tmrStageTimeOut.get() > 1.0) mStage++;
                                break;
                            case 5:
                                Console.logMsg("Starting drive backwards, opening claw, retracting arm...");
                                Chassis.resetDistance();
                                Manipulator.retractArm();
                                Manipulator.openGrip();
                                Chassis.setDrive(-.15, -.15);
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 6:
                                if(tmrStageTimeOut.get() > .5) mStage++;                             
                                break;
                            case 7:
                                Console.logMsg("Returning elevator to bottom...");
                                Elevator.goToHeight(Elevator.Height.BOTTOM);
                                Chassis.disable();
                                tmrStageTimeOut.reset();
                                mStage++;
                            case 8:
                                if(Elevator.isLiftAtBottom() || tmrStageTimeOut.get() > 2.0) mStage++;
                                break;
                            case 9:
                                Console.logMsg("Starting drive backwards and closing grip...");
                                Chassis.goToDistance(mWallReverseDistance  - Chassis.getDistance());
                                Manipulator.closeGrip();
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 10:
                                if(Chassis.isAtDistance() || tmrStageTimeOut.get() > 3.0) mStage++;                             
                                break;
                            case 11:
                                Console.logMsg("Distance reached. Starting turn...");
                                if (mAlliance == Alliance.Red){
                                    Chassis.goToAngle(-168.00);
                                } else {
                                    Chassis.goToAngle(168.00);
                                }
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 12:
                                if(Chassis.isAtAngle() || tmrStageTimeOut.get() > 3.0) mStage++;                             
                                break;
                            case 13:
                                Console.logMsg("Angle reached. Opening claw and extending arm...");
                                Manipulator.openGrip();
                                Manipulator.extendArm();
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 14:
                                if(tmrStageTimeOut.get() > 1.0) mStage++;                             
                                break;
                            case 15:
                                Console.logMsg("Starting drive forward...");
                                Chassis.goToDistance(24.00);
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 16:
                                if(Chassis.isAtDistance() || tmrStageTimeOut.get() > 1.0) mStage++;                             
                                break;
                            case 17:
                                Console.logMsg("Distance reached. Stopping drive...");
                                Chassis.disable();
                                mStage++;
                                break;
                            case 18:
                                Console.logMsg("Sequence Complete \"" + Sequence.HIGH_SCORE_CONE.toString() + "\" - " + StartingPosition.WALL.toString());
                                mStage++;
                                break;
                            default:
                                Chassis.disable();
                                Elevator.disable();
                                Manipulator.disable();
                        } break;
                    case CHARGE_STATION:
                        switch(mStage){
                            case 0:
                                Console.logMsg("Starting Sequence \"" + Sequence.HIGH_SCORE_CONE.toString() + "\" - " + StartingPosition.CHARGE_STATION.toString());
                                mStage++;
                                break;
                            case 1:
                                Console.logMsg("Elevator to high position...");
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
                                Console.logMsg("Starting drive backwards, opening claw, retracting arm...");
                                Chassis.resetDistance();
                                Manipulator.retractArm();
                                Manipulator.openGrip();
                                Chassis.setDrive(-.15, -.15);
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 6:
                                if(tmrStageTimeOut.get() > .5) mStage++;                             
                                break;
                            case 7:
                                Console.logMsg("Returning elevator to bottom... \"");
                                Elevator.goToHeight(Elevator.Height.BOTTOM);
                                Chassis.disable();
                                tmrStageTimeOut.reset();
                                mStage++;
                            case 8:
                                if(Elevator.isLiftAtBottom() || tmrStageTimeOut.get() > 2.0) mStage++;
                                break;
                            case 9:
                                Console.logMsg("Waiting...");
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 10:
                            //FIXME: Make this an actual time
                                if(tmrStageTimeOut.get() > 0.0){
                                    mStage++;
                                };
                                break;
                            case 11:
                                Console.logMsg("Starting drive backwards to park and closing grip...");
                                Chassis.goToDistance(mChargeBalanceDistance - Chassis.getDistance());
                                Manipulator.closeGrip();
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 12:
                                if(Chassis.isAtDistance() || tmrStageTimeOut.get() > 5.0) mStage++;                             
                                break;
                            case 13:
                                Console.logMsg("Sequence Complete \"" + Sequence.HIGH_SCORE_CONE.toString() + "\" - " + StartingPosition.CHARGE_STATION.toString());
                                mStage++;
                                break;
                            default:
                                Elevator.disable();
                                Manipulator.disable();
                        } break;
                    case LOADING:
                        switch(mStage){
                            case 0:
                                Console.logMsg("Starting Sequence \"" + Sequence.HIGH_SCORE_CONE.toString() + "\" - " + StartingPosition.LOADING.toString());
                                mStage++;
                                break;
                            case 1:
                                Console.logMsg("Elevator to high position...");
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
                                Console.logMsg("Starting drive backwards...");
                                Chassis.resetDistance();
                                Manipulator.retractArm();
                                Manipulator.openGrip();
                                Chassis.setDrive(-.15, -.15);
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 6:
                                if(tmrStageTimeOut.get() > .5) mStage++;                             
                                break;
                            case 7:
                                Console.logMsg("Returning elevator to bottom... \"");
                                Elevator.goToHeight(Elevator.Height.BOTTOM);
                                Chassis.disable();
                                tmrStageTimeOut.reset();
                                mStage++;
                            case 8:
                                if(Elevator.isLiftAtBottom() || tmrStageTimeOut.get() > 2.0) mStage++;
                                break;
                            case 9:
                                Console.logMsg("Starting drive backwards and closing grip...");
                                Chassis.goToDistance(mLoadingReverseDistance - Chassis.getDistance());
                                Manipulator.closeGrip();
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 10:
                                if(Chassis.isAtDistance() || tmrStageTimeOut.get() > 3.0) mStage++;                             
                                break;
                            case 11:
                                Console.logMsg("Distance reached. Starting turn...");
                                if (mAlliance == Alliance.Red){
                                    Chassis.goToAngle(168.00);
                                } else {
                                    Chassis.goToAngle(-168.00);
                                }
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 12:
                                if(Chassis.isAtAngle() || tmrStageTimeOut.get() > 3.0) mStage++;                             
                                break;
                            case 13:
                                Console.logMsg("Angle reached. Opening claw and extending arm...");
                                Manipulator.openGrip();
                                Manipulator.extendArm();
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 14:
                                if(tmrStageTimeOut.get() > 1.0) mStage++;                             
                                break;
                            case 15:
                                Console.logMsg("Starting drive forward...");
                                Chassis.goToDistance(24.00);
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 16:
                                if(Chassis.isAtDistance() || tmrStageTimeOut.get() > 1.0) mStage++;                             
                                break;
                            case 17:
                                Console.logMsg("Distance reached. Stopping drive...");
                                Chassis.disable();
                                mStage++;
                                break;
                            case 18:
                                Console.logMsg("Sequence Complete \"" + Sequence.HIGH_SCORE_CONE.toString() + "\" - " + StartingPosition.LOADING.toString());
                                mStage++;
                                break;
                            default:
                                Chassis.disable();
                                Elevator.disable();
                                Manipulator.disable();
                        } break;
                    default:
                        Chassis.disableDrive();
                        Elevator.disable();
                        Manipulator.disable();
                }
            }
        };

        //Timers
        public static final Timer tmrStageTimeOut = new Timer();

        //Static Variables 
        private static int mStage = 0;
        private static int mCorrectionCount = 0;
        private static final double mWallReverseDistance = -156.00;
        private static final double mChargeBalanceDistance = -102.00;
        private static final double mLoadingReverseDistance = -156.00;
        
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
    private static Alliance mAlliance;

    /** Prevent instances of autonomous class */
    private Autonomous(){}

    /**
     * Runs once at start of autonomous program, pulls starting position 
     * and sequence from those selected on dashboard
     */
    public static void init() {
        mStartingPosition = chsStartingPosition.getSelected();
        mSequence = chsSequence.getSelected();
        mAlliance = DriverStation.getAlliance();

        mSequence.init();

        Chassis.setDriveNeutralMode(NeutralMode.Brake);

        Disabled.setBreakTimerEnabled(true);
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
        chsSequence.addOption(Sequence.JUST_DRIVE.label, Sequence.JUST_DRIVE);
        chsSequence.addOption(Sequence.LOW_SCORE.label, Sequence.LOW_SCORE);
        chsSequence.addOption(Sequence.HIGH_SCORE_CUBE.label, Sequence.HIGH_SCORE_CUBE);
        chsSequence.addOption(Sequence.HIGH_SCORE_CONE.label, Sequence.HIGH_SCORE_CONE);
        chsSequence.addOption(Sequence.FIX_THAT_WHOLE_BALANCE_THING.label, Sequence.FIX_THAT_WHOLE_BALANCE_THING);

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

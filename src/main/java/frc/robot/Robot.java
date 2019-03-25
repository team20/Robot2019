/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMNOdoc:cO0;.   .kMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMWXOxocl0WX:     ':.    .xMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMWNKKWNo.     .kNd.  ..   .'  .xMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMWXko:,;dXWl   ,'  .xO,  ':.  ;c  .xMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMWXkOk,   'odxXx.  ',   .oc. 'xd,,od,''lddoolllllllllooooooddxxkO0KXNWWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMNOo;',dKo.   .;xXO.   :o,',cl;,,;;;;;;;::cclloddxxxkkkkkkkkkxxdddooollllloodxOKNWWNK0OOkOO0KXWMMMMMMMMMMMMMMMMMMM
 * MMMMNOl,.   :XMNd.   ;l:c:,,;ll;,,,;:cldxO0KXNWWMMMMMMWWWWWWWWNNWWWWWMMMMMMWWNXKOxdl;..,,'...'''...',cd0NMMMMMMMMMMMMMMM
 * MMMMXdldc.   ;KMWx. .,;,;;,,,;cok0KNWMMMMWNXK0kxdollc:;;;,,,''''',,;;:cllodkOKXNXOo;..':ldk0KXXXK0Oxl;..,dXMMMMMMMMMMMMM
 * MMMMMMMMWk'   ,0WNx:;,,,:ox0XWMMMWNX0kdl:;'..                                ..'...:d0NWMMMMMMMMMMMMMWKd,.,OWMMMMMMMMMMM
 * MMMMMMMMMMKc.':ol;,,cd0NWMMMWX0xl:,..       .,;clooddooolc:,'.                 .cxXWMMMMMMMMMMMMMMMMMMMMXc .OWMMMMMMMMMM
 * MMMMMMMMMMMKd:,':d0NMMMMWKkl;.         .,lx0XWWMMMMMMMMMMMMWNX0d,            ,dKWMMMMMMMMMMMMMMMMMMMMMMMMX; ;XMMMMMMMMMM
 * MMMMMMMMWKo,':xKWMMMWXkl,.          .:xKWMMMMMMMMMMMMMMMMMMMMMMMNo.        ;kNMMMMMMMMMMMMMMMMMMMMMMMMMMMWx..cKWMMMMMMMM
 * MMMMMMWOc',oKWMMMWXkc.            'oKWMMMMMMMMMMMMMMMMMMMMMMMMMMMNc      'xNMMMMMMMMMMMMWNNWMMMMMMMMMMMMMMO. .'cOWMMMMMM
 * MMMMW0c.,xXMMMMW0l.             .dXWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMx.   .lXMMMMMMMMMMMMNx:''xWMMMMMMMMMMMMMO..ox;.c0WMMMM
 * MMMXo.'xNMMMMWO:.             .cKWMMMMMMMMMMMMWWMMMMMMMMMMMMMMMMMWo   'kWMMMMMMMMMMMM0:   .dWMMMMMMMMMMMMWd..OMNx,'oXMMM
 * MW0;.cKMMMMWKc.              .xNMMMMMMMMMMMXxc;:kWMMMMMMMMMMMMMMMK;  ;KMMMMMMMMMMMMMO,    ,KMMMMMMMMMMMMMX: :XMMMXl.;0WM
 * WO'.dNMMMMWk'               ,OWMMMMMMMMMMWx'    ;XMMMMMMMMMMMMMMNl  cXMMMMMMMMMMMMM0,    .xWMMMMMMMMMMMMMk. cNMMMMNx.'OW
 * 0,.dWMMMMWd.               ,0MMMMMMWWWWNNx.    .kWMMMMMMMMMMMMMXl. cXMMMMMMMMMMMMMX:     :XMMMMMMMMMMMMMNc  .oNMMMMWx.,0
 * :.cNMMMMWk.                ,lllcc::;;,,''.    :0WMMMMMMMMMMMMW0:  ;KMMMMMMMMMMMMMNo     'OMMMMMMMMMMMMMWx.   .dWMMMMWo.:
 * ..OMMMMMX:                                  ;kNMMMMMMMMMMMMNKd.  '0MMMMMMMMMMMMMMk.    .dWMMMMMMMMMMMMMK,     ,0MMMMMK,.
 * ;XMMMMMO.                               .:kNMMMMMMMMMMMMWk;.   .xWMMMMMMMMMMMMMK;     cNMMMMMMMMMMMMMXc      .xMMMMMNc
 * ;XMMMMMO.                             .l0WMMMMMMMMMMMMWO:.     cNMMMMMMMMMMMMMWo     ,KMMMMMMMMMMMMMNo.      .dMMMMMNl
 * ,0MMMMM0,                          .;dXWMMMMMMMMMMMMNk:.      '0MMMMMMMMMMMMMMO.    .OWMMMMMMMMMMMMNo.       .kMMMMMX:
 * ..dWMMMMWl                        'l0NMMMMMMMMMMMMWXx,         lWMMMMMMMMMMMMMXc    .xWMMMMMMMMMMMMXl.        :XMMMMMk..
 * o.,0MMMMMK;                    'lkNMMMMMMMMMMMMMNOl.          .kMMMMMMMMMMMMMMO.   .xWMMMMMMMMMMMWK:         '0MMMMMX:.o
 * Xc.;KMMMMMK:                'lkNMMMMMMMMMMMMMWKd,.            ,0MMMMMMMMMMMMMMO,.,cOWMMMMMMMMMMMNx'         ,OWMMMMXc.cX
 * MXc.,OWMMMMXo.          .,oONMMMMMMMMMMMMMWKx:......'',;;.    ,KMMMMMMMMMMMMMMWNXNWMMMMMMMMMMMW0c.        .cKMMMMMK:.cXM
 * MMNd..oXMMMMWO:.     .:d0WMMMMMMMMMMMMMMMMN0kkO00KXXNWWW0,    'OMMMMMMMMMMMMMMMMMMMMMMMMMMMMWKl.         ,kNMMMMNx'.dNMM
 * MMMW0:.,kNMMMMNk;  'xXWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMNl     .dWMMMMMMMMMMMMMMMMMMMMMMMMMW0l.  ...   .;xNMMMMWO:.:0WMMM
 * MMMMMNk;.;xXMMMWx..kWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWx.      'OMMMMMMMMMMMMMMMMMMMMMMWXx;..;dkd:'..cONMMMMNk:.;kNMMMMM
 * MMMMMMMNk:.,o0N0,.oNMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM0,        'xNMMMMMMMMMMMMMMMMMNKx:. .c0WNo..ckXWMMMWKd;':kNMMMMMMM
 * MMMMMMMMMW0o,.,' :XMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMNc           ,okKNWWMMMMWWXKOdc,.  ..l0XXOc.cXMMMWKd:',o0WMMMMMMMMM
 * MMMMMMMMMMMMNk' ,0MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWx.              ..,;::::::::::clodkO00KKXXKl'dXOo;,;lONMMMMMMMMMMMM
 * MMMMMMMMMMMMWx..kWMMMMMMMMMMMMWWWNNXXKK0Okkxddollcc:;.                 ..,codxk0KKKKXXKXXKKKKKXXx..,,cd0NMMMMMMMMMMMMMMM
 * MMMMMMMMMMMM0' :O0Okkxddollc::;,,,,,,''.                           .':dO0KXKKKKXXKKXXXXKKKK0O0KKk,..:cldXMMMMMMMMMMMMMMM
 * MMMMMMMMMMMXc...''',,,;;:cc:,..   .,coxdlc;'..                   .cxOKXXXXKKKKKKKKKKKK0kolc;;d0XNKkoc,.'kMMMMMMMMMMMMMMM
 * MMMMMMMMMMMN0O00KXXNNWWWMMMMWX0kdl:,''',;:::;;''...            ,oOKKKKXXKKKKKKKKOkdllclloxkxl:::cccccld0WMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWWXK0kxolc:;,''.....        .:ldxxkkOOkkxdollllloxOKNWMMMMMWNXKKKXNWMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWNXKK0Okkxxddddool:,'.......'',:lx0NWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 */

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.auto.AutoModes;
import frc.robot.controls.DriverControls;
import frc.robot.controls.OperatorControls;
import frc.robot.subsystems.*;
import frc.robot.utils.PrettyPrint;

import static frc.robot.subsystems.Arm.Position.STARTING_CONFIG;
import static frc.robot.subsystems.Elevator.Position.ELEVATOR_FLOOR;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
    private AutoModes auto;

    public static AHRS gyro = new AHRS(SerialPort.Port.kMXP); // DO NOT MOVE

    private boolean autoSet;
    private boolean inEndOfMatch;

    private double startTime;

    @Override
    public void robotInit() {
        auto = new AutoModes();

        autoSet = false;
        inEndOfMatch = false;

        Arduino.setAllianceColor(DriverStation.getInstance().getAlliance());
        Arduino.setPattern(1);
        Arduino.startThread();
        LineSensor.startThread();
        Arduino.setDiagnosticPattern(null, 0);
        Drivetrain.setBrakeMode(false);
    }

    @Override
    public void robotPeriodic() {
        //set pattern of LEDs
        if (inEndOfMatch)
            Arduino.setPattern(3);
        else
            //the line below has not been fully tested yet (it is for showing the height of the elevator on the LEDs when it is moving)
            //Arduino.setPattern(Elevator.doneMoving() ? 2 : (int) ((Elevator.getPosition() / Elevator.MAX_POSITION) * 15.0 + 4));
            Arduino.setPattern(2);

        //set diagnostic part of LEDs
        if (Intake.isCargoPresent())
            Arduino.setDiagnosticPattern(Arduino.Colors.Orange, 1);
        else if (Intake.intakeRunning())
            Arduino.setDiagnosticPattern(Arduino.Colors.Orange, 2);
//        else if (LineSensor.isBroken())
//            Arduino.setDiagnosticPattern(Arduino.Colors.Red, 2);
        else if (LineSensor.isLineSeen())
            Arduino.setDiagnosticPattern(Arduino.Colors.Green, 1);
        else
            Arduino.setDiagnosticPattern(null, 0);

//        PrettyPrint.put("Elev Amps", Elevator.getCurrent());
//        PrettyPrint.put("Elev Temp", Elevator.getTemperature());
//        PrettyPrint.put("Elev Pos", Elevator.getPosition());
//        PrettyPrint.put("Arm Amps", Arm.getPosition());
//        PrettyPrint.put("Arm Pos", Arm.getPosition());

        PrettyPrint.setFrequency(2);
        PrettyPrint.print();
    }

    @Override
    public void autonomousInit() {
        Arm.setPosition(STARTING_CONFIG);
        Elevator.setPosition(ELEVATOR_FLOOR);
        Arduino.setAllianceColor(DriverStation.getInstance().getAlliance());
    }


    @Override
    public void autonomousPeriodic() {
//        PrettyPrint.put("line sensor total", LineSensor.getTotal());
//        PrettyPrint.put("line position", LineSensor.getLinePosition());
//        PrettyPrint.put("turn speed", LineSensor.getTurnSpeed());

        PrettyPrint.put("Front Pos", Climber.getFrontEncPosition());
        PrettyPrint.put("Back Pos", Climber.getBackEncPosition());
        PrettyPrint.put("DT Pos", Drivetrain.getEncoderPosition());

        DriverControls.driverControls();
        OperatorControls.operatorControls();
    }

    @Override
    public void teleopInit() {
        startTime = Timer.getFPGATimestamp();
        Arduino.setAllianceColor(DriverStation.getInstance().getAlliance());
    }

    @Override
    public void teleopPeriodic() {
        if (Timer.getFPGATimestamp() - startTime >= 135 - 40) {
            inEndOfMatch = true;
        }
        DriverControls.driverControls();
        OperatorControls.operatorControls();
    }


    @Override
    public void testInit() {
    }

    @Override
    public void testPeriodic() {
    }

    @Override
    public void disabledInit() {
//        LineSensor.stopThread();
        Drivetrain.setBrakeMode(false);
        inEndOfMatch = false;
        PrettyPrint.removeAll();
    }
}

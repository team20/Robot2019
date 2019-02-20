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
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.auto.AutoModes;
import frc.robot.auto.AutoModes.Mode;
import frc.robot.controls.DriverControls;
import frc.robot.controls.OperatorControls;
import frc.robot.utils.PrettyPrint;

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

    @Override
    public void robotInit() {
        auto = new AutoModes();

        autoSet = false;
    }

    @Override
    public void autonomousInit() {
    }

    @Override
    public void autonomousPeriodic() {
        // This is here because autonomous init is not reliable
        if (!autoSet) {
            auto.setMode(Mode.Align); // TODO: eventually make auto selection based off of user input to the
                                      // SmartDashboard
            switch (auto.getMode()) {
            case CrossLine:
                auto.crossLine();
                break;
            case Align:
                auto.align(false);
                break;
            default:
                PrettyPrint.once("NO AUTO SELECTED");
                break;
            }
            autoSet = true;
        }
        auto.runAuto();
    }

    @Override
    public void teleopInit() {
    }

    @Override
    public void teleopPeriodic() {
        DriverControls.driverControls();
        OperatorControls.operatorControls();
    }

    @Override
    public void testInit() {
    }

    @Override
    public void testPeriodic() {
        PrettyPrint.put("Pitch", gyro.getPitch());
    }

    @Override
    public void disabledInit() {
        PrettyPrint.removeAll();
    }

    @Override
    public void robotPeriodic() {
        PrettyPrint.print();
    }
}

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Arm {
    private static final int deviceID = 1;
    private static CANSparkMax m_motor;
    private static CANPIDController m_pidController;
    private static CANEncoder m_encoder;
    public static double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput,rotations;

    public Arm() {
        m_motor = new CANSparkMax(deviceID, MotorType.kBrushless);
        kP = 0.0001;
        kI = 0;
        kD = 0;
        kIz = 0;
        kFF = 0;
        kMaxOutput = 1;
        kMinOutput = -1;
        rotations=0;
        m_pidController = m_motor.getPIDController();
        m_encoder = m_motor.getEncoder();
        m_pidController.setP(kP);
        m_pidController.setI(kI);
        m_pidController.setD(kD);
        m_pidController.setIZone(kIz);
        m_pidController.setFF(kFF);
        m_pidController.setOutputRange(kMinOutput, kMaxOutput);
        m_pidController.setReference(rotations, ControlType.kPosition);
        
       
     } 
     public static void Stablize(){// initialize motor
        m_pidController.setReference(rotations, ControlType.kPosition);
        SmartDashboard.putNumber("ProcessVariable", m_encoder.getPosition());
        
        
        
    }

}
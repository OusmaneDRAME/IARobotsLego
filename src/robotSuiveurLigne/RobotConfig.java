package robotSuiveurLigne;


import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;

public class RobotConfig {
	// Configuration du diamètre et de la distance entre les deux roues
	public static final int WHEEL_DIAMETER = 56 ;
	public static final int TRACK_WIDTH = 120 ;
	
	// Configuration moteur Robot
	public static final NXTRegulatedMotor MOTOR_LEFT = Motor.C;
	public static final  NXTRegulatedMotor MOTOR_RIGHT = Motor.B;
	public static final NXTRegulatedMotor MOTOR_HEAD = Motor.A;


	// Configuration Capteurs lumineux
	public static final LightSensor LIGHT_LEFT = new LightSensor(SensorPort.S2);
	public static final LightSensor LIGHT_RIGHT = new LightSensor(SensorPort.S1);

	//Configuration Capteur distance
	public static final UltrasonicSensor ULTRASONIC_S = new UltrasonicSensor(SensorPort.S3);


	// Valeur par defaut de la vitesse des moteurs
	public static final int VITESSE_ROTATION = 80;
	public static final int VITESSE_TETE = 700;
	public static final int VITESSE_DEFAUT = 200;
	public static final int LOW_SPEED = 150;
	public static final int MEDIUM_SPEED = 180;
	public static final int HIGH_SPEED = 220;
	public static final int VERY_HIGH_SPEED = 300;

	// Valeur du seuil de detection des objets
	public static final int SEUIL_DETECTION = 16;
	public static final int SEUIL_DETECTION_COTE = 30;
	
	// Valeur seuil pour différencier le noir et le blanc
	public static final int VALEUR_SEUIL = 50;
	public static final int LEFT_LOW_THRESHOLD = 43;
	public static final int LEFT_MID_THRESHOLD = 47;
	public static final int LEFT_HIGH_THRESHOLD = 53;
	public static final int RIGHT_LOW_THRESHOLD = 43;
	public static final int RIGHT_MID_THRESHOLD = 47;
	public static final int RIGHT_HIGH_THRESHOLD = 53;
	
	
	// Valeur pour calcul PID suivi de ligne
	public static final int SP = 0;
	public static final float KP = 40;
	public static final float KI = 0;
	public static final float KD = 0;
	public static final float I_HIGH = 100;
	public static final float I_LOW = 0;
	public static final float MV_HIGH = 150;
	public static final float MV_LOW = -150;
	
	
	// Valeur pour calcul PID 1 capteur
	public static final int SP1 = 45;
	public static final int KP1 = 8;
	public static final int KI1 = 0;
	public static final int KD1 = 0;
	

}
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
	public static final  NXTRegulatedMotor MOTOR_RIGTH = Motor.B;
	public static final NXTRegulatedMotor MOTOR_HEAD = Motor.A;


	// Configuration Capteurs lumineux
	public static final LightSensor LIGHT_LEFT = new LightSensor(SensorPort.S2);
	public static final LightSensor LIGHT_RIGTH = new LightSensor(SensorPort.S1);

	//Configuration Capteur distance
	public static final UltrasonicSensor ULTRASONIC_S = new UltrasonicSensor(SensorPort.S3);


	// Valeur par defaut de la vitesse des moteurs
	public static final int VITESSE_ROTATION = 80;
	public static final int VITESSE_TETE = 700;
	public static final int VITESSE_DEFAUT = 140;

	// Valeur du seuil de detection des objets
	public static final int SEUIL_DETECTION = 16;
	public static final int SEUIL_DETECTION_COTE = 30;
	
	// Valeur seuil pour différencier le noir et le blanc
	public static final int VALEUR_SEUIL = 50;

}

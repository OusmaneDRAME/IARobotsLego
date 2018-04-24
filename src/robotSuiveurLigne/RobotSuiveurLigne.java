package robotSuiveurLigne;


import java.util.LinkedList;
import java.util.List;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.Sound;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.RotateMoveController;
import lejos.util.PIDController;

public class RobotSuiveurLigne {
	private DifferentialPilot pilote = new DifferentialPilot(RobotConfig.WHEEL_DIAMETER, RobotConfig.TRACK_WIDTH, RobotConfig.MOTOR_LEFT, RobotConfig.MOTOR_RIGHT) ;
	private int[] notes = {2349,115,0,5,1760,165,0,35} ;
	private PIDController pidControl = new PIDController(RobotConfig.SP,10);
	private PIDController pidControl1Sensor = new PIDController(RobotConfig.SP1,10);
	private List<Direction> parcours = new LinkedList<>();

	
	public void initParcours () {
		parcours.add(Direction.GAUCHE);
		parcours.add(Direction.DROITE);
		parcours.add(Direction.GAUCHE);
		parcours.add(Direction.DROITE);
	}
	
	public void parcourir () {
		this.initParcours();
		while (parcours.size() > 0) {
			this.followLinePID(false);
		}
	}
	
	public RobotSuiveurLigne() {
		RobotConfig.MOTOR_LEFT.setSpeed(RobotConfig.VITESSE_DEFAUT) ;
		RobotConfig.MOTOR_RIGHT.setSpeed(RobotConfig.VITESSE_DEFAUT) ;
		RobotConfig.MOTOR_HEAD.setSpeed(RobotConfig.VITESSE_TETE) ;
		this.pilote.setRotateSpeed(RobotConfig.VITESSE_ROTATION) ;
		this.pilote.setTravelSpeed(RobotConfig.VITESSE_DEFAUT) ;
	}
	
	public void avancerEnSuivantLigne() {
	
		LCD.drawString("OK OK OK ", 0, 0) ;
		LCD.drawString("Après Boucle ", 1, 1) ;
		while(true) {
			
			if(RobotConfig.LIGHT_LEFT.getLightValue() > RobotConfig.VALEUR_SEUIL){
				LCD.drawString("IF LUMIERE GAUCHE BLANC", 2, 2) ;
				pilote.setTravelSpeed(30) ;
				while(RobotConfig.LIGHT_LEFT.getLightValue() > RobotConfig.VALEUR_SEUIL) {
					pilote.rotateLeft() ;pilote.forward() ; 
				}
							
			} else if (RobotConfig.LIGHT_RIGHT.getLightValue() > RobotConfig.VALEUR_SEUIL) {
				LCD.drawString("LUMIERE D", 3, 3) ;
				pilote.setTravelSpeed(30) ;
				while(RobotConfig.LIGHT_RIGHT.getLightValue() > RobotConfig.VALEUR_SEUIL) {
					pilote.rotateRight() ; 
					pilote.forward() ;
				}
				
			} else if (RobotConfig.LIGHT_LEFT.getLightValue() > RobotConfig.VALEUR_SEUIL && RobotConfig.LIGHT_RIGHT.getLightValue() > RobotConfig.VALEUR_SEUIL) {
				if(RobotConfig.LIGHT_LEFT.getLightValue() > RobotConfig.VALEUR_SEUIL && RobotConfig.LIGHT_RIGHT.getLightValue() > RobotConfig.VALEUR_SEUIL) {
					Sound.pause(3000) ;
				}
			}
			pilote.setTravelSpeed(RobotConfig.VITESSE_DEFAUT) ;
			pilote.forward() ;
		}
		
	}
	
	public int sensorLevel (LightSensor sensor , int lowTreshold, int midTreshold, int highTreshold) {
		int value = sensor.getLightValue();
		int level;
		if (value <= lowTreshold) {
			level = 0;
		}
		else if (value <= midTreshold) {
			level = 1;
		}
		else if (value <= highTreshold) {
			level = 2;
		}
		else {
			level = 3;
		}
		return level;
	}
	
	public int getPIDInput () {
		int leftLevel = sensorLevel(RobotConfig.LIGHT_LEFT,RobotConfig.LEFT_LOW_THRESHOLD,RobotConfig.LEFT_MID_THRESHOLD,RobotConfig.LEFT_HIGH_THRESHOLD);
		int rightLevel = sensorLevel(RobotConfig.LIGHT_RIGHT,RobotConfig.RIGHT_LOW_THRESHOLD,RobotConfig.RIGHT_MID_THRESHOLD,RobotConfig.RIGHT_HIGH_THRESHOLD);
		int index = leftLevel*4 + rightLevel;
		int inputValue = 0;
		switch (index) {
		case 0 : //center
			inputValue = 0;
			break;
		case 1 : //left
			inputValue = 3;
			break;
		case 2 : //far left
			inputValue = 4;
			break;
		case 3 : //far far left
			inputValue = 5;
			break;
		case 4 : //right
			inputValue = -3;
			break;
		case 5 : //center
			inputValue = 0;
			break;
		case 6 : //near near left
			inputValue = 1;
			break;
		case 7 : //near left
			inputValue = 2;
			break;
		case 8 : //far right
			inputValue = -4;
			break;
		case 9 : //near near right
			inputValue = -1;
		case 10 : //center
			inputValue = 0;
		case 11 : //near near left
			inputValue = 1;
		case 12 : //far far right
			inputValue = -5;
		case 13 : //near right
			inputValue = -2;
		case 14 : //near near right
			inputValue = -1;
		case 15 : //center
			inputValue = 99;
		}
		return inputValue;
	}
	
	public void stop () {
		RobotConfig.MOTOR_LEFT.stop();
		RobotConfig.MOTOR_RIGHT.stop();;
	}
	
	public void followLinePID (boolean ignore) {
		int input;
		boolean continu = true;
		LCD.clear(6);
		LCD.drawString("follow line", 0, 6);
		pidControl.setPIDParam(PIDController.PID_KP, RobotConfig.KP);
		pidControl.setPIDParam(PIDController.PID_KI, RobotConfig.KI);
		pidControl.setPIDParam(PIDController.PID_KD, RobotConfig.KD);
		pidControl.setPIDParam(PIDController.PID_I_LIMITLOW, RobotConfig.I_LOW);
		pidControl.setPIDParam(PIDController.PID_I_LIMITHIGH, RobotConfig.I_HIGH);
		while (continu) {
			input = getPIDInput();
			if (input == 99) {
				continu = false;
				this.detectIntersection(ignore);
			}
			else {
				int output = pidControl.doPID(input);
				LCD.drawInt(output, 0, 0);
				RobotConfig.MOTOR_LEFT.setSpeed(220 - output);
				RobotConfig.MOTOR_RIGHT.setSpeed(220 + output);
				this.forward();
			}
		}
	}
	
	public void forward () {
		RobotConfig.MOTOR_LEFT.forward();
		RobotConfig.MOTOR_RIGHT.forward();
	}
	
	public void testSensor () {
		while (true) {
			int leftValue = RobotConfig.LIGHT_LEFT.getLightValue();
			int rightValue = RobotConfig.LIGHT_RIGHT.getLightValue();
			LCD.drawInt(leftValue, 0, 0);
			LCD.drawInt(rightValue, 0, 1);
		}
	}
	
	
	public void jouerSons() {
		for(int i = 0 ; i < notes.length ; i += 2) {
			int temps = notes[i+1] * 10 ;
			int note = notes[i] ;
			if(note != 0) Sound.playTone(note, temps) ;
			Sound.pause(temps) ;
		}
		Sound.beepSequence() ;
		Sound.beepSequenceUp() ;
	}
	
	public void forwardBackward() {
		
		RobotConfig.MOTOR_LEFT.forward() ;
		RobotConfig.MOTOR_RIGHT.forward() ;
		Sound.pause(1000) ;
		RobotConfig.MOTOR_LEFT.backward() ;
		RobotConfig.MOTOR_RIGHT.backward() ;
		Sound.pause(1000) ;
		RobotConfig.MOTOR_LEFT.stop() ;
		RobotConfig.MOTOR_RIGHT.stop() ;
	}
	

	
	public void setSpeed (int speed) {
		RobotConfig.MOTOR_LEFT.setSpeed(speed);
		RobotConfig.MOTOR_RIGHT.setSpeed(speed);
	}
	
	public void detectIntersection (boolean ignore) {
		int state = 0;
		long start;
		long stop;
		start = System.currentTimeMillis();
		stop = System.currentTimeMillis();
		while (RobotConfig.LIGHT_LEFT.getLightValue() > RobotConfig.LEFT_HIGH_THRESHOLD && RobotConfig.LIGHT_RIGHT.getLightValue() > RobotConfig.RIGHT_HIGH_THRESHOLD) {
			this.setSpeed(140);
			this.forward();
			stop = System.currentTimeMillis();
		}
		//this.stop();
		/*if (stop - start >= 3000) {
			state = 2;
		}
		else {
			this.setSpeed(100);
			pilote.travel(10);
				if (RobotConfig.LIGHT_LEFT.getLightValue() > RobotConfig.LEFT_HIGH_THRESHOLD && RobotConfig.LIGHT_RIGHT.getLightValue() > RobotConfig.RIGHT_HIGH_THRESHOLD) {
					state = 1;
				}
		}*/
		if (ignore) {
		}
		else {
			if (state == 0) {
				LCD.drawString("Intersection", 0, 3);
				if (parcours.get(0) == Direction.GAUCHE) {
					parcours.remove(0);
					this.intersectionLeft();
				}
				else {
					parcours.remove(0);
					this.intersectionRight();
				}
				
			}
			else if (state == 1) {
				LCD.drawString("Croisement", 0, 3);
				this.stop();
			}
			else if (state == 2) {
				LCD.drawString("Impasse", 0, 3);
				this.stop();
			}
			this.setSpeed(RobotConfig.VITESSE_DEFAUT);
		}
	}
	
	public int detectLine (int lightValue, int prev) {
		if (prev == 1 && lightValue < RobotConfig.LEFT_LOW_THRESHOLD) {
			return 1;
		}
		else 
			return 0;
	}
	

	
	public void intersectionLeft () {
		pidControl1Sensor.setPIDParam(PIDController.PID_KP, RobotConfig.KP1);
		pidControl1Sensor.setPIDParam(PIDController.PID_KI, RobotConfig.KI1);
		pidControl1Sensor.setPIDParam(PIDController.PID_KD, RobotConfig.KD1);
		LCD.clear(6);
		LCD.drawString("inter gauche", 0, 6);
		long start = System.currentTimeMillis();
		long startCont = System.currentTimeMillis();
		long stop;
		boolean continu = true;
		while (continu) {
			int input = RobotConfig.LIGHT_LEFT.getLightValue();
			int rightValue = RobotConfig.LIGHT_RIGHT.getLightValue();
			if (rightValue >= RobotConfig.RIGHT_HIGH_THRESHOLD) {
				startCont = System.currentTimeMillis();
			}
			int output = pidControl1Sensor.doPID(input);
			RobotConfig.MOTOR_LEFT.setSpeed(200 + output);
			RobotConfig.MOTOR_RIGHT.setSpeed(200 - output);
			this.forward();
			stop = System.currentTimeMillis();
			LCD.drawInt((int) (stop-start), 0, 7);
			if (stop-startCont > 2000) {
				continu = false;
			}
			if (input >= RobotConfig.LEFT_HIGH_THRESHOLD && rightValue >= RobotConfig.RIGHT_HIGH_THRESHOLD && stop-start > 2300) {
				continu = false;
				while (input > RobotConfig.LEFT_LOW_THRESHOLD || rightValue > RobotConfig.RIGHT_LOW_THRESHOLD) {
					this.forward();
					input = RobotConfig.LIGHT_LEFT.getLightValue();
					rightValue = RobotConfig.LIGHT_RIGHT.getLightValue();
				}
			}
		}
	}
	
	public void intersectionRight () {
		pidControl1Sensor.setPIDParam(PIDController.PID_KP, RobotConfig.KP1);
		pidControl1Sensor.setPIDParam(PIDController.PID_KI, RobotConfig.KI1);
		pidControl1Sensor.setPIDParam(PIDController.PID_KD, RobotConfig.KD1);
		LCD.clear(6);
		LCD.drawString("inter droite", 0, 6);
		long start = System.currentTimeMillis();
		long startCont = System.currentTimeMillis();
		long stop;
		boolean continu = true;
		while (continu) {
			int input = RobotConfig.LIGHT_RIGHT.getLightValue();
			int leftValue = RobotConfig.LIGHT_LEFT.getLightValue();
			if (leftValue >= RobotConfig.LEFT_HIGH_THRESHOLD) {
				startCont = System.currentTimeMillis();
			}
			int output = pidControl1Sensor.doPID(input);
			RobotConfig.MOTOR_RIGHT.setSpeed(200 + output);
			RobotConfig.MOTOR_LEFT.setSpeed(200 - output);
			this.forward();
			stop = System.currentTimeMillis();
			LCD.drawInt((int) (stop-start), 0, 7);
			if (stop-startCont > 2000) {
				continu = false;
			}
			if (input >= RobotConfig.RIGHT_HIGH_THRESHOLD && leftValue >= RobotConfig.LEFT_HIGH_THRESHOLD && stop-start > 2300) {
				continu = false;
				while (input > RobotConfig.RIGHT_LOW_THRESHOLD || leftValue > RobotConfig.RIGHT_LOW_THRESHOLD) {
					this.forward();
					input = RobotConfig.LIGHT_RIGHT.getLightValue();
					leftValue = RobotConfig.LIGHT_LEFT.getLightValue();
				}
			}
		}
	}
	

	
	public void junctionLeft () {
		pidControl1Sensor.setPIDParam(PIDController.PID_KP, RobotConfig.KP1);
		pidControl1Sensor.setPIDParam(PIDController.PID_KI, RobotConfig.KI1);
		pidControl1Sensor.setPIDParam(PIDController.PID_KD, RobotConfig.KD1);
		int acc = 0;
		int prev = 0;
		while (acc < 3) {
			int input = RobotConfig.LIGHT_LEFT.getLightValue();
			int rightValue = RobotConfig.LIGHT_RIGHT.getLightValue();
			if (this.detectLine(rightValue, prev) == 1) {
				prev = 0;
				acc =+ 1;
			}
			if (rightValue >= RobotConfig.RIGHT_HIGH_THRESHOLD) {
				prev = 1;
			}
			int output = pidControl1Sensor.doPID(input);
			RobotConfig.MOTOR_LEFT.setSpeed(180 - output);
			RobotConfig.MOTOR_RIGHT.setSpeed(180 + output);
			this.forward();
		}
		pilote.stop();
	}
	
	public void junctionRight () {
		pidControl1Sensor.setPIDParam(PIDController.PID_KP, RobotConfig.KP1);
		pidControl1Sensor.setPIDParam(PIDController.PID_KI, RobotConfig.KI1);
		pidControl1Sensor.setPIDParam(PIDController.PID_KD, RobotConfig.KD1);
		int acc = 0;
		int prev = 0;
		while (acc < 3) {
			int input = RobotConfig.LIGHT_RIGHT.getLightValue();
			int leftValue = RobotConfig.LIGHT_LEFT.getLightValue();
			if (this.detectLine(leftValue, prev) == 1) {
				prev = 0;
				acc =+ 1;
			}
			if (leftValue >= RobotConfig.LEFT_HIGH_THRESHOLD) {
				prev = 1;
			}
			int output = pidControl1Sensor.doPID(input);
			RobotConfig.MOTOR_RIGHT.setSpeed(180 - output);
			RobotConfig.MOTOR_LEFT.setSpeed(180 + output);
			this.forward();
		}
		pilote.stop();
	}
	
	public void rotate360Degres() {
		RobotConfig.MOTOR_RIGHT.forward() ;
		int degreReel = 0 ;
		while(degreReel < 360) 
			degreReel = RobotConfig.MOTOR_RIGHT.getTachoCount() ;
		RobotConfig.MOTOR_RIGHT.flt() ;
		LCD.drawInt(degreReel, 0, 1) ;
		while(RobotConfig.MOTOR_RIGHT.getRotationSpeed() > 0) ;
		LCD.drawInt(RobotConfig.MOTOR_RIGHT.getTachoCount(), 7, 1) ;
	}

	public void rotate360DegresBis() {
		RobotConfig.MOTOR_RIGHT.rotate(1440) ;
		LCD.drawInt(RobotConfig.MOTOR_RIGHT.getTachoCount(), 4, 0, 1) ;
		RobotConfig.MOTOR_RIGHT.rotateTo(0) ;
		LCD.drawInt(RobotConfig.MOTOR_RIGHT.getTachoCount(), 4, 0, 2) ;
	}
	
	public void utilisationDuTachymetre() {	
		rotate360DegresBis();
		RobotConfig.MOTOR_RIGHT.setSpeed(200) ;
		rotate360Degres() ;
	}
	


}

package robotSuiveurLigne;

import lejos.nxt.LCD;
import lejos.nxt.Sound;
import lejos.robotics.navigation.DifferentialPilot;


public class RobotSuiveurLigne {
	
	private DifferentialPilot pilote = new DifferentialPilot(RobotConfig.WHEEL_DIAMETER, RobotConfig.TRACK_WIDTH, RobotConfig.MOTOR_LEFT, RobotConfig.MOTOR_RIGTH) ;
	private int[] notes = {2349,115,0,5,1760,165,0,35} ;
	
	/*
	 * Construct a new line following object
	 */
	public RobotSuiveurLigne() {
		RobotConfig.MOTOR_LEFT.setSpeed(RobotConfig.VITESSE_DEFAUT) ;
		RobotConfig.MOTOR_RIGTH.setSpeed(RobotConfig.VITESSE_DEFAUT) ;
		RobotConfig.MOTOR_HEAD.setSpeed(RobotConfig.VITESSE_TETE) ;
		this.pilote.setRotateSpeed(RobotConfig.VITESSE_ROTATION) ;
//		this.pilote.setTravelSpeed(RobotConfig.VITESSE_DEFAUT) ;
		RobotConfig.MOTOR_LEFT.setSpeed(400) ;
		RobotConfig.MOTOR_RIGTH.setSpeed(400);

	}
	
	/*
	 * Determines if the robot has arrived at an intersection 
	 */
	
	protected boolean atIntersection() {
		return RobotConfig.LIGHT_RIGTH.getLightValue() > RobotConfig.VALEUR_SEUIL
				&& RobotConfig.LIGHT_LEFT.getLightValue() > RobotConfig.VALEUR_SEUIL ;
	}
/*	
	public void followLineUntilIntersection() {
	
		LCD.drawString("OK OK OK ", 0, 0) ;
		LCD.drawString("Après Boucle ", 1, 1) ;
		boolean go = true;
		while(go) {
			
			if (RobotConfig.LIGHT_RIGTH.getLightValue() > RobotConfig.VALEUR_SEUIL) {
				LCD.drawString("LUMIERE D", 3, 3) ;
				pilote.setTravelSpeed(20) ;
				while(RobotConfig.LIGHT_RIGTH.getLightValue() > RobotConfig.VALEUR_SEUIL) {
					pilote.rotateRight() ; 
					pilote.forward() ;
				}
				RobotConfig.MOTOR_RIGTH.setSpeed(200/4) ;
			} else if(RobotConfig.LIGHT_LEFT.getLightValue() > RobotConfig.VALEUR_SEUIL){
				LCD.drawString("IF LUMIERE GAUCHE BLANC", 2, 2) ;
				pilote.setTravelSpeed(20) ;
				while(RobotConfig.LIGHT_LEFT.getLightValue() > RobotConfig.VALEUR_SEUIL) {
					pilote.rotateLeft() ;pilote.forward() ; 
				}
				RobotConfig.MOTOR_LEFT.setSpeed(200/4) ;			
			}else {
				RobotConfig.MOTOR_RIGTH.setSpeed(200) ;
				RobotConfig.MOTOR_LEFT.setSpeed(200) ;
			}
			pilote.setTravelSpeed(RobotConfig.VITESSE_DEFAUT) ;
			pilote.forward() ;
			if(atIntersection()) {
				go = false ;
				pilote.stop() ;
			}
		}
		
	
		
	}
	*/
	
	public void followLineUntilIntersection() {
	
		while(!atIntersection()) {
			
			if (RobotConfig.LIGHT_RIGTH.getLightValue() > RobotConfig.VALEUR_SEUIL) {
				LCD.drawString("LUMIERE D", 3, 3) ;
				RobotConfig.MOTOR_RIGTH.setSpeed(200/4) ;
			} else if(RobotConfig.LIGHT_LEFT.getLightValue() > RobotConfig.VALEUR_SEUIL){
				LCD.drawString("IF LUMIERE GAUCHE BLANC", 2, 2) ;
				RobotConfig.MOTOR_LEFT.setSpeed(200/4) ;			
			}else {
				RobotConfig.MOTOR_LEFT.setSpeed(400) ;
				RobotConfig.MOTOR_RIGTH.setSpeed(400);
			}
			RobotConfig.MOTOR_LEFT.forward() ;
			RobotConfig.MOTOR_RIGTH.forward() ;
		}
		
		RobotConfig.MOTOR_LEFT.stop() ;
		RobotConfig.MOTOR_RIGTH.stop() ;
		
	}

	public void turnLeftAtJunction() {
		RobotConfig.MOTOR_RIGTH.forward() ;
		RobotConfig.MOTOR_LEFT.forward() ;
		RobotConfig.MOTOR_LEFT.setSpeed(200/4) ;
	}
	
	public void turnRigthAtJunction() {
		RobotConfig.MOTOR_RIGTH.forward() ;
		RobotConfig.MOTOR_LEFT.forward() ;
		
	}
	
	public void robotDrive(int direction) {
		while(true) {
			this.followLineUntilIntersection() ;
			if(direction == 0) 
				this.turnLeftAtJunction() ;
			else if(direction == 1)
				this.turnRigthAtJunction() ;
		}
	}
	
	public void pause(int delay) {
		try {
			Thread.sleep(delay) ;
		} catch (InterruptedException ie) {
			
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
		RobotConfig.MOTOR_RIGTH.forward() ;
		Sound.pause(1000) ;
		RobotConfig.MOTOR_LEFT.backward() ;
		RobotConfig.MOTOR_RIGTH.backward() ;
		Sound.pause(1000) ;
		RobotConfig.MOTOR_LEFT.stop() ;
		RobotConfig.MOTOR_RIGTH.stop() ;
	}
	
	public void rotate360Degres() {
		RobotConfig.MOTOR_RIGTH.forward() ;
		int degreReel = 0 ;
		while(degreReel < 360) 
			degreReel = RobotConfig.MOTOR_RIGTH.getTachoCount() ;
		RobotConfig.MOTOR_RIGTH.flt() ;
		LCD.drawInt(degreReel, 0, 1) ;
		while(RobotConfig.MOTOR_RIGTH.getRotationSpeed() > 0) ;
		LCD.drawInt(RobotConfig.MOTOR_RIGTH.getTachoCount(), 7, 1) ;
	}


}

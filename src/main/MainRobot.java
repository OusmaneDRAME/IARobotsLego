package main;

import lejos.nxt.Button;
import robotSuiveurLigne.RobotSuiveurLigne;

public class MainRobot {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RobotSuiveurLigne robotSuiveurLigne = new RobotSuiveurLigne() ;
		Button.waitForAnyPress();
		robotSuiveurLigne.parcourir();
		//robotSuiveurLigne.avancerEnSuivantLigne() ;
/*		robotSuiveurLigne.jouerSons() ;
		robotSuiveurLigne.utilisationDuTachymetre() ;*/
		

	}

}

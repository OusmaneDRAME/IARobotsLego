package main;

import java.util.LinkedList;
import java.util.List;

import communication.BTReceive;
import graphe.Aetoile;
import graphe.Arc;
import graphe.Graphe;
import graphe.GrapheTest;
import graphe.Sommet;
import lejos.nxt.Button;
import robotSuiveurLigne.RobotSuiveurLigne;
import utils.Direction;

public class MainRobot {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		RobotSuiveurLigne robotSuiveurLigne = new RobotSuiveurLigne() ;
		
		/*GrapheTest test = new GrapheTest();
		Graphe graphe = test.graphe1();
		Aetoile aetoile = new Aetoile();
		aetoile.setGraphe(graphe);
		List<Arc> arcs = new LinkedList<Arc>();
		List<Arc> arcH = new LinkedList<Arc>();
		arcs.add(graphe.getArc(graphe.getSommet('A'), graphe.getSommet('B')));
		arcH.add(graphe.getArc(graphe.getSommet('A'), graphe.getSommet('B')));
		arcs.add(graphe.getArc(graphe.getSommet('B'), graphe.getSommet('D')));
		arcH.add(graphe.getArc(graphe.getSommet('B'), graphe.getSommet('D')));
		arcs.add(graphe.getArc(graphe.getSommet('C'), graphe.getSommet('D')));
		arcH.add(graphe.getArc(graphe.getSommet('C'), graphe.getSommet('D')));
		aetoile.setButs(arcs);
		List<Arc> hopital = new LinkedList<Arc>();
		hopital.add(graphe.getArc(graphe.getSommet('C'), graphe.getSommet('A')));
		arcH.add(graphe.getArc(graphe.getSommet('C'), graphe.getSommet('A')));
		aetoile.setHopitaux(hopital);
		aetoile.setButsHopitaux(arcH);
		aetoile.sauverVictimes(graphe.getArc(graphe.getSommet('B'), graphe.getSommet('C')));*/
		
		
		
		
		BTReceive comm = new BTReceive(robotSuiveurLigne);
		Button.waitForAnyPress();
		comm.start();
		while (robotSuiveurLigne.getParcours().size() == 0 || robotSuiveurLigne.getParcours().get(0) != Direction.STOP) {
			robotSuiveurLigne.parcourir();
		}
		
		
		
		//Button.waitForAnyPress();
		//robotSuiveurLigne.initParcours();
		//robotSuiveurLigne.parcourir(robotSuiveurLigne.getParcours());
		//robotSuiveurLigne.initParcours();
		//robotSuiveurLigne.parcourir(robotSuiveurLigne.getParcours());
		//robotSuiveurLigne.testSensorLevel();
		

	}

}

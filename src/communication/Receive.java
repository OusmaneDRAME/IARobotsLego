package communication;

import java.io.IOException;

import lejos.nxt.LCD;
import lejos.nxt.comm.NXTConnection;
import utils.Direction;

public class Receive extends Thread{
	
	private NXTConnection connection = null;
	private BTReceive comm = null;
	
	public Receive(NXTConnection connection, BTReceive comm) {
		super();
		this.connection = connection;
		this.comm = comm;
	}
	
	public void receive () throws IOException {
		char command;
		do {
			command = comm.receiveChar();
			switch (command) {
			case 'q' :
				comm.parcours.add(Direction.GAUCHE);
				break;
			case 'd' :
				comm.parcours.add(Direction.DROITE);
				break;
			case 'e' :
				comm.parcours.add(Direction.DEMITOUR);
				
			default :
				break;
			}
		} while (command != '0');
		LCD.clear();
		comm.robot.setParcours(comm.parcours);
		LCD.drawString(comm.robot.getParcours().toString(), 0, 2);
	}
	
	public void run() {
		try {
			this.receive();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	

}

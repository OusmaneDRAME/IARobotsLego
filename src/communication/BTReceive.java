package communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;
import robotSuiveurLigne.RobotSuiveurLigne;
import utils.Direction;

public class BTReceive extends Thread{
	
	private NXTConnection connection = null;
	private DataInputStream dis = null;
	private DataOutputStream dos = null;
	protected RobotSuiveurLigne robot;
	public List<Direction> parcours = null;
	
	public BTReceive(RobotSuiveurLigne robot) {
		super();
		this.robot = robot;
		this.robot.setCommunicator(this);
	}
	

	public void run () {
		try {
			this.receive();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void receive () throws Exception{
		String connected = "Connected";
		String waiting = "Waiting...";
		String closing = "Closing...";
		boolean isConnected = false;
		
		while (!isConnected) {
			LCD.drawString(waiting, 0, 0);
			String address = Bluetooth.getLocalAddress();
			LCD.drawString(address, 0, 1);
			connection = Bluetooth.waitForConnection();
			LCD.clear();
			LCD.drawString(connected, 0, 0);
			isConnected = true;
			dis = connection.openDataInputStream();
			dos = connection.openDataOutputStream();
			
			
			
			this.receiveDirection();
			
			
			
			
			LCD.clear();
			LCD.drawString(closing, 0, 0);
			dis.close();
			dos.close();
			connection.close();
			LCD.clear();
		}
	}
	
	public void sendChar (char c) throws IOException {
		dos.writeChar(c);
		dos.flush();
	}
	
	protected char receiveChar () throws IOException {
		char c = dis.readChar();
		LCD.drawChar(c, 7, 1);
		return c;
	}
	
	private void controlRobot () throws IOException {
		char command = '0';
		
		while(command != 'a') {
			command = this.receiveChar();
			switch (command) {
			case 'a' : 
				break;
			case 'z' :
				robot.forward(1000);
				break;
			case 'q' :
				robot.rotateLeft90();
				break;
			case 's' :
				robot.backward(1000);
				break;
			case 'd' :
				robot.rotateRight90();
				break;
			default :
				break;
			}
			this.sendChar(command);
		}
	}
	
	private void controlDirection () throws IOException {
		char command = '0';
		List<Direction> parcours = new LinkedList<>();
		
		while(command != 'a') {
			command = this.receiveChar();
			switch (command) {
			case 'a' : 
				LCD.clear();
				parcours.add(Direction.STOP);
				robot.setParcours(parcours);
				LCD.drawString(robot.getParcours().toString(), 0, 2);
				break;
			case 'q' :
				LCD.clear();
				parcours.add(Direction.GAUCHE);
				robot.setParcours(parcours);
				LCD.drawString(robot.getParcours().toString(), 0, 2);
				break;
			case 'd' :
				LCD.clear();
				parcours.add(Direction.DROITE);
				robot.setParcours(parcours);
				LCD.drawString(robot.getParcours().toString(), 0, 2);
				break;
			case 'e' :
				LCD.clear();
				parcours.add(Direction.DEMITOUR);
				robot.setParcours(parcours);
				LCD.drawString(robot.getParcours().toString(), 0, 2);
			default :
				break;
			}
			this.sendChar(command);
		}
	}
	
	private List<Direction> receiveDirection () throws IOException, InterruptedException {
		
		Receive receptor = new Receive (this.connection,this);
		receptor.start();
		
		while (robot.getCommand() != 'a') {LCD.drawChar(robot.getCommand(), 0, 3);
			if (robot.getCommand() == 'q' || robot.getCommand() == 'd' || robot.getCommand() == 'e') {
				this.sendChar(robot.getCommand());
				LCD.drawString("envoi", 0, 3);
				robot.setCommand('0');
			}
			/*else {
				do {
					command = this.receiveChar();
					switch (command) {
					case 'q' :
						this.parcours.add(Direction.GAUCHE);
						break;
					case 'd' :
						this.parcours.add(Direction.DROITE);
						break;
					case 'e' :
						this.parcours.add(Direction.DEMITOUR);
						
					default :
						break;
					}
				} while (command != '0');
				
				LCD.clear();
				robot.setParcours(parcours);
				LCD.drawString(robot.getParcours().toString(), 0, 2);
			}*/
		}
		
		return parcours;
	}

}

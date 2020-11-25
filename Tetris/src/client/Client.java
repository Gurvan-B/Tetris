package client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JOptionPane;

import main.Player;
import main.World;
import packets.PlayerPacket;


public class Client implements Runnable{
	
	private 	ObjectOutputStream 	out;
	private 	Socket 				s;
	private 	boolean 			running;
	private 	boolean 			isLeft;
	private 	boolean 			isLeftDefined;
	protected 	World 				w;
	public		Object				lockDefineLeft;
	public		final boolean		showLogs = false;
	public		boolean				opponentJustOver;
	
	
	public Client(World w) {
		this.w = w;
		running =  false;
		isLeftDefined = false;
		opponentJustOver = false;
		lockDefineLeft = new Object();
		if (connect()) {
			ServerConnection servConn = new ServerConnection(s,this);
			new Thread(servConn).start();
			displayMessageLog("Waiting for isLeft...",true);
			synchronized(lockDefineLeft) {
				try {
					lockDefineLeft.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} else System.exit(1);
	}
	
	@Override
	public void run() {
		
		try {
			displayMessageLog("Starting to send",true);
			

			try {
				while (running) {
				sendPlayer();
				Thread.sleep(200);
				
				}
			} catch (IOException e) {
				e.printStackTrace();
				displayMessageLog("Server Closed",true);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
		
		displayMessageLog("Stopped to send",true);
		} 
//		catch (IOException e) {
//			e.printStackTrace();
//			displayMessageLog("Server Closed",true);
//		}
		finally {
//			displayMessageLog("disconnecting", true);
//			disconnect();
			try {
				sendPlayer();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void sendPlayer() throws IOException {

		out.reset();
		if (getisLeftDefined() == true) {
			if (getisLeft()) {
				out.writeObject(new PlayerPacket(w.leftPlayer));
			}
			else {
				out.writeObject(new PlayerPacket(w.rightPlayer));
			}
			out.flush();
		}
	}
	
	public void displayMessageLog(String message, boolean displayAnyway) {
		if (showLogs || displayAnyway) System.out.println("[CLIENT] " + message);
	}
	
	public void startSending() {
		if (!running) {
			new Thread(this).start();
			running = true;
		}
		
	}
	
	public void stopSending() {
		running = false;
	}

	public boolean getisLeft() { // TODO rajouter le synchronized
			return isLeft;
	}
	
	public void setIsleft(boolean isLeft) {
			this.isLeft = isLeft;
			w.playingLeft=isLeft;
			w.bind.updatePlayer();
	}
	
	public boolean getisLeftDefined() {
			return isLeftDefined;
	}
	
	public void setIsLeftDefined(boolean b) {
			isLeftDefined = b;
	}
	
	public void updateOtherPlayer(Player p) {
		if (isLeft) w.rightPlayer = p;
		else w.leftPlayer = p;
	}
	
	public void sendToOther (Object o) {
		try {
			out.writeObject(o);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean connect(){
//		String ip = "192.168.137.1";
//		int port = 6585;
		String ip = "";
		int port = 0;
		ip = JOptionPane.showInputDialog(null,"Enter ip adress", "Connect to a server",JOptionPane.PLAIN_MESSAGE);
//			System.out.println(ip);
		String portSt = JOptionPane.showInputDialog(null,"Enter port adress", "Connect to a server",JOptionPane.PLAIN_MESSAGE);
//			System.out.println(portSt);
		try {
			port = Integer.parseInt(portSt);
		} catch (NumberFormatException e) {
		}
		try {
			s = new Socket(ip,port);
			out = new ObjectOutputStream(s.getOutputStream());
			return true;
		} catch (IOException e) {
//			e.printStackTrace();
			displayMessageLog("Unable to connect to " + ip + ":" + port,true);
			JOptionPane.showMessageDialog(null, "Unable to connect to " + ip + ":" + port,"Connection error",JOptionPane.WARNING_MESSAGE);
			return false;
		}
		
	}
	
	public void disconnect() {
		try {
			out.close();
			s.close();
			running = false;
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
}

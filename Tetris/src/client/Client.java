package client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import main.Player;
import main.World;


public class Client implements Runnable{
	
	private 	ObjectOutputStream 	out;
	private 	Socket 				s;
	private 	boolean 			running;
	private 	boolean 			isLeft;
	private 	boolean 			isLeftDefined = false;
	private 	World 				w;
	
	
	public Client(World w) {
		this.w = w;
		running =  false;
	}
	
	@Override
	public void run() {
		
		connect();
		
		ServerConnection servConn = new ServerConnection(s,this);
		
		new Thread(servConn).start();
		
		running = true;
		
		try {
			System.out.println("[CLIENT] Starting...");
			
		while (running) {
			out.reset();
			if (getisLeftDefined() == true) {
				if (getisLeft()) {
					out.writeObject(w.leftPlayer);
//					System.out.println("[CLIENT] " + " Sent " + w.leftPlayer);
				}
				else {
					out.writeObject(w.rightPlayer);
//					System.out.println("[CLIENT] " + " Sent " + w.rightPlayer);
				}
				out.flush();
			} 
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("[CLIENT] Server Closed");
		} finally {
			disconnect();
		}
		
	}

	public boolean getisLeft() { // TODO rajouter le synchronized
			return isLeft;
	}
	
	public void setIsleft(boolean isLeft) {
			this.isLeft = isLeft;
	}
	
	public boolean getisLeftDefined() {
			return isLeftDefined;
	}
	
	public void setIsLeftDefined(boolean b) {
			isLeftDefined = b;
	}
	
	public void updatePlayer(Player p) {
		if (isLeft) w.rightPlayer = p;
		else w.leftPlayer = p;
	}
	
	public void connect(){
		try {
			s = new Socket("192.168.1.32",6585);
			out = new ObjectOutputStream(s.getOutputStream());
			
		} catch (IOException e) {
			e.printStackTrace();
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

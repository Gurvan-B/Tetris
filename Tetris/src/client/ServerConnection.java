package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import main.Player;



public class ServerConnection implements Runnable{

	private 	Socket				server;
	private 	ObjectInputStream 	in;
	private		Client 				client;
	private 	boolean				running;
	
	public ServerConnection(Socket socket, Client client) {
		this.server = socket;
		this.client = client;
		running = false;
		try {
			in = new ObjectInputStream(server.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		try {
			running = true;
			while (running) {
				Object response = in.readObject();
				
			if (response instanceof Boolean) {
				client.setIsleft( (boolean) response );
				client.setIsLeftDefined(true);
				
				System.out.println("[CLIENT] Set isLeft to " + client.getisLeft());
//				System.out.println("[CLIENT] Set isLeftDefined to " + client.getisLeftDefined());
			}
			else if (response instanceof Player) {
				System.out.println("[CLIENT] Recu :" + response);
				client.updatePlayer( (Player)response); // Updates the other side player from client
			}
			else System.out.println("Objet non identifie");
			}
			
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace(); // A retirer plus tard
//			System.out.println("Connection lost"); TODO
		}
		finally {
			try {
				in.close();
				server.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}

}

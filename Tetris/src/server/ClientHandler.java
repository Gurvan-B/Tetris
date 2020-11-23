package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import main.Player;


public class ClientHandler implements Runnable{

	private 	Socket 				client;
	private 	ObjectOutputStream 	out;
	private 	ObjectInputStream 	in;
	private 	boolean 			isLeft;
	private 	Server 				server;
	 
	public ClientHandler(Socket clientSocket, boolean isLeft, Server server) throws IOException {
		this.server = server;
		this.client = clientSocket;
		out = new ObjectOutputStream(client.getOutputStream());
		in = new ObjectInputStream(client.getInputStream());
		this.isLeft = isLeft;
	}
	
	@Override
	public void run() {
		try {
			sendToClient(isLeft);
			while (true) {
				Object request = in.readObject();
				System.out.println("[SERVER] " + isLeft + " | " + request);
					
				if (request instanceof Player) {
					server.sendToTheClient(!isLeft,request);
				} else System.out.println("[SERVER] Object rejected, did not sent");
				
			}
			} catch (IOException e) {
				e.printStackTrace(); // TODO Retirer l'affichage de l'erreur
				
				if (isLeft) {
					System.out.println("[SERVER] Left player disconnected");
					server.stopLeft();
				}
				else {
					System.out.println("[SERVER] Right player disconnected");
					server.stopRight();
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			finally {
				try {
					in.close();
					out.close();
					client.close();
				} catch (IOException e) {
					e.printStackTrace();	// TODO A Retirer
				}
			}
	}
	
	public void sendToClient(Object request) {
		try {
			out.writeObject(request);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		};
	}

}

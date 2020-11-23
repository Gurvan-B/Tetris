package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server implements Runnable{

	private 	ServerSocket 	sS;
	private 	boolean 		running;
	private 	ClientHandler 	leftClient;
	private 	ClientHandler 	rightClient;
	
	
//	private ArrayList<ClientHandler> clients = new ArrayList<>();
//	private ExecutorService pool = Executors.newFixedThreadPool(4); // TODO
	
	public Server () {
		running = false;
		leftClient = null;
		rightClient = null;
	}
	
	@Override
	public void run() {
		
		System.out.println("[SERVER] Starting...");
		try {
			sS= new ServerSocket(6585);
//			sS.setSoTimeout(20000); // TODO
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		running = true;
		while (running) {
			try {
				System.out.println("[SERVER] Waiting for connexion...");
				Socket client = sS.accept();
				System.out.println("[SERVER] Connected !");
				
				if (leftClient == null) {
					ClientHandler clientHandler = new ClientHandler(client,true,this);
					leftClient = clientHandler;
					new Thread(clientHandler).start();
				} else if (rightClient == null) {
					ClientHandler clientThread = new ClientHandler(client,false,this);
					rightClient = clientThread;
					new Thread(clientThread).start();
				} else {
					System.out.println("Server is full !");
				}
//				clients.add(clientThread);
//				pool.execute(clientThread);
//				new Thread(clientThread).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
			

	}
	
	public void sendToTheClient(boolean left, Object o) {
		if (left) {
			if (leftClient != null) leftClient.sendToClient(o);
		} else {
			if (rightClient != null) rightClient.sendToClient(o);
		}
	}
	
	public void stopLeft() {
		leftClient = null;
	}
	
	public void stopRight() {
		rightClient = null;
	}
}

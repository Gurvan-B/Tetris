package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;


public class Server implements Runnable{

	private 	ServerSocket 	sS;
	private 	boolean 		running;
	private 	ClientHandler 	leftClient;
	private 	ClientHandler 	rightClient;
	protected	boolean			leftReady;
	protected	boolean			rightReady;
	protected	boolean			showLogs = false;
	
	
//	private ArrayList<ClientHandler> clients = new ArrayList<>();
//	private ExecutorService pool = Executors.newFixedThreadPool(4); // TODO
	
	public Server () {
		running = false;
		leftClient = null;
		rightClient = null;
	}
	
	@Override
	public void run() {
		int port = 6585;
		String ip = null;
		try {
			sS= new ServerSocket(port);
//			sS.setSoTimeout(20000); // TODO
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		displayMessageLog("IP: " + ip + " | Port: " + port,true);
		displayMessageLog("Starting...",true);
		
		running = true;
		while (running) {
			try {
				displayMessageLog("Waiting for connexion...",true);
				Socket client = sS.accept();
				
				if (leftClient == null) {
					ClientHandler clientHandler = new ClientHandler(client,true,this);
					leftClient = clientHandler;
					displayMessageLog("Left player Connected !",true);
					new Thread(clientHandler).start();
				} else if (rightClient == null) {
					ClientHandler clientThread = new ClientHandler(client,false,this);
					rightClient = clientThread;
					displayMessageLog("Right player Connected !",true);
					new Thread(clientThread).start();
				} else {
					displayMessageLog("Server is full !",true);
				}
//				clients.add(clientThread);
//				pool.execute(clientThread);
//				new Thread(clientThread).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
			

	}
	
	public void displayMessageLog(String message,boolean displayAnyway) {
		if (showLogs || displayAnyway) System.out.println("[SERVER] " + message);
	}
	
	public void setIsReady(boolean b,boolean isLeft) {
		if(isLeft) leftReady = b;
		else rightReady = b;
	}
	
	public boolean bothReady() {
		return leftReady && rightReady;
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
		leftReady = false;
	}
	
	public void stopRight() {
		rightClient = null;
		rightReady = false;
	}
}

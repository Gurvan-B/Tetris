package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


public class Server implements Runnable{

	private 	ServerSocket 	sS;
	private 	boolean 		running;
	private 	ClientHandler 	leftClient;
	private 	ClientHandler 	rightClient;
	protected	boolean			leftReady;
	protected	boolean			rightReady;
	protected	boolean			showLogs = false;
	private		Scanner			sc;
	
	
//	private ArrayList<ClientHandler> clients = new ArrayList<>();
//	private ExecutorService pool = Executors.newFixedThreadPool(4); // TODO
	
	public Server () {
		leftClient = null;
		rightClient = null;
		running = true;
		sc = new Scanner(System.in);
		Thread console = new Thread() {
			@Override
			public void run() {
				while (true) {
				String request = sc.nextLine();
				if (request.charAt(0) == '/') processCommand(request.substring(1));
				}
			}
		};
		console.start();
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
		
		displayMessageLog("Type a command: ",true);
		while (running) {
			try {
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
			} finally {
				stopLeft();
				stopRight();
//				running = false;
			}
		}
			

	}
	
	public void processCommand(String command) {
		switch (command) {
		case "stop" : {
			running = false;
			stopLeft();
			stopRight();
			displayMessageLog("Server stopped", true);
			System.exit(0);
			break;
		}
		case "kick left" :{
			stopLeft();
			displayMessageLog("Kicked left player", true);
			break;
		}
		case "kick right" :{
			stopRight();
			displayMessageLog("Kicked right player", true);
			break;
		}
		default: {
			displayMessageLog("Unknown command", true);
			break;
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
		if (leftClient != null) {
			try {
				leftClient.out.close();
				leftClient.in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			leftClient = null;
		}
		leftReady = false;
	}
	
	public void stopRight() {
		if (rightClient != null) {
			try {
				rightClient.out.close();
				rightClient.in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			rightClient = null;
		}
		rightReady = false;
	}
}

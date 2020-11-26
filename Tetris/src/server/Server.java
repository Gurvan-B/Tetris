package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import packets.IntPacket;


public class Server implements Runnable{

	private 	ServerSocket 	sS;
	private 	boolean 		running;
	private 	ClientHandler 	leftClient;
	private 	ClientHandler 	rightClient;
	protected	boolean			leftReady;
	protected	boolean			rightReady;
	protected	boolean			showLogs = false;
	private		Scanner			sc;
	protected	int				difficulty;
	
	
//	private ArrayList<ClientHandler> clients = new ArrayList<>();
//	private ExecutorService pool = Executors.newFixedThreadPool(4); // TODO
	
	public Server () {
		leftClient = null;
		rightClient = null;
		running = true;
		difficulty = 3;
		sc = new Scanner(System.in);
		Thread console = new Thread() {
			@Override
			public void run() {
				while (true) {
				String request = sc.nextLine();
				if (request.charAt(0) == '/') processCommand(request.substring(1).split(" "));
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
		
		try {
			while (running) {
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
			}
//				clients.add(clientThread);
//				pool.execute(clientThread);
//				new Thread(clientThread).start();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			stopLeft();
			stopRight();
//			running = false;
		}
		
			

	}
	
	public void processCommand(String[] command) {
		
		if (command.length>0) switch (command[0]) {
		case "stop" : {
			running = false;
			stopLeft();
			stopRight();
			displayMessageLog("Server stopped", true);
			System.exit(0);
			break;
		}
		case "kick" :{
			if (command.length>1) switch (command[1]) {
				case "left":{
					stopLeft();
					displayMessageLog("Kicked left player", true);
					break;
				}
				case "right" : {
					stopRight();
					displayMessageLog("Kicked right player", true);
					break;
				}
				default: {
					displayMessageLog("/kick: Enter a valid player", true);
					break;
				}
			} else displayMessageLog("/kick: Enter a player to kick", true);
			break;
		}
		
		case "get":{
			if (command.length>1) switch (command[1]) {
				case "difficulty":{
					displayMessageLog("Difficulty is set to " + difficulty, true);
					break;
				}
				default:{
					displayMessageLog("/get: Enter a valid value", true);
					break;
				}
			} else displayMessageLog("/get: Enter a value to get", true);
			break;
		}
		
		case "set":{
			if (command.length>1) switch (command[1]) {
				case "difficulty":{
					if (command.length>2) {
						int newDifficulty = -1;
						try {
							newDifficulty = Integer.parseInt(command[2]);
						} catch (NumberFormatException e) {
							displayMessageLog("/set: Enter a valid difficulty", true);
						}
						if (newDifficulty > 0 && newDifficulty<10) {
							IntPacket p = new IntPacket (newDifficulty,IntPacket.setDifficulty);
							if (command.length>3) switch (command[3]) {
								case "left": {
									if (leftClient != null) {
									leftClient.sendToClient(p);
									displayMessageLog("Difficulty set to " + newDifficulty + " for left", true);
									} else displayMessageLog("/set: Left player is not connected", true);
									break;
								}
								case "right":{
									if (rightClient != null) {
									rightClient.sendToClient(p);
									displayMessageLog("Difficulty set to " + newDifficulty + " for right", true);
									} else displayMessageLog("/set: Right player is not connected", true);
									break;
								}
								default:{
									displayMessageLog("/set: Enter a valid player", true);
									break;
								}
							} else {
								if (leftClient != null && rightClient != null) {
									leftClient.sendToClient(p);
									rightClient.sendToClient(p);
									difficulty = newDifficulty;
									displayMessageLog("Difficulty set to " + newDifficulty, true);
								} else displayMessageLog("/set: One player is not online", true);
							}
						} else displayMessageLog("/set: Enter a valid difficulty", true);
					}
					break;
				}
				default:{
					displayMessageLog("/set: Enter a valid value", true);
					break;
				}
			} else displayMessageLog("/set: Enter a value to set", true);
			break;
		}
		
		default: {
			displayMessageLog("Unknown command", true);
			break;
		}
		} else displayMessageLog("Enter a command", true);
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

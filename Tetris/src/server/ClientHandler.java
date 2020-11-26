package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import main.Player;
import packets.BooleanPacket;
import packets.Packet;
import packets.PlayerPacket;


public class ClientHandler implements Runnable{

	private 	Socket 				client;
	protected 	ObjectOutputStream 	out;
	protected 	ObjectInputStream 	in;
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
			sendToClient(new BooleanPacket(isLeft,BooleanPacket.isLeftBool));
			while (true) {
				Object packet = in.readObject();
				server.displayMessageLog(isLeft + " | " + packet,false);
					
				if (packet instanceof Packet) {
					if (((Packet)packet).getType() == BooleanPacket.isReadyBool){
						server.setIsReady(((BooleanPacket)packet).getBoolean(), isLeft);
						if (server.bothReady()) {
							server.sendToTheClient(isLeft,new BooleanPacket(true,BooleanPacket.beginBool));
							server.sendToTheClient(!isLeft,new BooleanPacket(true,BooleanPacket.beginBool));
						}
					} else if (packet instanceof PlayerPacket) {
							Player player = ((PlayerPacket)packet).getPlayer();
							if (player.over) {
								server.setIsReady(false,isLeft);
							}
							server.sendToTheClient(!isLeft,packet);
						}
				} else server.displayMessageLog("Object rejected, did not sent",true);
				
			}
			} catch (IOException e) {
//				e.printStackTrace(); // TODO Retirer l'affichage de l'erreur
				
				if (isLeft) {
					server.displayMessageLog("Left player disconnected",true);
					server.stopLeft();
				}
				else {
					server.displayMessageLog("Right player disconnected",true);
					server.stopRight();
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			finally {
//				System.out.println("coucou"+ System.currentTimeMillis());
//				JOptionPane.showMessageDialog(null, "erreur","Connection error coucou",JOptionPane.WARNING_MESSAGE);
//				try {
//					in.close();
//					out.close();
//					client.close();
//				} catch (IOException e) {
//					e.printStackTrace();	// TODO A Retirer
//				}
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

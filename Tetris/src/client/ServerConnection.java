package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import javax.swing.JOptionPane;

import main.Player;
import packets.BooleanPacket;
import packets.Packet;
import packets.PlayerPacket;


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
				Object packet = in.readObject();
				if (packet instanceof Packet) {
					client.displayMessageLog("Recu: " + packet,false);
					if (((Packet)packet).getType()==BooleanPacket.isLeftBool) {
						boolean request = ((BooleanPacket)packet).getBoolean();
						client.setIsleft( (boolean) request );
						client.setIsLeftDefined(true);
						synchronized(client.lockDefineLeft) {
							client.lockDefineLeft.notifyAll();	// TODO notifyAll ou notify
						}
						client.displayMessageLog("Set isLeft to " + client.getisLeft(),true);
					}
					
//					else if (((Packet)packet).getType()==BooleanPacket.isReadyBool) {
//						@SuppressWarnings("unused") // TODO
//						boolean request = ((BooleanPacket)packet).getBoolean();
//						client.w.opponentIsReady=request;
//						client.w.processStartBothReady();
//					}
					
					else if (((Packet)packet).getType()==BooleanPacket.beginBool) {
						@SuppressWarnings("unused") // TODO
						boolean request = ((BooleanPacket)packet).getBoolean();
						client.w.drawChrono = request;
						if (client.w.drawChrono) {
							client.w.reset();
							client.startSending();
						}
					}
					
					else if (packet instanceof PlayerPacket) {
						Player request = ((PlayerPacket)packet).getPlayer();
						if (request.over) {
							client.opponentJustOver = true;
							client.displayMessageLog("Opponent game over", false);
						}
						client.updateOtherPlayer(request);
					}
					else client.displayMessageLog("Packet non identifie: " + packet,true);
					}
				else client.displayMessageLog("Objet non identifie: " + packet,true);
			}
			
		} catch (IOException | ClassNotFoundException e) {
//			e.printStackTrace(); // A retirer plus tard
			client.displayMessageLog("Connection lost",true);
			JOptionPane.showMessageDialog(null, "Connection lost","Connection error",JOptionPane.WARNING_MESSAGE);
		}
		finally {
			try {
				in.close();
				server.close();
				System.exit(0);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}

}

package graphiques;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import main.World;
import packets.BooleanPacket;

public class Mouse implements MouseMotionListener, MouseListener {
	
	private Object mouseLock = new Object();
	public int mouseX;
	public int mouseY;
	private World w;
	private int centerX = 1920/2;
	private int centerY = 620;
	private int sizeX = 200;
	private int sizeY = 100;

	public Mouse (World w) {
		this.w = w;
		this.mouseX = 0;
		this.mouseY = 0;
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		synchronized (mouseLock) {
			mouseX = e.getX();
			mouseY = e.getY();
//			System.out.println("x " + mouseX);
//			System.out.println("y " + mouseY);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		synchronized (mouseLock) {
			mouseX = e.getX();
			mouseY = e.getY();
//			System.out.println("x " + mouseX);
//			System.out.println("y " + mouseY);
		}
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		synchronized (mouseLock) {
			if (!w.playing && !w.drawChrono && centerX-sizeX/2 < mouseX && mouseX < centerX+sizeX/2 && mouseY < centerY+sizeY/2 && centerY-sizeY/2 < mouseY) {
//				w.leftPlayer.restart();
//				w.leftPlayer.restartStats();
//				w.leftPlayer.start = true;
//				
//				w.rightPlayer.restart();
//				w.rightPlayer.restartStats();
//				w.rightPlayer.start = true;
//				w.x= -1;
//				
//				w.musique.play();
				
				w.playerIsReady = !w.playerIsReady;
				w.client.sendToOther(new BooleanPacket(w.playerIsReady, BooleanPacket.isReadyBool ));
//				w.processStartBothReady();
				
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}
	
}

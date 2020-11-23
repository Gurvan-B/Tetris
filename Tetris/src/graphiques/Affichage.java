package graphiques;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import client.Client;
import main.World;


public class Affichage extends JPanel implements ActionListener {

	private 	static final long 	serialVersionUID = -2977860217912678180L;
	private 	final int 			maxFps = 60;
	private 	Timer 				timer;
	protected 	World 				w;
	private 	Client 				client;
	
//	public Object playerLock = new Object();

	public Affichage() {
		w = new World();
		timer = new Timer(1000/maxFps,this);
		w.x = -1;
		
		client = new Client(w);
		new Thread(client).start();
		
//		synchronized (playerLock) {
		w.leftPlayer.restart(); // Instant start
		w.leftPlayer.restartStats();
		w.leftPlayer.start = true;
		
		w.rightPlayer.restart();
		w.rightPlayer.restartStats();
		w.rightPlayer.start = true;
//		w.musique.play();
//		}
		timer.start();
	}
	
	@Override
	public void paintComponent(Graphics g) { // Calcul de l'affichage global ( drawScreen )
		super.paintComponent(g);
		
//		synchronized (playerLock) {
			w.drawScreen(g);
//		}
		
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) { // Les étapes entre chaque refresh ( Start )
		
		// Process Keys TODO
//		System.out.println((int)System.currentTimeMillis());
//		System.out.println(lastTime);
//		if ((int)System.currentTimeMillis() - lastTime > 150 ) {	
		
		
		
//		lastTime = (int) System.currentTimeMillis();
//		}
		
//		synchronized (playerLock) {
		
		w.bind.processKeys();
		
		// Makes a step for in the world
		if (w.leftPlayer.start) { // TODO A changer si l'on veut descendre à la même vitesse selon les fps
			w.leftPlayer.step();
		}
		if (w.rightPlayer.start) {
			w.rightPlayer.step();
		}
		
		// Actions when both players just lost
		if (w.leftPlayer.justOver && w.rightPlayer.justOver) {
			w.gameOverActions();
		}
//		}
		
//		w.rightPlayer.showMatrice();
//		w.leftPlayer.showPlacedTiles();
//		w.leftPlayer.shape.showLayout();
		
		this.repaint();
		
	}
	

}

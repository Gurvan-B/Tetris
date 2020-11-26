package graphiques;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import client.Client;
import main.World;


public class Affichage extends JPanel implements ActionListener {

	private 	static final long 	serialVersionUID = -2977860217912678180L;
	private 	final int 			maxFps = 600;
	private 	Timer 				timer;
	protected 	World 				w;
	private 	Client 				client;
//	private		boolean				sendPlayer;
	private		int					refreshLastSecond;
	private		long				timeLastSecond;
	private		int					lastFPS;
	
//	public Object playerLock = new Object();

	public Affichage() {
		w = new World();
		timer = new Timer(1000/maxFps,this);
		
//		String portSt = JOptionPane.showInputDialog(null,"Enter port adress", "Connect to a server",JOptionPane.PLAIN_MESSAGE);
		
		w.defineLocal();
		if (!w.local) {
			client = new Client(w);
			w.setClient(client);
		} else {
			w.setDifficulty();
			w.bind.updatePlayers();
		}
		
//		w.reset();
//		w.playing=true;
//		w.playingLeft=true;
//		w.bind.updatePlayer();
//		w.processStartBothReady();
		refreshLastSecond = 0;
		lastFPS = 0;
		timeLastSecond = System.currentTimeMillis();
		timer.start();
	}
	
	@Override
	public void paintComponent(Graphics g) { // Calcul de l'affichage global ( drawScreen )
		super.paintComponent(g);
		
//		synchronized (playerLock) {
		w.drawScreen(g,lastFPS);
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
		
		
//		if (w.bothReady()) w.startPlaying();
		
		
		
		
		
		updateFPS();
		w.bind.processKeys();
		
		// Makes a step for in the world
		if (w.leftPlayer.start && (w.local || w.playingLeft))  { // TODO A changer si l'on veut descendre à la même vitesse selon les fps
			w.leftPlayer.step();
		}
		if (w.rightPlayer.start && (w.local || !w.playingLeft)) {
			w.rightPlayer.step();
		}
		
		// Actions when both players just lost
//		if (w.getPlayingPlayer().justOver) System.out.println("PlayingjustOver");
//		if (client.opponentJustOver) System.out.println("clientJustOver");
		if (w.local) {
			if (w.leftPlayer.localPlayerJustOver && w.rightPlayer.localPlayerJustOver) {
				w.gameOverActions();
			}
		}
		else if (!w.getPlayingPlayer().start && client.opponentJustOver) {
			w.gameOverActions();
		}
//		}
		
//		w.rightPlayer.showMatrice();
//		w.leftPlayer.showPlacedTiles();
//		w.leftPlayer.shape.showLayout();
		
		this.repaint();
		
	}
	
	public void updateFPS() {
		if (timeLastSecond+1000 < System.currentTimeMillis()) {
			timeLastSecond = System.currentTimeMillis();
//			client.displayMessageLog("FPS: " + refreshLastSecond,true);
			lastFPS = refreshLastSecond;
			refreshLastSecond = 0;
		} else {
			refreshLastSecond++;
		}
	}
	
	

}

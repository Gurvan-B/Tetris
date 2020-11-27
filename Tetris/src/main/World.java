package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import client.Client;
import display.AudioFile;
import display.Vector2;
import graphiques.Bindings;
import graphiques.Mouse;
import shape.*;

public class World {

	public 	Player 			leftPlayer;
	public 	Player 			rightPlayer;
//	private StartButton 	startButton;
//	private Bindings 		bind;
	public AudioFile 		musique;
	private long 			milliSecSinceYouWin;
	public 	int				x;
	private BufferedImage 	logo;
	private BufferedImage 	fond;
	private BufferedImage	background;
	public 	Bindings		bind;
	public 	Mouse 			mouse;
	public	boolean			playingLeft;
	public	boolean			playerIsReady; // If this world is ready to start or not
	public	boolean			playing;
	public	Client			client;
	private long			millisSecSinceChrono;
	private int 			chrono;
	public boolean 			drawChrono;
	public boolean			showFps;
	private TextureLoader	tl;
	public	boolean			local;
	private int				difficulty;
	private AudioFile 		won;
	private AudioFile 		lost;
//	private AudioFile 		countdown;
//	private AudioFile 		go;
	public AudioFile 		countdown;
	
	public World() {
		leftPlayer = new Player(true);
		rightPlayer = new Player(false);
		musique = new AudioFile("sound_effects/musique",true);
		won = new AudioFile("sound_effects/won",false);
		lost = new AudioFile("sound_effects/lost",false);
//		countdown = new AudioFile("sound_effects/countdown",false);
//		go = new AudioFile("sound_effects/go",false);
		countdown = new AudioFile("sound_effects/countdown",false);
		milliSecSinceYouWin = 0;
		millisSecSinceChrono=0;
		chrono = 4;
		x= -1; // hors de l'écran de base
		mouse = new Mouse(this);
		bind = new Bindings(this);
		playerIsReady = false;
		playing = false;
		difficulty = 3;
		fond = loadImage("images/fond");
		logo = loadImage("images/logo");
		background = loadImage("images/background");
		showFps = false;
		tl = new TextureLoader();
	}
	
//	public BufferedImage loadImage(String fileName) {
//		try {	
//			return ImageIO.read(new File(FileSystems.getDefault().getPath("").toAbsolutePath() + "\\Ressources\\" + fileName + ".png"));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
	
	BufferedImage loadImage(String fn) {
		InputStream inputStr = this.getClass().getResourceAsStream("/" + fn + ".png");
			
		BufferedImage image = null;
		
		try {
			image = ImageIO.read(inputStr);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}
	
//	public BufferedImage loadImage(String fileName) {
//		try {	
//			return ImageIO.read(new File("C:\\Users\\GURVAN\\git\\Tetris\\Tetris\\ressources\\" + fileName + ".png"));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
	
	public void reset() {
		leftPlayer.restart();
		leftPlayer.restartStats();
		
		rightPlayer.restart();
		rightPlayer.restartStats();
		x=-1;
	}
	
	public void defineLocal() {
		int answer = JOptionPane.showConfirmDialog(null, "Connect to a server ?","Play local/online", JOptionPane.YES_NO_OPTION);
		switch (answer) {
		case 0: {
			local = false;
			break;
		}
		case 1: {
			local = true;
			break;
		}
		case -1:{
			System.exit(0);
			break;
		}
		default:{
			System.exit(0);
		}
		}
	}
	
	public void setDifficulty() {
		String[] difficulties = {"1","2","3","4","5","6","7","8","9"};
		String difficultySt = (String) JOptionPane.showInputDialog(
				null, "Choose a difficulty ?","Difficulty",
				JOptionPane.WARNING_MESSAGE,null,
				difficulties, difficulties[2] );
		setDifficultyBothPlayers(Integer.parseInt(difficultySt));
	}
	
	public void setDifficultyBothPlayers(int newDifficulty) {
		difficulty = newDifficulty;
		leftPlayer.setDifficulty(difficulty);
		rightPlayer.setDifficulty(difficulty);
	}
	
	public int getDifficulty() {
		return difficulty;
	}
	
	public void processChrono() {
		if (drawChrono) {
			if (millisSecSinceChrono+1000 <= System.currentTimeMillis()) {
				if(chrono != 0) {
					chrono --;
					if (chrono != 0) {
//						countdown.restart();
					}
					else{
//						go.restart();
					}
				} else {
					startPlaying();
					drawChrono = false;
					chrono = 4;
					musique.play();
				}
			millisSecSinceChrono = System.currentTimeMillis();
			}
		}
	}
	
	public void drawChrono(Graphics g) {
		if (drawChrono) {
			String value = chrono +"";
			if (chrono == 0) value = "GO";
			if(local) {
				drawBox(g, 270, 100, 200, 100, 10, 10, Color.lightGray,Color.white);
				drawText(g, 270, 100, value, Color.green);
				
				drawBox(g, 270+1380, 100, 200, 100, 10, 10, Color.lightGray,Color.white);
				drawText(g, 270+1380, 100, value, Color.green);
			} else {
				int x=0;
				if (!playingLeft) x = 1380;
				drawBox(g, 270+x, 100, 200, 100, 10, 10, Color.lightGray,Color.white);
				drawText(g, 270+x, 100, value, Color.green);
			}
		}
	}
	
	public void drawFPS(Graphics g, int lastFPS) {
		if (showFps) drawText(g, 1900, 10, lastFPS+"", Color.green);
	}
	
//	public boolean getPlaying() {
//		return (playing);
//	}
	
	public void setClient(Client client) {
		this.client = client;
	}
	
	public Player getPlayingPlayer() {
		if (playingLeft) return leftPlayer;
		else return rightPlayer;
	}
	
	public Player getOpponent() {
		if (playingLeft) return rightPlayer;
		else return leftPlayer;
	}
	
	// TODO Faire apparaitre les shapes tout en haut
	
	public void gameOverActions() { // mettre tout ça dans une classe World et renommer World "Game" ou "Player" pour mettre toutes les fonctions supérieures dans World
		musique.stop();
		boolean vicL = leftPlayer.score>rightPlayer.score; // conditions de victoires
		boolean vicR = leftPlayer.score<rightPlayer.score;
		if (vicL) { // Droite victorieux
			x = 0;
		} else if (vicR) { // Gauche victorieux
			x = 1380;
		} else {
			System.out.println("EGALITE");
		}
				
//		startButton = new StartButton( new Vector2<Double>(0.3,0.175) , new Vector2<Double>(0.03,0.015)  );
			
//		getPlayingPlayer().start = false;
//		rightPlayer.start = false; TODO
			
//		getPlayingPlayer().justOver = false;
		
		leftPlayer.localPlayerJustOver = false;
		rightPlayer.localPlayerJustOver = false;
		
		playing = false;
		playerIsReady = false;
		if (!local) {
			client.opponentJustOver = false;
			client.stopSending();
		}
		
		if ( (!local && (getPlayingPlayer().score <= getOpponent().score))) {
			lost.restart();
		} else {
			won.restart();
		}
		
	}
	
	public void drawYouWin(Graphics g) {
		if ( x >= 0 ){
			if (milliSecSinceYouWin+400 >= System.currentTimeMillis()) {
			drawBox(g, 270+x, 100, 200, 100, 10, 10, Color.lightGray,Color.white);
			drawText(g, 270+x, 100, "YOU WIN", Color.green);
			} else if (milliSecSinceYouWin+800 >= System.currentTimeMillis()) {
			} else {
				milliSecSinceYouWin = System.currentTimeMillis();
			}
		}
		
	}
	
	public void drawNextShape(Graphics g, Player player, int centerX, int centerY) {
		
		switch (player.nextShape) {
		case 0: { 
			Shape_Class s = new T_Shape(Color.pink,true);	
			s.drawForSideScreen(g, centerX, centerY+27,tl);
			break;
		}
		case 1: {
			Shape_Class s = new I_Shape(Color.cyan,true);	
			s.drawForSideScreen(g, centerX, centerY+27,tl);
			break;
		}
		case 2: {
			Shape_Class s = new J_Shape(Color.blue,true);	
			s.drawForSideScreen(g, centerX+27, centerY,tl);
			break;
		}
		case 3: {
			Shape_Class s = new L_Shape(Color.orange,true);	
			s.drawForSideScreen(g, centerX-27, centerY,tl);
			break;
		}
		case 4: {
			Shape_Class s = new O_Shape(Color.yellow,true);	
			s.drawForSideScreen(g, centerX+27, centerY+27,tl);
			break;
		}
		case 5: {
			Shape_Class s = new Z_Shape(Color.red,true);	
			s.drawForSideScreen(g, centerX, centerY-27,tl);
			break;
		}
		case 6: {
			Shape_Class s = new S_Shape(Color.green,true);	
			s.drawForSideScreen(g, centerX, centerY-27,tl);
			break;
		}
		}
		
	}
	
	public void updateShapePos(Shape_Class s, int x, int y) { // TODO à mettre dans la classe Shape
		for (Tile t : s.getLayout()) {
			t.updatePosition( new Vector2<Integer>(t.getPosition().getX()+x,t.getPosition().getY()+y ));
		}
	}
	
	public void drawScreen(Graphics g, int lastFPS) {
		drawBackground(g);
		leftPlayer.draw(g,tl);
		rightPlayer.draw(g,tl);
		drawSideScreen(g);
		drawYouWin(g);
		processChrono();
		drawChrono(g);
		drawFPS(g,lastFPS);
	}
	
//	public boolean playing() {
////		return (leftPlayer.start==false && rightPlayer.start==false);
//		return ((playingLeft && leftPlayer.start) || (!playingLeft && rightPlayer.start));
//	}
	

	public void startPlaying() {
		leftPlayer.start = rightPlayer.start = true;
		playing = true;
	}

	
	public void drawText(Graphics g, int middleX, int middleY, String text,Color c) {
		g.setColor(c);
		Font font = new Font("SansSerif", Font.PLAIN, 20);
		g.setFont(font);
		FontMetrics metrics = g.getFontMetrics();
		int ws = metrics.stringWidth(text);
		int hs = metrics.getDescent();
		g.drawString(text,middleX-ws/2,middleY+hs*2);
//		g.setColor(Color.red);
//		g.fillRect(middleX, middleY, 1, 1);
	}
	
	public void drawBox(Graphics g, int middleX, int middleY,int sizeX, int sizeY, int borderSizeX, int borderSizeY, Color borderColor, Color middleColor) {
//		int sizeX = 200;
//		int sizeY = 100;
		
//		g.setColor(Color.lightGray);
		g.setColor(borderColor);
		g.fillRect(middleX-sizeX/2, middleY-sizeY/2, sizeX, sizeY);
		
		g.setColor(middleColor);
		g.fillRect(middleX-sizeX/2+borderSizeX, middleY-sizeY/2+borderSizeY, sizeX-2*borderSizeX, sizeY-2*borderSizeX);
		
	}
	
	public void drawRect(Graphics g, int middleX, int middleY,int sizeX, int sizeY, Color color) {
		
		g.setColor(color);
		g.fillRect(middleX-sizeX/2, middleY-sizeY/2, sizeX, sizeY);
		
	}
	
	public void drawBackground(Graphics g) {
		g.drawImage(background,0,0,1920,1080,null);
	}
	
	public void drawSideScreen(Graphics g) {
		
		
		
		
//		g.setColor(Color.black);
//		g.fillRect(x-borderSize, y-borderSize, size+borderSize*2, size+borderSize*2);
//		
//		g.setColor(color);
//		g.fillRect(x+borderSize, y+borderSize, size-borderSize*2, size-borderSize*2);
		
		int resX = 1920;
		int resY = 1080;
		
		int mainBorderSizeX = 20;
		int mainBorderSizeY = 20;
		
		int borderSizeX = 10;
		int borderSizeY = 10;
		
		Color brown = new Color(29,0,0,255);
		g.setColor(brown);
		g.fillRect(540, 0, resX-(2*540), resY);
		
//		g.setColor(Color.gray);
//		g.fillRect(540+mainBorderSizeX, 0+mainBorderSizeY, resX-(2*540)-(2*mainBorderSizeX), resY-(2*mainBorderSizeY));
		
		int widthFond = resX-(2*540)-(2*mainBorderSizeX);
			
		int heightFond = resY-(2*mainBorderSizeY);
			
		g.drawImage(fond, 540+mainBorderSizeX, 0+mainBorderSizeY, widthFond, heightFond, null);
			
		
		int widthLogo = 770/3;
		int heightLogo = 533/3;
		drawRect(g,resX/2,mainBorderSizeY+60+heightLogo/2,widthLogo+mainBorderSizeX+borderSizeX*2,heightLogo+mainBorderSizeY+borderSizeX*2,brown);
		drawRect(g,resX/2,mainBorderSizeY+60+heightLogo/2,widthLogo+mainBorderSizeX,heightLogo+mainBorderSizeY,Color.white);
		g.drawImage(logo, resX/2-widthLogo/2, mainBorderSizeY+60, widthLogo, heightLogo, null);
			
		
//		int yScore = 350;
//		drawBox(g,770,yScore,borderSizeX,borderSizeY);
//		drawText(g,"SCORE: " + leftPlayer.score,770,yScore);
//		drawBox(g,1150,yScore,borderSizeX,borderSizeY);
//		drawText(g,"SCORE: " + rightPlayer.score,1150,yScore);
		
		int sizeX = 200;
		int sizeY = 100;
		Color c = new Color(149,73,39,255);
		Color borderColor = new Color(29,0,0,255);
		
		int yScore = 490;
		drawBox(g,resX/2,yScore,sizeX,sizeY, borderSizeX,borderSizeY,borderColor,Color.white);
		drawText(g,resX/2,yScore,leftPlayer.score + " SCORE " + rightPlayer.score,c);
		
		int yLevel = 400;
		drawBox(g,resX/2-90,yLevel,sizeX,sizeY,borderSizeX,borderSizeY,borderColor,Color.white);
		drawText(g,resX/2-90,yLevel,leftPlayer.level + " LEVEL " + rightPlayer.level,c);
		
		int yLine = 400;
		drawBox(g,resX/2+90,yLine,sizeX,sizeY,borderSizeX,borderSizeY,borderColor,Color.white);
		drawText(g,resX/2+90,yLine,leftPlayer.lineCount + " LINES " + rightPlayer.lineCount,c);
		
		if (!playing && !drawChrono) {
			int yStart = 620;
			drawBox(g,resX/2,yStart,sizeX,sizeY,borderSizeX,borderSizeY,borderColor,Color.white);
			if(playerIsReady) drawBox(g,resX/2,yStart,100,50,5,5,new Color(66,209,54),Color.green);
			drawText(g,resX/2,yStart,"READY",c);
		}
		int yShape = 850;
		int sizeShapeX = 300;
		int sizeShapeY = 300;
		drawBox(g,resX/2-(sizeShapeX/2-borderSizeX),yShape,sizeShapeX,sizeShapeY,borderSizeX,borderSizeY,borderColor,Color.white);
		drawBox(g,resX/2+(sizeShapeX/2-borderSizeX),yShape,sizeShapeX,sizeShapeY,borderSizeX,borderSizeY,borderColor,Color.white);

		if (playing) {
			drawNextShape(g,leftPlayer,resX/2-(sizeShapeX/2-borderSizeX),yShape);
			drawNextShape(g,rightPlayer,resX/2+(sizeShapeX/2-borderSizeX),yShape);
		}
		
	}

	
}

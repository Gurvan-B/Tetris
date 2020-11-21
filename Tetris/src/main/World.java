package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import display.AudioFile;
//import display.Button;
import display.Vector2;
import shape.I_Shape;
import shape.J_Shape;
import shape.L_Shape;
import shape.O_Shape;
import shape.S_Shape;
import shape.Shape_Class;
import shape.T_Shape;
import shape.Tile;
import shape.Z_Shape;

public class World {

	public 	Player 			leftPlayer;
	public 	Player 			rightPlayer;
//	private StartButton 	startButton;
//	private Bindings 		bind;
	public AudioFile 		musique;
	private int 			frameSinceYouWin;
	public int			x;
	private BufferedImage logo;
	private BufferedImage fond;
	
	public World() {
		leftPlayer = new Player(true);
		rightPlayer = new Player(false);
		musique = new AudioFile("musique",true);
		frameSinceYouWin = 0;
		x= -1; // hors de l'écran de base
		
		try {	
			fond = ImageIO.read(new File("C:\\Users\\GURVAN\\eclipse-workspace\\Tetris\\ressources\\fond.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {	
			logo = ImageIO.read(new File("C:\\Users\\GURVAN\\eclipse-workspace\\Tetris\\ressources\\logo.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
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
			
		leftPlayer.start = false;
		rightPlayer.start = false;
			
		leftPlayer.justOver = false;
		rightPlayer.justOver = false;
	}
	
	public void drawYouWin(Graphics g) {
		if ( x >= 0 ){
			if (frameSinceYouWin <= 20) {
			drawBox(g, 270+x, 100, 200, 100, 10, 10, Color.lightGray);
			drawText(g, 270+x, 100, "YOU WIN", Color.green);
			frameSinceYouWin ++;
			} else if (frameSinceYouWin <= 40) {
				frameSinceYouWin++;
			} else {
				frameSinceYouWin = 0;
			}
		}
		
	}
	
	public void drawNextShape(Graphics g, Player player, int centerX, int centerY) {
		
		switch (player.nextShape) {
		case 0: { 
			Shape_Class s = new T_Shape(Color.pink,true);	
			s.drawForSideScreen(g, centerX, centerY+27);
			break;
		}
		case 1: {
			Shape_Class s = new I_Shape(Color.cyan,true);	
			s.drawForSideScreen(g, centerX, centerY+27);
			break;
		}
		case 2: {
			Shape_Class s = new J_Shape(Color.blue,true);	
			s.drawForSideScreen(g, centerX+27, centerY);
			break;
		}
		case 3: {
			Shape_Class s = new L_Shape(Color.orange,true);	
			s.drawForSideScreen(g, centerX-27, centerY);
			break;
		}
		case 4: {
			Shape_Class s = new O_Shape(Color.yellow,true);	
			s.drawForSideScreen(g, centerX+27, centerY+27);
			break;
		}
		case 5: {
			Shape_Class s = new Z_Shape(Color.red,true);	
			s.drawForSideScreen(g, centerX, centerY-27);
			break;
		}
		case 6: {
			Shape_Class s = new S_Shape(Color.green,true);	
			s.drawForSideScreen(g, centerX, centerY-27);
			break;
		}
		}
		
	}
	
	public void updateShapePos(Shape_Class s, int x, int y) { // TODO à mettre dans la classe Shape
		for (Tile t : s.getLayout()) {
			t.updatePosition( new Vector2<Integer>(t.getPosition().getX()+x,t.getPosition().getY()+y ));
		}
	}
	
	public void drawScreen(Graphics g) {
		leftPlayer.draw(g);
		rightPlayer.draw(g);
		drawSideScreen(g);
		drawYouWin(g);
	}
	
	public boolean gameIsOver() {
		return (leftPlayer.start==false && rightPlayer.start==false);
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
	
	public void drawBox(Graphics g, int middleX, int middleY,int sizeX, int sizeY, int borderSizeX, int borderSizeY, Color borderColor) {
//		int sizeX = 200;
//		int sizeY = 100;
		
//		g.setColor(Color.lightGray);
		g.setColor(borderColor);
		g.fillRect(middleX-sizeX/2, middleY-sizeY/2, sizeX, sizeY);
		
		g.setColor(Color.white);
		g.fillRect(middleX-sizeX/2+borderSizeX, middleY-sizeY/2+borderSizeY, sizeX-2*borderSizeX, sizeY-2*borderSizeX);
		
	}
	
	public void drawRect(Graphics g, int middleX, int middleY,int sizeX, int sizeY, Color color) {
		
		g.setColor(color);
		g.fillRect(middleX-sizeX/2, middleY-sizeY/2, sizeX, sizeY);
		
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
		drawBox(g,resX/2,yScore,sizeX,sizeY, borderSizeX,borderSizeY,borderColor);
		drawText(g,resX/2,yScore,leftPlayer.score + " SCORE " + rightPlayer.score,c);
		
		int yLevel = 400;
		drawBox(g,resX/2-90,yLevel,sizeX,sizeY,borderSizeX,borderSizeY,borderColor);
		drawText(g,resX/2-90,yLevel,leftPlayer.level + " LEVEL " + rightPlayer.level,c);
		
		int yLine = 400;
		drawBox(g,resX/2+90,yLine,sizeX,sizeY,borderSizeX,borderSizeY,borderColor);
		drawText(g,resX/2+90,yLine,leftPlayer.lineCount + " LINES " + rightPlayer.lineCount,c);
		
		if (this.gameIsOver()) {
			int yStart = 620;
			drawBox(g,resX/2,yStart,sizeX,sizeY,borderSizeX,borderSizeY,borderColor);
			drawText(g,resX/2,yStart,"START",c);
		}
		int yShape = 850;
		int sizeShapeX = 300;
		int sizeShapeY = 300;
		drawBox(g,resX/2-(sizeShapeX/2-borderSizeX),yShape,sizeShapeX,sizeShapeY,borderSizeX,borderSizeY,borderColor);
		drawBox(g,resX/2+(sizeShapeX/2-borderSizeX),yShape,sizeShapeX,sizeShapeY,borderSizeX,borderSizeY,borderColor);

		if (!gameIsOver()) {
			drawNextShape(g,leftPlayer,resX/2-(sizeShapeX/2-borderSizeX),yShape);
			drawNextShape(g,rightPlayer,resX/2+(sizeShapeX/2-borderSizeX),yShape);
		}
		
	}

	
}

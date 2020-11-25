package main;
import java.util.Collection;
import java.util.LinkedList;
import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import display.AudioFile;
import display.Vector2;
import shape.*;

/**
 * The world contains the ships and draws them to screen.
 */
public class Player implements Serializable{
	
/**
	 * 
	 */
	private static final long serialVersionUID = -2732929164670973602L;
	
	//	private Bindings 	bind;					// The bindings of the game.
//	protected long 				time;					// The current time 
	protected int				score;
	protected int				lineCount;
	protected int				level;
	public Shape_Class 			shape;					// The shape of the player
	int							nextShape;				// The next shape of the player
	protected int[][]			matrice;
	protected Random 			rand;					// Instance of Random to generate random integers.
	protected long				millisecSinceGoDown;
	public boolean				goDownFaster;
//	public	boolean				gameIsOver;
	public	Collection<Tile> 	placedTiles;
//	public 	boolean				refreshAllScreen;
	public	boolean				inPause;
	public	Shape_Class			shadow;
	public	boolean				start;
//	public 	StartButton			startButton;
//	public	AudioFile 			musique;
	public	boolean				over;
	public	boolean				isLeft;
	
	/**
	 * Creates the world with the bindings, the player ship
	 * and the opponent ship.
	 */
	
	public Player(boolean isLeft) {
//		bind = new BindingsLeft(this);
//		time = System.currentTimeMillis();
		over = false;
		this.isLeft = isLeft;
		rand = new Random();
		millisecSinceGoDown = 0;
		goDownFaster = false;
//		gameIsOver = false;
//		refreshAllScreen = false;
		score = 0;
		lineCount = 0;
		level = 0;
		start = false;
		inPause = false;
		placedTiles = new LinkedList<Tile>();
//		player = new I_Shape(StdDraw.CYAN);
		matrice = new int[20+2][10+2]; // Pour utiliser la matrice se décaler des murs ET de 1 car commence à 0 et pos commence à 1 !!
//		showMatrice();
		putWalls();
//		showMatrice();
//		
//		startButton = new StartButton( new Vector2<Double>(0.1,0.2) , new Vector2<Double>(0.03,0.015)  );
		
//		musique = new AudioFile("musique",true);
		
		shape = randomShape(rand.nextInt(7));
		
		
		processHit(2);
		nextShape = rand.nextInt(7);
		
		shadow = new Shadow_Shape(new Color(182,182,182,50), isLeft);
		initShadow();
		updateShadow();
		millisecSinceGoDown = System.currentTimeMillis();
	}
	
	public void restart () { 
		millisecSinceGoDown = 0;
		goDownFaster = false;
//		score = 0; 
		// TODO écrire les meilleurs score dans un fichier avec le pseudo du joueur
		// Faire un menu un ou deux joueurs ?
//		lineCount = 0; 
//		level = 0;
		start = false;
		over = false;
		inPause = false;
		placedTiles = new LinkedList<Tile>();
		matrice = new int[20+2+1][10+2];
		putWalls();
//		showMatrice();
//		
//		startButton = new StartButton( new Vector2<Double>(0.1,0.2) , new Vector2<Double>(0.03,0.015)  );
		
		shape = randomShape(rand.nextInt(7));

		
		processHit(2);
		nextShape = rand.nextInt(7);
		
		shadow = new Shadow_Shape(new Color(182,182,182,50), isLeft);
		initShadow();
		updateShadow();
		
//		StdDraw.setPenColor(StdDraw.WHITE);
//		StdDraw.filledRectangle(0.1, 0.2, 0.1, 0.2);
	}
	
	public void restartStats () {
		score = 0;
		lineCount = 0; 
		level = 0;
	}
	
	
	
	public void initShadow() {
		for (Tile t : shape.getLayout()) {
			shadow.addTile( new Tile( new Vector2<Integer>(t.getPosition().getX(),t.getPosition().getY()),new Color(182,182,182,50)));
		}
	}
	
//	private static int DoublePosToInt(Double pos) {
//		return (int) (Math.round((pos/0.02)*100)/100);
//	}
	
	public void updateShadow() {
//		for (Tile t : player.getLayout()) {
//			shadow.modifyTile(((ArrayList<Tile>) player.getLayout()).indexOf(t),t);
//		}
		setyShadow();
		
		for ( Tile t : shadow.getLayout()) {
//			System.out.println(((ArrayList<Tile>) player.getLayout()).get(((ArrayList<Tile>) shadow.getLayout()).indexOf(t)).getPosition().getX());
//			System.out.println(t.getPosition().getY());
			t.updatePosition( 
					new Vector2<Integer>(
							((ArrayList<Tile>) shape.getLayout()).get(((ArrayList<Tile>) shadow.getLayout()).indexOf(t)).getPosition().getX(),
							((ArrayList<Tile>) shape.getLayout()).get(((ArrayList<Tile>) shadow.getLayout()).indexOf(t)).getPosition().getY() + shape.yShadow
//							t.getPosition().getY()-player.yShadow  // erreur il faut mettre 
							)
					); 
			// le x doit Ãªtre celui du layout de la vraie shape
//			System.out.println(t.getPosition());
		}
		
		
	}
	
	
	
	public void putWalls() {
		for (int i = 0; i<matrice.length;i++) {
			for (int j=0; j<matrice[i].length;j++) {
				matrice[0][j] = 1;
				matrice[matrice.length-1][j] = 1;
			}
			matrice[i][0] = 1;
			matrice[i][matrice[i].length-1] = 1;
		}
	}
	
	public void goMaxDown() {
		while (!processHit(0)) { 
			shape.goDown();
		}
	}
	
	public void setyShadow() {
		shape.yShadow = 0;
//		System.out.println("TEST");
		shape.incyShadow();
		while(!shadowHasTouched()) {
			shape.incyShadow();
//			updateShadow();
//			if (player.yShadow > 1)
//				System.exit(0);
//			System.out.println("hello");
		}
		shape.decyShadow();
//		updateShadow();
//		shadow.backToLastPos();
	}
	
	public void showMatrice() {
		System.out.println();
		for (int i = 0; i<matrice.length;i++) {
			for (int j=0; j<matrice[i].length;j++) {
				System.out.print(matrice[i][j]);
			}
			System.out.println();
		}
	}
	
	public void showPlacedTiles() {
		for (Tile t : placedTiles) {
			System.out.println(t);
		}
		System.out.println();
	}
	
	public boolean processHit(int action) {
		// Si pour toutes les tiles il y en a une qui touche un 1 on remet la dernière pos
		// Puis on met des "1" dans la matrice pour toutes les cases
		// Faire une fonction qui étant donnée une forme, place des "1" dans la matrice pour toutes ses cases ( attention au décalage !! )

	
		// action = 0 -> going down
		// action = 1 -> going left or right
		// action = 2 -> spawning a new shape
//		showMatrice();
		shape.yShadow = 0;
		if ( hasTouched()) {
			if (action == 0) {
				shape.backToLastPos();
				placeShapeInMatrice();
				placeShapeInPlacedTiles();
				
				AudioFile bloc = new AudioFile("sound_effects/bloc",false);
				bloc.play();	
				level++;
				
				updateTiles(destroyFullLines());
				
//				showMatrice();
				
//				refreshAllScreen = true;
				shape = randomShape(nextShape);

				processHit(2);
				nextShape = rand.nextInt(7);
			}
			else if (action == 1){
				shape.backToLastPos();
			} else if (action == 2 ) {
//				gameIsOver = true;
//				start = false;
//				restart();
				over = true;
				start = false;
//				musique.stop();
				
//				AudioFile gameOver = new AudioFile("gameOver",false);
//				gameOver.play();
			} else {
				System.out.println("Erreur: l'entier action doit �tre compris entre 0 et 2");
//				gameIsOver = true;
				start = false;
				restart();
			}
			return true;
		} return false;
		
//		if ( hasTouchedBottom() ) {
//			System.out.println("Coucou");
//			player.backToLastPos();
//			placeShapeInMatrice();
//			player.draw();
////			showMatrice();
//			player = new I_Shape(StdDraw.CYAN);
//			
//		}
//		if ( hasTouchedBorder()) {
//			System.out.println("Bonjour");
//			player.backToLastPos();
//		}
	}
	
	public boolean hasTouched() {
		for ( Tile t : shape.getLayout()) {
//			if (isLeft) {
//			System.out.println("Y:" + t.getPosition().getY());
//			System.out.println("X: " + t.getPosition().getX());
//			System.out.println();
//			}
			
			if (matrice[t.getPosition().getY()][t.getPosition().getX()] == 1) return true;
		}
//		if (isLeft) System.out.println("____");
		return false;
	}
	
	public boolean shadowHasTouched() {
//		System.out.println("called from shadowHasTouched");
		for ( Tile t : shape.getLayout()) {
//			System.out.println();
//			System.out.println("I: " + y_PosTo_i(t.getPosition().getY()));
//			System.out.println("J: " + x_PosTo_j(t.getPosition().getX()));
			if (matrice[t.getPosition().getY() + shape.yShadow][t.getPosition().getX()] == 1) return true;
		}
		return false;
	}
//	public boolean hasTouchedBottom() {
//		for ( Tile t : player.getLayout()) {
//			if (matrice[(int) (matrice.length-1 - ( t.getPosition().getY()/0.02) ) ][(int) (t.getPosition().getX()/0.02)] == 2) return true;
//		}
//		return false;
//	}
	
	public void placeShapeInMatrice() {
		for ( Tile t : shape.getLayout()) {
//			System.out.println(t.getPosition());
//			System.out.println("ligne: " +   t.getPosition().getY()) ;
//			System.out.println("colonne: " +  t.getPosition().getX());
			matrice[t.getPosition().getY()][t.getPosition().getX()] = 1;
		}
//		System.out.println();
//		showMatrice();
//		System.out.println();
	}
	
	public void placeShapeInPlacedTiles() {
		for (Tile t : shape.getLayout()) {
			placedTiles.add(t);
		}
	}
	
//	public int y_PosTo_i (Double y) {
//		return (int) Math.round((matrice.length-1 - ( y/0.02) ));
//	}
//	
//	public int x_PosTo_j (Double x) {
//		return (int) Math.round((x/0.02));
//	}
//	
//	public Double i_PosTo_y (int i) {
//		return (matrice.length-1 - i)*0.02 ;
//	}
//	
//	public Double j_PosTo_x (int j) {
//		return j*0.02;
//	}
	
//	public Collection<Double> getDifferentLines() {
//		Collection<Double> liste = new LinkedList<Double>();
//		for (Tile t : player.getLayout()) {
//				liste.add( t.getPosition().getY() );
//		}
//		return liste;
//	}
	
	public boolean lineIsFull(int i) {
		for (int j = 1; j < matrice[i].length-1; j++) {
			if((matrice[i][j] == 0))
				return false;
		}
		return true;
	}
	
	public void clearLine(int i) {
		for (int j = 1; j<matrice[i].length-1;j++) {
			matrice[i][j] = 0;
		}
	}
	
	public void swapLines(int line1 , int line2) {
		int[] save = matrice[line1];
		matrice[line1] = matrice[line2];
		matrice[line2] = save;
		}
	
	public Collection<Integer> destroyFullLines() {
		
		Collection<Integer> destroyedLines = new LinkedList<Integer>();
		for (int i = 1; i<matrice.length-1;i++) {
			if ( lineIsFull(i) ) {
				clearLine(i);
				destroyedLines.add(matrice.length-i);
				lineCount ++;
				for (int line2 = i-1 ; line2 > 0 && i > 1 ; line2--) {
					swapLines(i,line2);
					i--;
				}
			}
		}
		if (!destroyedLines.isEmpty()) {
			AudioFile line = new AudioFile("sound_effects/line",false);
			line.play();
		}
		return destroyedLines;
		
//		Collection<Double> destroyedLines = new LinkedList<Double>();
//		for (Double line : getDifferentLines()) {
//			int i = y_PosTo_i(line);
//			if ( lineIsFull(i) ) {
//				System.out.println(i);
//				clearLine(i);
//				destroyedLines.add(line);
//				lineCount ++;
//				for (int line2 =i-1 ; line2 > 0 && i > 1 ; line2--) {
//					swapLines(i,line2);
//					i--;
//				}
//			}
//		}
//		System.out.println(destroyedLines);
//		return destroyedLines;
	}
	
	public void drawTiles(Graphics g,TextureLoader tl) {
		for (Tile t : placedTiles) {
			t.draw(isLeft,g,tl);
		}
	}
	
	public void updateTiles(Collection<Integer> destroyedLines) {
		
		for (int line : destroyedLines) {
			placedTiles.removeIf( t -> ( t.getPosition().getY() == line ));
			for (Tile t : placedTiles) {
				if (t.getPosition().getY() > line) {
					t.updatePosition( new Vector2<Integer> (t.getPosition().getX(), t.getPosition().getY()+1) );
				}
			}
		}
		
	}
	
	public Shape_Class randomShape(int r) {
//		int r = rand.nextInt(7);
		switch(r) {
			case 0: {
				return new T_Shape(Color.pink, isLeft);
			}
			case 1: {
				return new I_Shape(Color.cyan, isLeft);
			}
			case 2: {
				return new J_Shape(Color.blue, isLeft);
			}
			case 3: {
				return new L_Shape(Color.orange, isLeft);
			}
			case 4: {
				return new O_Shape(Color.yellow, isLeft);
			}
			case 5: {
				return new Z_Shape(Color.red, isLeft);
			}
			case 6: {
				return new S_Shape(Color.green, isLeft);
			}
			default: {
				return null;
			}
		}
	}
	
	
	
	/**
	 * Makes a step in the world.
	 */
	public void step() {
		
		long millisecUntilGoDown = 600;
		if (goDownFaster) millisecUntilGoDown /= 10;
		
		if (!inPause) { // Faire une pause de plus de 600 ms = redescendre juste après la fin de la pause
			if ( millisecSinceGoDown+millisecUntilGoDown <= System.currentTimeMillis()) { 
				shape.goDown();
				processHit(0);
				updateShadow();
				millisecSinceGoDown = 0;
//				if (isLeft) showMatrice();
				millisecSinceGoDown = System.currentTimeMillis();
			}
			else updateShadow();
		}	
		score = level*4 + lineCount*10;
	}

	
	/**
	 * Draws the ships and HUDs.
	 */
	public void draw(Graphics g, TextureLoader tl) {
		if (start) {
			shadow.draw(g,tl);
			shape.draw(g,tl);
		}
		drawTiles(g,tl);
	}
	
	
	

}

package shape;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.Serializable;

//import display.StdDraw;
import display.Vector2;

/**
 * A tile is a cell of the ship's layout.
 * A weapon can be attached to the tile and a crew member 
 * can be on the tile.
 */
public class Tile implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2732929164670973602L;
	protected	Vector2<Integer> 	tilePos;	// The position of the tile
	protected	Color				color;
	protected	Vector2<Integer>	lastPos;
	int					size;
	private		int					borderSize;
//	private transient BufferedImage purple = loadImage("textures/purple");
//	private transient BufferedImage cyan = loadImage("textures/cyan");
//	private transient BufferedImage blue = loadImage("textures/blue");
//	private transient BufferedImage orange = loadImage("textures/orange");
//	private transient BufferedImage yellow = loadImage("textures/yellow");
//	private transient BufferedImage red = loadImage("textures/red");
//	private transient BufferedImage green = loadImage("textures/green");
	
	/**
	 * Creates a tile for the player of the opponent
	 * which is drawn at the given position.
	 * @param position location to draw the tile
	 * @param isPlayer whether it is owned by the player ship
	 */
	public Tile(Vector2<Integer> position, Color color) {
		if (position !=null) {
		this.tilePos = new Vector2<Integer>(position.getX(),position.getY());
		this.lastPos = new Vector2<Integer>(position.getX(),position.getY());
		}
		this.color = color;
		this.size = 54; // une tile est un carré de pixels de 54x54
		this.borderSize = 1; // en Pixels
		
	}
	
	
	
	/**
	 * Draws the tile, the member inside and the weapon.
	 * @return 
	 */
//	public void draw(boolean left) {
//		if (tilePos == null)
//			return;
//		
//		double x = size * (tilePos.getX()-1);
//		double y = size * (tilePos.getY()-1);
//		if (!left) {
//			x+=size*10 + 840; // TODO 840 correspond à l'écran central pour l'instant
//		}
//		
//		
//		
//		StdDraw.setPenColor(color);
//		StdDraw.filledRectangle(x-0.01, y-0.01, 0.01, 0.01);
//		StdDraw.setPenColor(StdDraw.BLACK);
//		drawHorizontalWall(x,y);
//		y-=0.02;
//		drawVerticalWall(x,y);
//		drawHorizontalWall(x,y);
//		x-=0.02;
//		drawVerticalWall(x,y);
//		y+=0.02;
//		x+=0.02;
//
//		StdDraw.setPenColor(StdDraw.BLACK);
//	}
	
//	BufferedImage loadImage(String fn) {	
//
//		InputStream inputStr = this.getClass().getResourceAsStream("/" + fn + ".png");
//			
//		BufferedImage image = null;
//		
//		try {
//			image = ImageIO.read(inputStr);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return image;
//	}
	
	@Override
	public String toString() {
		return "X: " + tilePos.getX() + "Y: " + tilePos.getY();
		
	}
	
//	public void draw(boolean left, Graphics g) {
//		if (tilePos == null)
//			return;
//		
//		int x = size * (tilePos.getX()-1);
//		int y = size * (tilePos.getY()-2);
//		if (!left) {
//			x+=size*10 + 840; // 840 correspond à l'écran central pour l'instant
//		}
//		
//		g.setColor(Color.black);
//		g.fillRect(x-borderSize, y-borderSize, size+borderSize*2, size+borderSize*2);
//		
//		g.setColor(color);
//		g.fillRect(x+borderSize, y+borderSize, size-borderSize*2, size-borderSize*2);
//	}
	
	public void draw(boolean left, Graphics g, TextureLoader tl) {
		if (tilePos == null)
			return;
		
		int x = size * (tilePos.getX()-1);
		int y = size * (tilePos.getY()-2);
		if (!left) {
			x+=size*10 + 840; // 840 correspond à l'écran central pour l'instant
		}
		BufferedImage texture;
		switch (color.hashCode()) {
			case -20561:{ // hashcode for pink
				texture = tl.purple;
				break;
			}
			case -16711681:{
				texture = tl.cyan;
				break;
			}
			case -16776961:{
				texture = tl.blue;
				break;
			}
			case -14336:{
				texture = tl.orange;
				break;
			}
			case -256:{
				texture = tl.yellow;
				break;
			}
			case -65536:{
				texture = tl.red;
				break;
			}
			case -16711936:{
				texture = tl.green;
				break;
			}
			default: {
				g.setColor(Color.black);
				g.fillRect(x-borderSize, y-borderSize, size+borderSize*2, size+borderSize*2);
				
				g.setColor(color);
				g.fillRect(x+borderSize, y+borderSize, size-borderSize*2, size-borderSize*2);
				return;
			}
		}
		g.drawImage(texture,x,y,size,size,null);
		
	}
	
	
	public void drawForSideScreen(Graphics g, int x, int y) {
		if (tilePos == null)
			return;
		
		x += size * (tilePos.getX()-1);
		y += size * (tilePos.getY()-1);
		
		g.setColor(Color.black);
		g.fillRect(x-borderSize, y-borderSize, size+borderSize*2, size+borderSize*2);
		
		g.setColor(color);
		g.fillRect(x+borderSize, y+borderSize, size-borderSize*2, size-borderSize*2);
	}
	
	/**
	 * Gives the position of the tile.
	 * @return the position
	 */
	public Vector2<Integer> getPosition() {
		return tilePos;
	}
	
//	/**
//	 * Gives the center position of the tile.
//	 * @return the position
//	 */
//	public Vector2<Integer> getCenterPosition() {
//		return new Vector2<Integer>(tilePos.getX()-0.01, tilePos.getY()-0.01);
//	}
	
//	public boolean isOutOfScreen() {
//		return tilePos.getX() > 1 || tilePos.getY() > 1 || tilePos.getX() < 0 || tilePos.getY() < 0;
//	}
	
	
	public void updatePosition(Vector2<Integer> pos) {
		this.lastPos = this.tilePos;
		this.tilePos = new Vector2<Integer>( pos.getX(), pos.getY() );
	}
	
	public void backToLastPos() {
		this.updatePosition(lastPos);
	}
	

}

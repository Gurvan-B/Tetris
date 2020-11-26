package shape;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import display.AudioFile;
import display.Vector2;



public abstract class Shape_Class implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2732929164670973602L;
	protected 	Collection<Tile>	layout;
	protected 	Color				color;
	protected 	Tile				centerPiece;
	public		int				yShadow;
	public 		boolean 			isLeft;
//	public static		AudioFile 	musique = new AudioFile("musique",true);
	
	
	public Shape_Class(Color color, boolean isLeft) {
		this.isLeft = isLeft;
		layout = new ArrayList<Tile>();
		this.color = color;
		yShadow = 0;
//		this.centerPiece = centerPiece;
		
	}
	
	@Override
	public String toString() {
		String res = "";
		for ( Tile t : layout) {
			res += "X:" + t.getPosition().getX() + ", Y: " + t.getPosition().getY() + " | ";
		}
		
		return res;
	}
	
	public void showLayout() {
		for ( Tile t : layout) {
			System.out.println(t);
		}
		System.out.println();
	}
	
	public void incyShadow() { // TODO gérer la variable size
		yShadow += 1;
	}
	
	public void decyShadow() {
		yShadow += -1;
	}
	
//	public int getShadowForMatrice() {
//		return (int) (Math.round( (yShadow/0.02)*100 )/100);
//	}
	
	public void addTile(Tile t) {
		layout.add(t);
	}

//	public void modifyTile(int index, Tile t) {
//		 ((ArrayList<Tile>) layout).get(index).updatePosition( new Vector2<Double>(t.getPosition().getX(),t.getPosition().getY()));
//	}
	
	public void draw(Graphics g, TextureLoader tl) {
		for ( Tile t : layout) {
			t.draw(isLeft,g,tl);
		}
	}
	
//	public void erase() {
//		for ( Tile t : layout) {
//			t.erase();
//		}
//	}
	
	public void drawForSideScreen(Graphics g,int centerX, int centerY, TextureLoader tl) {
		for ( Tile t : layout) {
			int size = centerPiece.size;
			int x = centerPiece.getPosition().getX()*size;
			int y = centerPiece.getPosition().getY()*size;
			
//			Vector2<Integer> v = getCenterPos();
			
//			int x = v.getX();
//			int y = v.getY();
			x = centerX-x+size/2;
			y = centerY-y+size/2;
			t.drawForSideScreen(g,x,y,tl);
		}
	}

//	public Vector2<Integer> getCenterPos() {
//		int x = 0;
//		int y = 0;
//		Collection<Integer> alreadyCountedX = new LinkedList<Integer>();
//		Collection<Integer> alreadyCountedY = new LinkedList<Integer>();
//		
//		for ( Tile t : layout) {
//			int xPos = t.getPosition().getX();
//			int yPos = t.getPosition().getY();
//			if (!alreadyCountedX.contains(xPos)) {
//				x += xPos;
////				System.out.println("x " + x);
//				alreadyCountedX.add(xPos);
//			}
//			
//			if (!alreadyCountedY.contains(yPos)) {
//				y += yPos;
////				System.out.println("y " + y);
//				alreadyCountedY.add(yPos);
//			}
//		}
//		
//		System.out.println(alreadyCountedX.size());
//		if (alreadyCountedX.size() != 0 && alreadyCountedY.size() != 0) {
//		x = x/alreadyCountedX.size();
//		y = y/alreadyCountedY.size();
//		}
//		return new Vector2<Integer>(x,y);
//		
//	}

	
	public void rotateRight(boolean playSound) {
		if ( !(this instanceof O_Shape) ) {
			int xCentre = centerPiece.getPosition().getX();
			int yCentre = centerPiece.getPosition().getY();
			for (Tile t : layout) {
				t.updatePosition( new Vector2<Integer>(xCentre + ( t.getPosition().getY() - yCentre ) , yCentre + (xCentre - t.getPosition().getX()) ));
			}
			if (playSound) {
				AudioFile rotate = new AudioFile("sound_effects/rotate",false);
				rotate.play();
			}
		}
	}
	
	public void rotateLeft(boolean playSound) {
		if ( !(this instanceof O_Shape)) {
			int xCentre = centerPiece.getPosition().getX();
			int yCentre = centerPiece.getPosition().getY();
			for (Tile t : layout) {
				t.updatePosition( new Vector2<Integer>(xCentre + ( yCentre - t.getPosition().getY() ) , yCentre + ( t.getPosition().getX() - xCentre ) ));
			}
			if (playSound) {
				AudioFile rotate = new AudioFile("sound_effects/rotate",false);
				rotate.play();
			}
		}
		
	}
	

	
	public Collection<Tile> getLayout() { return layout; }
	
	
	public void goDown() {
		
		for (int i = layout.size()-1; i>=0;i--) {
			Tile t = ((ArrayList<Tile>) layout).get(i);
			t.updatePosition( new Vector2<Integer>(t.getPosition().getX(),t.getPosition().getY()+1)  );
		}
		
	}
	
	public void goUp() {
		
		for (int i = layout.size()-1; i>=0;i--) {
			Tile t = ((ArrayList<Tile>) layout).get(i);
			t.updatePosition( new Vector2<Integer>(t.getPosition().getX(),t.getPosition().getY()-1)  );
		}
//		processHit();
	}
	
	public void goRight() {
		
		for (int i = layout.size()-1; i>=0;i--) {
			Tile t = ((ArrayList<Tile>) layout).get(i);
			t.updatePosition( new Vector2<Integer>(t.getPosition().getX()+1,t.getPosition().getY())  );
		}
		AudioFile move = new AudioFile("sound_effects/move",false);
		move.play();
	}
	
	public void goLeft() {
		
		for (int i = layout.size()-1; i>=0;i--) {
			Tile t = ((ArrayList<Tile>) layout).get(i);
			t.updatePosition( new Vector2<Integer>(t.getPosition().getX()-1,t.getPosition().getY())  );
		}
		AudioFile move = new AudioFile("sound_effects/move",false);
		move.play();
	}
	
	
	
//	public Tile getNext(Tile t) {
//		int i = ((ArrayList<Tile>) layout).indexOf(t);
//		if (i==0)
//			return t;
//		else
//			return ((ArrayList<Tile>) layout).get(i-1);
//	}
	
	public void backToLastPos() {
		for ( Tile t : layout ) {
			t.backToLastPos();
		}
	}

//	public void step() {
////		System.out.println("last:    " + ((ArrayList<Tile>) layout).get(0).lastPos);
////		System.out.println("current: " + ((ArrayList<Tile>) layout).get(0).tilePos);
//		goDown();
//	}
		
		

}

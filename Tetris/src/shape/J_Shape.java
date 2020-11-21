package shape;

import java.awt.Color;
import display.Vector2;

public class J_Shape extends Shape_Class {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = -2732929164670973602L;

	public J_Shape(Color color, boolean isLeft) {
		super(color,isLeft);
		centerPiece = new Tile( new Vector2<Integer>(5, 3), color );
		addTile( new Tile( new Vector2<Integer>(4, 4), color ) ); 
		addTile( new Tile( new Vector2<Integer>(5, 4), color ) );
		addTile( centerPiece );
		addTile( new Tile( new Vector2<Integer>(5, 2), color ) );
		
	}
	
//	@Override
//	public TetrisShape getShadow() { // faire un fonction pour créer la shadow ( sans être actualisée ) puis une dans tetrisShape pour la mettre à jour
//		TetrisShape shadow = new J_Shape(StdDraw.GRAY);
//		System.out.println("Shadow: " + yShadow);
//		for ( Tile t : shadow.layout) {
//			t.updatePosition( new Vector2<Double>( ((ArrayList<Tile>) layout).get(((ArrayList<Tile>) shadow.layout).indexOf(t)).getPosition().getX(), t.getPosition().getY()-yShadow )); // TODO le x doit Ãªtre celui du layout de la vraie shape
//			System.out.println(t.getPosition());
//		}
//		System.out.println();
//		return shadow;
//	}

}

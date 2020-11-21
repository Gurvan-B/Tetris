package shape;

import java.awt.Color;

import display.Vector2;

public class T_Shape extends Shape_Class {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2732929164670973602L;

	public T_Shape(Color color, boolean isLeft) {
		super(color, isLeft);
		centerPiece = new Tile( new Vector2<Integer>(5, 3),color) ;
		addTile( new Tile( new Vector2<Integer>(6, 3), color ) ); 
		addTile( new Tile( new Vector2<Integer>(4, 3), color ) );
		addTile( centerPiece );
		addTile( new Tile( new Vector2<Integer>(5, 2), color ) );
		
	}

}

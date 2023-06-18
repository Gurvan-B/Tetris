package display;

import java.io.Serializable;

public class Vector2<K> implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2732929164670973602L;
	private K X;
	private K Y;
	
	public Vector2(K x, K y) {
		X = x;
		Y = y;
	}
	
	public K getX() {
		return X;
	}
	
	public K getY() {
		return Y;
	}

	public boolean equals(Vector2<K> that) {
		return X.equals(that.X) && Y.equals(that.Y);
	}
	
	public String toString() {
		return "X: "+ X.toString() + ",Y: " + Y.toString();
	}
}

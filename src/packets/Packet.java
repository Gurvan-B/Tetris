package packets;

import java.io.Serializable;

public abstract class Packet implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2732929164670973602L;
	private int type;
	
	public Packet() {
	}
	
	public int getType() {
		return this.type;
	}
	
	public Object getValue() {
		return null;
	}
}

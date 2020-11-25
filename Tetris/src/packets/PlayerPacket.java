package packets;

import main.Player;

public class PlayerPacket extends Packet {

	public static final int player = 4;
//	public static final int updateRight = 5;
	
	private static final long serialVersionUID = 2732929164670973602L;
	private Player p;
	private int type;
	
	public PlayerPacket(Player p) {
		super();
		this.p = p;
		this.type= player;
	}
	
	public Player getPlayer() {
		return p;
	}
	
	public int getType() {
//		return type;
		return type;
	}

	public String toString() {
		return "Packet type: Player " + " | Value: " + p;
	}
}

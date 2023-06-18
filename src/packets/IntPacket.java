package packets;

public class IntPacket extends Packet {

	private static final long serialVersionUID = 2732929164670973602L;
	public static final int setDifficulty = 5;
	
	private int i;
	private int type;
	
	public IntPacket(int i, int type) {
		super();
		this.i = i;
		if (type == setDifficulty) this.type = type;
		else{
			this.type=-1;
			System.out.println("Erreur de type de packet");
		}
	}
	
	public int getInt() {
		return i;
	}
	
	public int getType() {
		return type;
	}

	public String toString() {
		return "Packet type: " + type + " | Value: " + i;
	}
}
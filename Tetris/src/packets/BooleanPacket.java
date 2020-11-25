package packets;


public class BooleanPacket extends Packet {

	private static final long serialVersionUID = 2732929164670973602L;
	public static final int isLeftBool = 0;
	public static final int isReadyBool = 1;
	public static final int beginBool = 2;
//	public static final int isDeadBool = 3; // rajouter le cas 3 dans le constructeur
	
	private boolean b;
	private int type;
	
	public BooleanPacket(boolean b, int type) {
		super();
		this.b = b;
		if (type>=0 && type<=2) this.type= type;
		else{
			this.type=-1;
			System.out.println("Erreur de type de packet");
		}
	}
	
	public boolean getBoolean() {
		return b;
	}
	
	public int getType() {
		return type;
	}

	public String toString() {
		return "Packet type: " + type + " | Value: " + b;
	}
}

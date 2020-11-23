package client;

public class Launch_Client {

	public static void main(String[] args) {
		Client client = new Client(null);
		new Thread(client).start();
	}

}

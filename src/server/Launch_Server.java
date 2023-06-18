package server;

public class Launch_Server {

	public static void main(String[] args) {
		
//		try {
//			InetAddress	add = InetAddress.getLocalHost();
//			System.out.println(add);
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//		}
		
		Server server = new Server();
		new Thread(server).start();
	}

}

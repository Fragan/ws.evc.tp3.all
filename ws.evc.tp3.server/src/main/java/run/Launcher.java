package run;

import java.rmi.RemoteException;

import broadcast.BroadcastUpdates;

import servicesImpl.Server;

public class Launcher {

	@SuppressWarnings("unused")
	private Server server;
	private static String OS = System.getProperty("os.name").toLowerCase();

	public Launcher() {
		try {
			server = new Server("Pluton", "localhost", 1234);
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
		
		try {
			
			if (OS.indexOf("win") >= 0)
				BroadcastUpdates.init("192.168.0.255", 1234);
			else 
				BroadcastUpdates.init("239.0.0.1", 1234);
		} catch (RemoteException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public static void main(String[] args) {
		@SuppressWarnings("unused")
		Launcher l = new Launcher();
	}

}

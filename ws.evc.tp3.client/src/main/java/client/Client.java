package client;

import j3d.interfaces.universe.ISharedUniverseServer;

import java.rmi.Naming;

public class Client {

	public static ISharedUniverseServer getSharedUniverse(String serverHostName, String serverRMIPort, String sharedWorldName) {
		ISharedUniverseServer sharedWorld = null;
		try {
			sharedWorld = (ISharedUniverseServer) Naming.lookup("//"
					+ serverHostName + ":" + serverRMIPort + "/"
					+ sharedWorldName);
		} catch (Exception e) {
			System.out.println("Probleme liaison RMI");
			e.printStackTrace();
			System.exit(1);
		}
		return sharedWorld;
	}
}

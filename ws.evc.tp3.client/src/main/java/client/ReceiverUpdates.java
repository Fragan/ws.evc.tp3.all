package client;

import ihm.app.App;
import j3d.interfaces.universe.ICamera;
import j3d.interfaces.universe.IObject;
import j3d.interfaces.universe.ISharedUniverse;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class ReceiverUpdates extends Thread implements Runnable {

	private transient MulticastSocket socketReception;
	private ISharedUniverse deportedClient;

	public void setDeportedClient(ISharedUniverse deportedClient) {
		this.deportedClient = deportedClient;
	}

	public ReceiverUpdates(final String nomGroupe, final int portDiffusion) {
		socketReception = null;
		try {
			InetAddress adresseDiffusion = InetAddress.getByName(nomGroupe);
			socketReception = new MulticastSocket(portDiffusion);
			socketReception.joinGroup(adresseDiffusion);
			socketReception.setLoopbackMode(App.LOCAL_SERVER);
			System.out.println("socket : " + socketReception.getLocalPort()
					+ " " + socketReception.getInetAddress());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Object receive() {
		
		try {
			byte[] message = new byte[1024];
			DatagramPacket paquet = new DatagramPacket(message, message.length);
			socketReception.receive(paquet);

			ByteArrayInputStream bais = new ByteArrayInputStream(
					paquet.getData());

			ObjectInputStream ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void run() {
		while (true) {
			Object obj = receive();
			if (obj instanceof ICamera)
				deportedClient.update((ICamera) obj);
			else if (obj instanceof IObject)
				deportedClient.update((IObject) obj);
		}
	}
}

package broadcast;

import j3d.interfaces.universe.ICamera;
import j3d.interfaces.universe.IObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.rmi.RemoteException;

public class BroadcastUpdates implements Serializable {

	/**
	 * 
	 *
	 */
	public static BroadcastUpdates instance;
	private static final long serialVersionUID = -2757235753788352228L;
	private int portDiffusion;
	private String nomGroupe;

	public String getNomGroupe() {
		return nomGroupe;
	}

	public static void init(final String ng, final int portDiffusion) throws RemoteException {
		instance = new BroadcastUpdates(ng, portDiffusion);
	}

	public static BroadcastUpdates getInstance() {
		return instance;
	}

	private InetAddress adresseDiffusion;
	private transient MulticastSocket socketDiffusion;

	public BroadcastUpdates(final String ng, final int portDiffusion)
			throws RemoteException {
		this.portDiffusion = portDiffusion;
		nomGroupe = ng;
		System.out.println("Diffusion sur le port " + portDiffusion
				+ " a destination du groupe " + nomGroupe);
		adresseDiffusion = null;
		socketDiffusion = null;
		try {
			adresseDiffusion = InetAddress.getByName(nomGroupe);
			socketDiffusion = new MulticastSocket();
			socketDiffusion.setTimeToLive(64);
			socketDiffusion.setLoopbackMode(false); // pour des envois d'une
													// machine à une autre
			// si on veut faire des tests avec serveur et clients sur une même
			// machine, il faut écrire : socketDiffusion.setLoopbackMode(false)
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("socket : " + socketDiffusion.getLocalPort() + " "
				+ socketDiffusion.getInetAddress());
	}

	public void diffuse(IObject object) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		DatagramPacket paquet = new DatagramPacket(baos.toByteArray(),
				baos.toByteArray().length, adresseDiffusion, portDiffusion);
		try {
			socketDiffusion.send(paquet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void diffuse(ICamera camera) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(baos);
			oos.writeObject(camera);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		DatagramPacket paquet = new DatagramPacket(baos.toByteArray(),
				baos.toByteArray().length, adresseDiffusion, portDiffusion);
		try {
			socketDiffusion.send(paquet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getPortDiffusion() throws RemoteException {
		return (portDiffusion);
	}

	public InetAddress getAdresseDiffusion() throws RemoteException {
		return (adresseDiffusion);
	}

}
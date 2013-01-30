package servicesImpl;

import j3d.abstraction.universe.ASharedUniverse;
import j3d.interfaces.universe.ICamera;
import j3d.interfaces.universe.IObject;
import j3d.interfaces.universe.ISharedUniverse;
import j3d.interfaces.universe.ISharedUniverseServer;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import broadcast.BroadcastUpdates;

public class Server extends UnicastRemoteObject implements ISharedUniverseServer {

	private static final long serialVersionUID = -2752486228990221680L;

	private ISharedUniverse sharedUniverse;

	public Server(String sharedWorldName, String serverHostName,
			int serverRMIPort)
			throws RemoteException {
		super();
		sharedUniverse = new ASharedUniverse(sharedWorldName);
		try {
			// dans un shell, il faudrait avoir fait : remiregistry
			// `serverRMIPort`,
			// mais on peut avantageusement remplacer cette commande par un
			// "createRegistry"
			LocateRegistry.createRegistry(serverRMIPort);
			Naming.rebind("//" + serverHostName + ":" + serverRMIPort + "/"
					+ sharedWorldName, this);
			System.out.println("Ready...");
			
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Problem youston");
		}
	}

	public ArrayList<IObject> getObjects() throws RemoteException {
		ArrayList<IObject> l = new ArrayList<IObject>();
		l.addAll(sharedUniverse.getObjects());
		return l;
	}

	public ArrayList<ICamera> getCameras() throws RemoteException {
		ArrayList<ICamera> l = new ArrayList<ICamera>();
		l.addAll(sharedUniverse.getCameras());
		return l;
	}

	public boolean add(IObject object) throws RemoteException {
		boolean result = sharedUniverse.add(object);
		if (result)
			BroadcastUpdates.getInstance().diffuse(object);
		return result;
	}

	public void remove(IObject object) throws RemoteException {
		sharedUniverse.remove(object);
	}

	public boolean add(ICamera camera) throws RemoteException {
		boolean result =  sharedUniverse.add(camera);
		if (result)
			BroadcastUpdates.getInstance().diffuse(camera);
		return result;
	}

	public void remove(ICamera camera) throws RemoteException {
		sharedUniverse.remove(camera);
	}

	public IObject getObject(String name) throws RemoteException {
		return sharedUniverse.getObject(name);
	}

	public ICamera getCamera(String name) throws RemoteException {
		return sharedUniverse.getCamera(name);
	}

	public String getName() throws RemoteException {
		return sharedUniverse.getName();
	}

	public void update(ICamera camera) throws RemoteException {
		ICamera ourCamera = sharedUniverse.getCamera(camera.getOwnerName());
		if (ourCamera != null) {
			ourCamera.setOrientation(camera.getOrientation(), false);
			ourCamera.setPosition(camera.getPosition(), false);
			BroadcastUpdates.getInstance().diffuse(camera);
			
		}
	}

	public void update(IObject object) throws RemoteException {
		IObject ourObject = sharedUniverse.getObject(object.getName());
		if (ourObject != null) {
			ourObject.setOrientation(object.getOrientation(), false);
			ourObject.setPosition(object.getPosition(), false);
			BroadcastUpdates.getInstance().diffuse(object);
		}
	}
	
	

}

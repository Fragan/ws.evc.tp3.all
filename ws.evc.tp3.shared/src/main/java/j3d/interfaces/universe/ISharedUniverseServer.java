package j3d.interfaces.universe;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ISharedUniverseServer extends  Remote {
	
	public ArrayList<IObject> getObjects() throws RemoteException;
	
	public ArrayList<ICamera> getCameras() throws RemoteException;	
	
	public boolean add(IObject object) throws RemoteException;
	
	public void remove(IObject object) throws RemoteException;
	
	public boolean add(ICamera camera) throws RemoteException;
	
	public void remove(ICamera camera) throws RemoteException;
	
	public IObject getObject(String name) throws RemoteException;

	public ICamera getCamera(String name) throws RemoteException;
	
	public String getName() throws RemoteException;
	
	public void update(ICamera camera) throws RemoteException;
	
	public void update(IObject object) throws RemoteException;

}

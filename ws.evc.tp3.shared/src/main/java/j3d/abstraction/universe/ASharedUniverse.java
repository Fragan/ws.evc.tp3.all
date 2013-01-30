package j3d.abstraction.universe;

import j3d.interfaces.universe.ICamera;
import j3d.interfaces.universe.IObject;
import j3d.interfaces.universe.ISharedUniverse;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import broadcast.BroadcastUpdates;

public class ASharedUniverse  implements ISharedUniverse, Serializable {


	private static final long serialVersionUID = 1714649179279421333L;
	private Map<String, IObject> objects;
	private Map<String, ICamera> cameras;
	private String name;

	public ASharedUniverse(String name) {
		objects = new HashMap<String, IObject>();
		cameras = new HashMap<String, ICamera>();
		this.name = name;
	}

	public Collection<IObject> getObjects() {
		return objects.values();
	}

	public Collection<ICamera> getCameras() {
		return cameras.values();
	}

	public boolean add(IObject object) {
		if (getObject(object.getName()) != null) {
			System.err.println("The object " + object.getName()
					+ " already included in the universe");
			return false;
		}
		objects.put(object.getName(), object);
		return true;
	}

	public void remove(IObject object) {
		objects.remove(object.getName());
	}

	public boolean add(ICamera camera) {
		if (getObject(camera.getOwnerName()) != null) {
			System.err.println("The camera " + camera.getOwnerName()
					+ " already included in the universe");
			return false;
		}
		cameras.put(camera.getOwnerName(), camera);
		return true;
	}

	public void remove(ICamera camera) {
		cameras.remove(camera.getOwnerName());
	}

	public IObject getObject(String name) {
		return objects.get(name);
	}

	public ICamera getCamera(String name) {
		return cameras.get(name);
	}
	
	public String getName() {
		return name;
	}

	public void update(ICamera camera) {
	
	}

	public void update(IObject object) {
		

	}

}

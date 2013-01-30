package j3d.interfaces.universe;

import java.util.Collection;

public interface ISharedUniverse {
	
	public Collection<IObject> getObjects();
	
	public Collection<ICamera> getCameras();	
	
	public boolean add(IObject object);
	
	public void remove(IObject object);
	
	public boolean add(ICamera camera);
	
	public void remove(ICamera camera);
	
	public IObject getObject(String name);

	public ICamera getCamera(String name);
	
	public String getName();
	
	public void update(ICamera camera);
	
	public void update(IObject object);

}

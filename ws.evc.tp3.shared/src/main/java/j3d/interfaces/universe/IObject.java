package j3d.interfaces.universe;

import j3d.abstraction.universe.AObject;

import javax.media.j3d.Transform3D;
import javax.vecmath.Quat4d;
import javax.vecmath.Vector3d;



public interface IObject {
	public Vector3d getPosition();

	public void setPosition(Vector3d position, boolean diffuse);

	public Quat4d getOrientation();

	public void setOrientation(Quat4d orientation, boolean diffuse);

	public String getName();

	public String getURLGeometry();
	
	public void setTransform(Transform3D t3d, boolean diffuse);

	public AObject getAbstraction();
	
	public String getUsedBy();

	public void setUsedBy(String usedBy, boolean diffuse);
}

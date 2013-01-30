package j3d.abstraction.universe;

import j3d.interfaces.universe.IObject;

import java.io.Serializable;

import javax.media.j3d.Transform3D;
import javax.vecmath.Quat4d;
import javax.vecmath.Vector3d;

// Les donnees
public class AObject implements IObject, Serializable {

	private static final long serialVersionUID = 2L;
	private String name;
	private Vector3d position;
	private Quat4d orientation;
	private String urlGeometry;
	private String usedBy;

	public AObject(String name, String urlGeometry) {
		this.name = name;
		this.urlGeometry = urlGeometry;
		this.orientation = new Quat4d();
		this.position = new Vector3d();
		usedBy = "";
	}

	public Vector3d getPosition() {
		return position;
	}

	public void setPosition(Vector3d position, boolean diffuse) {
		this.position = position;
	}

	public Quat4d getOrientation() {
		return orientation;
	}

	public void setOrientation(Quat4d orientation, boolean diffuse) {
		this.orientation = orientation;
	}

	public String getName() {
		return name;
	}

	public String getURLGeometry() {
		return urlGeometry;
	}

	public void setTransform(Transform3D t3d, boolean diffuse) {
		t3d.get(orientation, position);
	}

	public AObject getAbstraction() {
		return this;
	}
	
	public String getUsedBy() {
		return usedBy;
	}

	public void setUsedBy(String usedBy) {
		this.usedBy = usedBy;
	}
}

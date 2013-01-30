package j3d.abstraction.universe;

import j3d.interfaces.universe.ICamera;

import java.io.Serializable;

import javax.media.j3d.Transform3D;
import javax.vecmath.Quat4d;
import javax.vecmath.Vector3d;

public class ACamera implements ICamera, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	private String ownerName;
	private Vector3d position;
	private Quat4d orientation;

	public ACamera(String ownerName) {
		this.ownerName = ownerName;
		this.orientation = new Quat4d();
		this.position = new Vector3d();
	}

	public String getOwnerName() {
		return ownerName;
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

	public void setTransform(Transform3D t3d, boolean diffuse) {
		t3d.get(orientation, position);
	}

	public ACamera getAbstraction() {
		return this;
	}
}

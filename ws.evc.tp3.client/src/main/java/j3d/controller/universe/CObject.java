package j3d.controller.universe;

import j3d.abstraction.universe.AObject;
import j3d.interfaces.universe.IObject;
import j3d.interfaces.universe.ISharedUniverseServer;
import j3d.presentation.universe.PObject;

import java.rmi.RemoteException;

import javax.media.j3d.Transform3D;
import javax.vecmath.Quat4d;
import javax.vecmath.Vector3d;

import tools.Downloader;

// controle les interactions
public class CObject implements IObject {

	private PObject presentation;
	private AObject abstraction;
	private ISharedUniverseServer serverProxy;

	public CObject(AObject abstraction, ISharedUniverseServer proxy) {
		this.abstraction = abstraction;
		serverProxy = proxy;
		String userUrl = abstraction.getURLGeometry();
		if (userUrl.startsWith("http")) {
			userUrl = Downloader.donwloadFile(userUrl, false);
		}

		presentation = new PObject(this, userUrl);
	}

	/*
	 * public void rotateTo(double h, double d, double r) { Vector3d rotate =
	 * new Vector3d(); rotate.set(h, d, r);
	 * 
	 * Transform3D t3D = new Transform3D(); presentation.getTransform(t3D);
	 * 
	 * Vector3d translate = new Vector3d();
	 * 
	 * t3D.get(translate); t3D.setEuler(rotate); t3D.setTranslation(translate);
	 * 
	 * presentation.setTransform(t3D); super.setTransform(t3D); }
	 */
	
	public boolean isUtilizable() {
		if (getUsedBy().isEmpty()) {
			setUsedBy(CSharedUniverse.cCameraUser.getOwnerName(), true);
			return true;
		}
		else if(  CSharedUniverse.cCameraUser.getOwnerName().equals(getUsedBy()))
			return true;
		else
			return false;
	}

	public void rotate(double dh, double dp, double dr) {
		if (!isUtilizable())
			return;
		Transform3D oldT3D = new Transform3D();
		presentation.getTransform(oldT3D);
		Transform3D tc = new Transform3D();

		double x = 0, y = 0, z = 0;
		x = (Math.PI * dh / 180);
		y = Math.PI * dp / 180;
		z = Math.PI * dr / 180;

		tc.setEuler(new Vector3d(-y, x, z));
		oldT3D.mul(tc);

		oldT3D.normalize();
		presentation.setTransform(oldT3D);
		setTransform(oldT3D, true);
	}

	public void translate(double dx, double dy, double dz) {
		if (!isUtilizable())
			return;
		Transform3D vpT3D = new Transform3D();
		presentation.getTransform(vpT3D);
		Transform3D vpT3Dinv = new Transform3D();
		vpT3Dinv.invert(vpT3D);

		Transform3D oldT3D = new Transform3D();
		presentation.getTransform(oldT3D);
		Vector3d translate = new Vector3d();
		translate.set(dx, dy, dz);
		Transform3D localDeltaT3D = new Transform3D();
		localDeltaT3D.setTranslation(translate);
		Transform3D absoluteDeltaT3D = new Transform3D();
		absoluteDeltaT3D.mul(vpT3D, localDeltaT3D);
		absoluteDeltaT3D.mul(vpT3Dinv);
		Transform3D newT3D = new Transform3D();
		newT3D.mul(absoluteDeltaT3D, oldT3D);
		
		newT3D.normalize();
		presentation.setTransform(newT3D);
		setTransform(newT3D, true);
	}

	public void refresh() {
		Transform3D t3d = new Transform3D();
		presentation.getTransform(t3d);
		t3d.setRotation(abstraction.getOrientation());
		t3d.setTranslation(abstraction.getPosition());
		t3d.normalize();
		if (getUsedBy().isEmpty() && presentation.isBillBoardExisting())
			presentation.hideBillBoard();
		else if (!presentation.isBillBoardExisting() && !getUsedBy().isEmpty()) {
			presentation.showBillBoard();
		}
		presentation.setTransform(t3d);
	}

	public PObject getPresentation() {
		return presentation;
	}

	public Vector3d getPosition() {
		return abstraction.getPosition();
	}

	public void setPosition(Vector3d position, boolean diffuse) {
		abstraction.setPosition(position, false);
		if (diffuse) {
			try {
				serverProxy.update(abstraction);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	public Quat4d getOrientation() {
		return abstraction.getOrientation();
	}

	public void setOrientation(Quat4d orientation, boolean diffuse) {
		abstraction.setOrientation(orientation, false);
		if (diffuse) {
			try {
				serverProxy.update(abstraction);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	public String getName() {
		return abstraction.getName();
	}

	public String getURLGeometry() {
		return abstraction.getURLGeometry();
	}

	public void setTransform(Transform3D t3d, boolean diffuse) {
		abstraction.setTransform(t3d, false);
		if (diffuse) {
			try {
				serverProxy.update(abstraction);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	public AObject getAbstraction() {
		return abstraction;
	}

	public String getUsedBy() {
		return abstraction.getUsedBy();
	}

	public void setUsedBy(String usedBy, boolean diffuse) {
		abstraction.setUsedBy(usedBy, false);
		if (diffuse) {
			try {
				serverProxy.update(abstraction);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

}

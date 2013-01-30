package j3d.controller.universe;

import ihm.app.CanvasExtended;
import ihm.interaction.mouse.MouseInteractor;
import j3d.abstraction.universe.ACamera;
import j3d.abstraction.universe.AObject;
import j3d.interfaces.universe.ICamera;
import j3d.interfaces.universe.IObject;
import j3d.interfaces.universe.ISharedUniverse;
import j3d.interfaces.universe.ISharedUniverseServer;
import j3d.presentation.universe.PSharedUnivrese;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CSharedUniverse implements ISharedUniverse {

	private PSharedUnivrese presentation;
	private ISharedUniverseServer serverProxy;

	private Map<String, IObject> cObjects;
	private Map<String, ICamera> cCameras;
	public static CCamera cCameraUser = null;

	public CSharedUniverse(ISharedUniverseServer serverProxy,
			CanvasExtended canvas) {
		this.serverProxy = serverProxy;
		cObjects = new HashMap<String, IObject>();
		cCameras = new HashMap<String, ICamera>();
		presentation = new PSharedUnivrese(this, canvas);

	}

	public void init() throws RemoteException {
		for (ICamera camera : serverProxy.getCameras()) {
			if (cCameraUser == null
					|| (!camera.getOwnerName().equals(
							cCameraUser.getOwnerName()))) {
				System.out.println("===========> " + camera.getOwnerName() +  " <> "+ cCameraUser.getOwnerName());
				CCamera cCamera = new CCamera((ACamera) camera, 
						serverProxy, "http://espacezives.free.fr/pyramid.wrl");
				cCameras.put(cCamera.getOwnerName(), cCamera);
				presentation.add(cCamera.getPresentation());
				cCamera.refresh();
			}
		}

		for (IObject object : serverProxy.getObjects()) {
			CObject cObject = new CObject((AObject) object, serverProxy);
			cObjects.put(cObject.getName(), cObject);
			presentation.add(cObject.getPresentation());
			cObject.refresh();
		}
	}

	public PSharedUnivrese getPresentation() {
		return presentation;
	}

	public boolean add(IObject object) {
		if (object instanceof CObject) {
			try {
				if (serverProxy.add(object.getAbstraction())) {
					presentation.add(((CObject) object).getPresentation());
					cObjects.put(object.getName(), object);
					return true;
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public void remove(IObject object) {
		if (object instanceof CObject) {
			try {
				if (serverProxy.getObjects().contains(object)) {
					presentation.remove(((CObject) object).getPresentation());
					cObjects.remove(object);
				}
				serverProxy.remove(object);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}

	}

	public boolean add(ICamera camera) {
		if (camera instanceof CCamera) {
			try {
				if (serverProxy.add(camera.getAbstraction())) {
					presentation.add(((CCamera) camera).getPresentation());
					cCameras.put(camera.getOwnerName(), camera);
					return true;
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public void remove(ICamera camera) {
		if (camera instanceof CCamera) {
			try {
				if (serverProxy.getObjects().contains(camera)) {
					presentation.remove(((CCamera) camera).getPresentation());
					cCameras.put(camera.getOwnerName(), camera);
				}
				serverProxy.remove(camera);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}

	}

	public void addMouseInteractor(MouseInteractor mi) {
		presentation.add(mi);
	}

	public Collection<IObject> getObjects() {
		try {
			return serverProxy.getObjects();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Collection<ICamera> getCameras() {
		try {
			return serverProxy.getCameras();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return null;
	}

	public IObject getObject(String name) {
		try {
			return serverProxy.getObject(name);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return null;
	}

	public ICamera getCamera(String name) {
		try {
			return serverProxy.getCamera(name);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return null;
	}

	public ICamera getCameraUser() {
		return cCameraUser;
	}

	public void setCameraUser(CCamera camera, boolean pushOnServer) {
		if (cCameraUser != null && pushOnServer)
			remove(cCameraUser);
		cCameraUser = camera;
		if (pushOnServer)
			add(cCameraUser);
	}

	public void update(ICamera camera) {
		ICamera cCamera = cCameras.get(camera.getOwnerName());
		if (cCameraUser != null
				&& camera.getOwnerName().equals(cCameraUser.getOwnerName()))
			return;
		if (cCamera != null) {
			cCamera.setPosition(camera.getPosition(), false);
			cCamera.setOrientation(camera.getOrientation(), false);
			((CCamera) cCamera).refresh();
		} else {
			CCamera cCameraNew = new CCamera((ACamera) camera, 
					serverProxy, "http://espacezives.free.fr/pyramid.wrl");
			cCameras.put(cCameraNew.getOwnerName(), cCameraNew);
			presentation.add(cCameraNew.getPresentation());
			cCameraNew.refresh();
		}

	}

	public void update(IObject object) {
		IObject cObject = cObjects.get(object.getName());
		if (cObject != null) {
			cObject.setOrientation(object.getOrientation(), false);
			cObject.setPosition(object.getPosition(), false);
			((CObject) cObject).refresh();
		} else {
			CObject cObjectNew = new CObject((AObject) object, serverProxy);
			cObjects.put(cObjectNew.getName(), cObjectNew);
			presentation.add(cObjectNew.getPresentation());
			cObjectNew.refresh();
		}

	}

	public String getName() {
		try {
			return serverProxy.getName();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return null;
	}

}

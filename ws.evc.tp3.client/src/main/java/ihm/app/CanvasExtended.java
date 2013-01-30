package ihm.app;

import ihm.interaction.keyboard.KeyCameraStateForMouseInteractor;
import ihm.interaction.mouse.MouseInteractor;
import ihm.interaction.mouse.MouseStimulusCamera;
import ihm.interaction.mouse.MouseStimulusObject;
import j3d.abstraction.universe.ACamera;
import j3d.abstraction.universe.AObject;
import j3d.controller.universe.CCamera;
import j3d.controller.universe.CObject;
import j3d.controller.universe.CSharedUniverse;
import j3d.interfaces.universe.ISharedUniverseServer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.rmi.RemoteException;

import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.J3DGraphics2D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Point3d;

import client.Client;
import client.ReceiverUpdates;

import com.sun.j3d.utils.universe.SimpleUniverse;

public class CanvasExtended extends Canvas3D {

	private static final long serialVersionUID = 1L;
	private CSharedUniverse universe;

	private MouseInteractor mouseInteractor;
	private CCamera camera;
	private ISharedUniverseServer universeProxyServer;
	private ReceiverUpdates receiverUpdates;

	public CanvasExtended(String ownerName) {
		super(SimpleUniverse.getPreferredConfiguration());
		setDoubleBufferEnable(true);

		// Create a universe
		universeProxyServer = Client.getSharedUniverse(App.SERVER_NAME, "1234",
				"Pluton");
		universe = new CSharedUniverse(universeProxyServer, this);

		// Load a vrml model
		AObject aCube;
		if ("test".equals(ownerName)) {
			aCube = new AObject("cubeDe_" + ownerName + "_" + (Math.random() * 10),
					"http://espacezives.free.fr/coneVert.wrl");
		}
		else {
			aCube = new AObject("cubeDe_" + ownerName + "_" + (Math.random() * 10),
					"http://espacezives.free.fr/colorcube2.wrl");
		}
		CObject cube = new CObject(aCube, universeProxyServer);

		// Create a receiver
		receiverUpdates = new ReceiverUpdates("239.0.0.1", 1234);
		receiverUpdates.setDeportedClient(universe);

		

		// Check if user camera is existing in universe
		// True : assign the universe camera to the current user
		// False : create a new camera 
		ACamera existingCamera = (ACamera) universe.getCamera(ownerName);
		ACamera aCamera = null;
		if (existingCamera == null) 			
			aCamera = new ACamera(ownerName);
		else
			aCamera = existingCamera;
		
		TransformGroup tgCamera = universe.getPresentation()
				.getTransformgroupCamera();
		camera = new CCamera(aCamera, tgCamera, universeProxyServer);		
	
		// Refresh location of camera if already existing
		if (existingCamera != null) {
			camera.refresh();
		}
		else
			camera.relativeTranslate(0, 0, 5.0);
		
		universe.setCameraUser(camera, existingCamera == null);
		try {
			universe.init();
		} catch (RemoteException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		// Add personnal cube to the shared universe
				universe.add(cube);

		// Add a mouse interactor to the scene
		mouseInteractor = new MouseInteractor(universe.getPresentation()
				.getScene(), camera);
		mouseInteractor.setSchedulingBounds(new BoundingSphere(new Point3d(),
				1000.0));
		universe.addMouseInteractor(mouseInteractor);

		// Add a keylistener to the canvas
		addKeyListener(new KeyCameraStateForMouseInteractor(this,
				getMouseInteractor()));

		// Launch receiver client
		receiverUpdates.start();

	}

	public MouseInteractor getMouseInteractor() {
		return mouseInteractor;
	}

	public CCamera getCamera() {
		return camera;
	}

	@Override
	public void update(Graphics g) {
		super.update(g);
	}

	@Override
	public void postRender() {
		super.postRender();
		J3DGraphics2D g = getGraphics2D();
		Font myFont = new Font("Courier", Font.BOLD, 16);

		g.setFont(myFont);
		g.setColor(Color.WHITE);
		if (getMouseInteractor().getCurrentState() instanceof MouseStimulusObject)
			g.drawString("MODE OBJECT ON", 13, 17);
		else if (getMouseInteractor().getCurrentState() instanceof MouseStimulusCamera)
			g.drawString("MODE CAMERA ON", 13, 17);
		else
			g.drawString("UNKNOWN MODE", 13, 17);
		g.flush(false);
		g.dispose();
	}

}

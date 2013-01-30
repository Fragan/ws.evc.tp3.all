package j3d.presentation.universe;

import j3d.controller.universe.CCamera;
import j3d.object.VirtualObject;

import java.awt.Font;
import java.io.FileNotFoundException;

import javax.media.j3d.Appearance;
import javax.media.j3d.Billboard;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Font3D;
import javax.media.j3d.FontExtrusion;
import javax.media.j3d.Material;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Text3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;

import org.jdesktop.j3d.loaders.vrml97.VrmlLoader;

import tools.BillBoardFactory;

import com.sun.j3d.loaders.Scene;

//We have to simulate a TransformGroup
//because downcast is forbidden TransformGroup -> PCamera
public class PCamera extends TransformGroup {

	private CCamera controller;
	private TransformGroup realTgCamera;
	private Scene scene;

	public PCamera(CCamera camera) {
		this.controller = camera;
	}

	public PCamera(CCamera cCamera, TransformGroup tgCamera) {
		controller = cCamera;
		realTgCamera = tgCamera;
		if (realTgCamera == null)
			realTgCamera = new TransformGroup();
	}

	/**
	 * With a pyramid
	 * 
	 * @param cCamera
	 * @param tgCamera
	 * @param cameraObjectUrl
	 */
	public PCamera(CCamera cCamera, String cameraObjectUrl) {

		
		controller = cCamera;
		realTgCamera = new VirtualObject();

		realTgCamera.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		realTgCamera.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		realTgCamera.setCapability(TransformGroup.ENABLE_PICK_REPORTING);

		VrmlLoader loader = new VrmlLoader();
		try {
			scene = loader.load(cameraObjectUrl);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		// Pyramid
		realTgCamera.addChild(scene.getSceneGroup());

		// Billboard name
		BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),
				400.0);
		TransformGroup billboard = BillBoardFactory.createBillboard(controller.getOwnerName(),
				new Point3f(0f, 0f, 0f), Billboard.ROTATE_ABOUT_POINT,
				new Point3f(0f, 0f, 0f), bounds);
		realTgCamera.addChild(billboard);

		this.addChild(realTgCamera);
	}

	public CCamera getController() {
		return this.controller;
	}

	@Override
	public void getTransform(Transform3D t3d) {
		realTgCamera.getTransform(t3d);
	}

	@Override
	public void setTransform(Transform3D t3d) {
		realTgCamera.setTransform(t3d);
	}



}

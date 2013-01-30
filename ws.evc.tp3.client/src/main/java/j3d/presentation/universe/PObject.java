package j3d.presentation.universe;

import j3d.controller.universe.CObject;
import j3d.object.VirtualObject;

import java.io.FileNotFoundException;

import javax.media.j3d.Billboard;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;

import org.jdesktop.j3d.loaders.vrml97.VrmlLoader;

import tools.BillBoardFactory;

import com.sun.j3d.loaders.Scene;


//  La 3d
public class PObject extends VirtualObject {


	private CObject controller;
	private BranchGroup billboardOwnerName;

	public PObject(CObject controller, String urlGeometry) {
		this.controller = controller;
		
		this.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		this.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		this.setCapability(TransformGroup.ENABLE_PICK_REPORTING);	
		setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
		setCapability(TransformGroup.ALLOW_CHILDREN_READ);
		setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);

		VrmlLoader loader = new VrmlLoader();
		try {			
			Scene scene = loader.load(urlGeometry);
			this.addChild(scene.getSceneGroup());			
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		
		
	}

	public CObject getController() {
		return controller;
	}
	
	public void hideBillBoard() {
		if (billboardOwnerName != null) {
			billboardOwnerName.detach(); // Detaches this BranchGroup from its parent.
//			this.removeChild(billboardOwnerName);
			billboardOwnerName = null;
		}
		
	}
	
	public void showBillBoard() {				
		BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),
				400.0);
		TransformGroup tgb = BillBoardFactory.createBillboard(controller.getUsedBy(),
				new Point3f(0f, -1f, 0f), Billboard.ROTATE_ABOUT_POINT,
				new Point3f(0f, 0f, 0f), bounds);
		billboardOwnerName = new BranchGroup();
		billboardOwnerName.setCapability(BranchGroup.ALLOW_DETACH);
		billboardOwnerName.addChild(tgb);
		billboardOwnerName.compile();
		
		this.addChild(billboardOwnerName);
	}
	
	public boolean isBillBoardExisting() {
		return billboardOwnerName != null;
	}
	
	
}

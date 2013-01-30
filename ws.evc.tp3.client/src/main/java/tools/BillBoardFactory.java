package tools;

import java.awt.Font;

import javax.media.j3d.Appearance;
import javax.media.j3d.Billboard;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Font3D;
import javax.media.j3d.FontExtrusion;
import javax.media.j3d.Material;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Text3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;

public class BillBoardFactory {

	public static TransformGroup createBillboard(String szText,
			Point3f locationPoint, int nMode, Point3f billboardPoint,
			BoundingSphere bounds) {

		TransformGroup subTg = new TransformGroup();
		subTg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		Font3D f3d = new Font3D(new Font("SansSerif", Font.PLAIN, 1),
				new FontExtrusion());
		Text3D label3D = new Text3D(f3d, szText, locationPoint);

		Appearance app = new Appearance();

		Color3f black = new Color3f(0.1f, 0.1f, 0.1f);
		Color3f objColor = new Color3f(0.2f, 0.2f, 0.2f);

		app.setMaterial(new Material(objColor, black, objColor, black, 90.0f));
		Shape3D sh = new Shape3D(label3D, app);

		subTg.addChild(sh);

		Billboard billboard = new Billboard(subTg, nMode, billboardPoint);
		billboard.setSchedulingBounds(bounds);
		subTg.addChild(billboard);
		
		

		return subTg;
	}
}

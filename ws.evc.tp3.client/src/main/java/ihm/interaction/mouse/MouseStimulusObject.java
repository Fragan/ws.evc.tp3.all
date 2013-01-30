package ihm.interaction.mouse;

import ihm.interaction.mouse.MouseInteractor.MouseInteractorData;
import j3d.controller.universe.CObject;
import j3d.controller.universe.CSharedUniverse;
import j3d.presentation.universe.PObject;

import java.awt.AWTEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Enumeration;

import javax.media.j3d.Canvas3D;
import javax.media.j3d.Node;
import javax.media.j3d.WakeupOnAWTEvent;

import com.sun.j3d.utils.picking.PickCanvas;
import com.sun.j3d.utils.picking.PickResult;

public class MouseStimulusObject extends AMouseStimulusState {

	public MouseStimulusObject(MouseInteractorData msd) {
		super(msd);
	}

	@Override
	public void stimulus(@SuppressWarnings("rawtypes") Enumeration criteria) {
		while (criteria.hasMoreElements()) {
			WakeupOnAWTEvent w = (WakeupOnAWTEvent) criteria.nextElement();
			AWTEvent events[] = w.getAWTEvent();
			for (int i = 0; i < events.length; i++) {
				if (events[i].getID() == MouseEvent.MOUSE_PRESSED) { // TODO bloquer la sélection si pas son propre nom et non vide !!!!!!!
					if (((MouseEvent) events[i]).getButton() == MouseEvent.BUTTON1) {
						msd.button1Pressed = true;
					}
					if (((MouseEvent) events[i]).getButton() == MouseEvent.BUTTON2) {
						msd.button2Pressed = true;
					}
					if (((MouseEvent) events[i]).getButton() == MouseEvent.BUTTON3) {
						msd.button3Pressed = true;
					}
					if (msd.buttonsInUse == 0) {
						tryToPickUpObjectInInteraction((MouseEvent) events[i]);
						
					}
					msd.buttonsInUse++;
				} else if (events[i].getID() == MouseEvent.MOUSE_RELEASED) {
					msd.buttonsInUse--;
					
					if (msd.buttonsInUse == 0) {
						if (msd.cObjectInInteraction != null) {
							msd.cObjectInInteraction.setUsedBy("", true);
							System.err.println("Mouse released");
						}
						msd.cObjectInInteraction = null;
					}
					if (((MouseEvent) events[i]).getButton() == MouseEvent.BUTTON1) {
						msd.button1Pressed = false;
					}
					if (((MouseEvent) events[i]).getButton() == MouseEvent.BUTTON2) {
						msd.button2Pressed = false;
					}
					if (((MouseEvent) events[i]).getButton() == MouseEvent.BUTTON3) {
						msd.button3Pressed = false;
					}
				} else if (events[i].getID() == MouseEvent.MOUSE_DRAGGED) {
					if (msd.cObjectInInteraction != null) {
						double dx = 0, dy = 0, dz = 0;
						double dh = 0, dp = 0, dr = 0;
						msd.x2 = ((MouseEvent) events[i]).getX();
						msd.y2 = ((MouseEvent) events[i]).getY();
						if (msd.button1Pressed) { // rotation
							dh = (msd.x2 - msd.x1) /3.0;
							dp = (msd.y1 - msd.y2) / 3.0;
							msd.cObjectInInteraction.rotate(dh, dp, dr);
						}
						if (msd.button2Pressed ) { // zoom
							dz = (msd.x1 - msd.x2 + msd.y2 - msd.y1) / 40.0;
							msd.cObjectInInteraction.translate(dx, dy, dz);
						}
						if (msd.button3Pressed) { // translation dans le plan de
												// l'�cran
							dx = (msd.x2 - msd.x1) / 40.0;
							dy = (msd.y1 - msd.y2) / 40.0;
							msd.cObjectInInteraction.translate( dx, dy, dz);
						}
						
						msd.x1 = msd.x2;
						msd.y1 = msd.y2;
					}
				 } else if (events[i].getID() == MouseEvent.MOUSE_WHEEL) {
					 msd.cObjectInInteraction = null; //Otherwise, the scroll continue to interact with the object with the cursor outside of it.
					 tryToPickUpObjectInInteraction((MouseEvent) events[i]);
					 if (msd.cObjectInInteraction != null) {
						 MouseWheelEvent event = (MouseWheelEvent) events[i];
						 int rotates = event.getWheelRotation();
						 double dz = rotates;
						 msd.cObjectInInteraction.translate(0, 0, dz);
					 }
				 }
				
			}
		}

	}
	
	private void tryToPickUpObjectInInteraction(MouseEvent e) {
		PickCanvas pickShape = new PickCanvas(
				(Canvas3D) e.getSource(), msd.scene);
		pickShape.setShapeLocation((MouseEvent) e);
		msd.x1 = e.getX();
		msd.y1 = e.getY();
		PickResult[] sgPath = pickShape.pickAllSorted();
		if (sgPath != null) {
			try {
				Node node = sgPath[0]
						.getNode(PickResult.TRANSFORM_GROUP);
				if (node instanceof PObject) {
					CObject cObject = ((PObject) node).getController();
					if ("".equals(cObject.getUsedBy()) || CSharedUniverse.cCameraUser.getOwnerName().equals(cObject.getUsedBy())) {
						// Current user can't interact with the selected object if it used by an other user
						msd.cObjectInInteraction = cObject;
					}
				} else
					msd.cObjectInInteraction = null;
			} catch (Exception ex) {
				System.out.println(ex);
			}
		}
	}


}

package ihm.interaction.mouse;

import j3d.controller.universe.CCamera;
import j3d.controller.universe.CObject;
import j3d.scene.Scene;

import java.awt.AWTEvent;
import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnAWTEvent;
import javax.media.j3d.WakeupOr;


public class MouseInteractor extends Behavior {

		
	//We need a subclass to give private params to the MouseStimulusState
	class MouseInteractorData {	
		public int buttonsInUse;
		public boolean button1Pressed;
		public boolean button2Pressed;
		public boolean button3Pressed;
		public CObject cObjectInInteraction;
		
		public int x1;
		public int x2;
		public int y1;
		public int y2;
		
		public Scene scene;
		public CCamera cCamera;
		public WakeupOr wEvents;
		
	};
	
	private MouseInteractorData msd;
	private AMouseStimulusState currentState;

	public MouseInteractor(Scene scene, CCamera cCamera) {
		msd = new MouseInteractorData();		
		currentState = new MouseStimulusObject(msd);
		msd.scene = scene;
		msd.cCamera = cCamera;
	}

	
	public AMouseStimulusState getCurrentState() {
		return currentState;
	}


	public void switchCurrentState() {
		if (currentState instanceof MouseStimulusObject)
			currentState = new MouseStimulusCamera(msd);
		else 
			currentState = new MouseStimulusObject(msd);
	}


	@Override
	public void initialize() {
		WakeupOnAWTEvent wAWTEvent1 = new WakeupOnAWTEvent(
				AWTEvent.MOUSE_EVENT_MASK);

		WakeupOnAWTEvent wAWTEvent2 = new WakeupOnAWTEvent(
				AWTEvent.MOUSE_MOTION_EVENT_MASK);
		
		WakeupOnAWTEvent wAWTEvent3 = new WakeupOnAWTEvent(
				AWTEvent.MOUSE_WHEEL_EVENT_MASK);

		WakeupCriterion[] conditions = { wAWTEvent1, wAWTEvent2, wAWTEvent3 };

		msd.wEvents = new WakeupOr(conditions);
		wakeupOn(msd.wEvents);

		msd.buttonsInUse = 0;
		msd.button1Pressed = false;
		msd.button2Pressed = false;
		msd.button3Pressed = false;
	}

	
	public void processStimulus(@SuppressWarnings("rawtypes") Enumeration criteria) {
		currentState.stimulus(criteria);
		wakeupOn(msd.wEvents);
	}



}

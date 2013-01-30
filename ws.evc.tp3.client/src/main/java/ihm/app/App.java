package ihm.app;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class App extends JFrame {

	public static final String SERVER_NAME = "localhost";
	public static final boolean LOCAL_SERVER = false;
	
	private static final long serialVersionUID = 1L;
	private CanvasExtended canvas;
	private CameraControlsPanel cp;


	public App() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		String ownerPseudo = JOptionPane.showInputDialog(null, "Type your pseudo : ");
		if (ownerPseudo == null) {
			System.exit(0);
		}
		
		setTitle("By DOUCHEMENT & DEMIRDJIAN - Current user : " + ownerPseudo);
			
		
		//Initialize elements
		cp = new CameraControlsPanel();
		canvas = new CanvasExtended(ownerPseudo);
		
		//AddListener to camera controls panel
		cp.getTeleportToButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				canvas.getCamera().teleportTo(cp.getCoordX(), cp.getCoordY(), cp.getCoordZ()); 
			}
		});
		
		//Add Listener to camera rotation mode
		cp.getRbNormalMCR().addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent arg0) {			
				canvas.getCamera().setModeCameraRotationScene(false);
			}
		});
		cp.getRbSceneMCR().addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent arg0) {			
				canvas.getCamera().setModeCameraRotationScene(true);
			}
		});
		
		//Add elements
		setLayout(new BorderLayout());
		add(cp, BorderLayout.WEST);
		add(canvas, BorderLayout.CENTER);
		
		//Give focus to canvas
		SwingUtilities.invokeLater(new Runnable() {			
			public void run() {
				canvas.requestFocus();	
				canvas.requestFocusInWindow();
			}
		});		
						
		
		setSize(800, 600);
		setPreferredSize(getSize());
		pack();
	}
	
	
	
	public static void main(String[] args) {
		try {UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		App app = new App();
		app.setVisible(true);
	}
	
	

}

package graphiques;

import javax.swing.JFrame;
import java.awt.*;

public class MyFrame extends JFrame{
	
	Affichage affichage = new Affichage();
	private static final long serialVersionUID = -2977860217912678180L;
	
	public MyFrame() {

		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();

		this.setSize(screenSize.width,screenSize.height);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setUndecorated(true);
		this.add(affichage);
		affichage.setFocusable(false);
		this.addKeyListener(affichage.w.bind);
		this.addMouseListener(affichage.w.mouse);
		this.addMouseMotionListener(affichage.w.mouse);
	}
}

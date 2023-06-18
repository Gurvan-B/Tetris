package graphiques;

import javax.swing.JFrame;

public class MyFrame extends JFrame{
	
	Affichage affichage = new Affichage();
	private static final long serialVersionUID = -2977860217912678180L;
	
	public MyFrame() {
		this.setSize(1920,1080);
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

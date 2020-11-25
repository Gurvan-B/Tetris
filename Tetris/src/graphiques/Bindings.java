package graphiques;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Set;
import java.util.TreeSet;

import main.Player;
import main.World;

public class Bindings implements KeyListener {

//	public Player	leftPl;
//	public Player 	rightPl;
	public Set<Integer> keysPressed;
    private Object keyLock = new Object();
    private Key[] keys;
    public World w;
    private Player p;
	
	public Bindings(World w) {
//		this.leftPl = w.leftPlayer;
//		this.rightPl = w.rightPlayer;
		this.w=w;
		this.keysPressed = new TreeSet<Integer>();
		this.keys = new Key[256];
		initKeys();
	}
	
	public void processKeys() {
		
			
			// Left player
		if (p.start && !p.inPause) {
			
			if( keys[KeyEvent.VK_Q].mustProcess() ) {
				p.shape.goLeft();
				p.processHit(1);
			}
			
			
			if( keys[KeyEvent.VK_D].mustProcess() ) {
				p.shape.goRight();
				p.processHit(1);
			}
			
			if( keys[KeyEvent.VK_S].mustProcess() ) {
				p.shape.rotateLeft(true);
				p.processHit(1);
			}
			
			if( keys[KeyEvent.VK_Z].mustProcess() ) {
				p.shape.rotateRight(true);
				p.processHit(1);
			}
			
			if( keys[KeyEvent.VK_SPACE].mustProcess() ) {
				p.goMaxDown();
			}
			
			if( keys[KeyEvent.VK_SHIFT].mustProcess() ) {
				p.goDownFaster = true;
			} else {
				p.goDownFaster = false;
			}
			
		}
		
			// Pause and Escape
			
			if( keys[KeyEvent.VK_P].mustProcess() ) {
				p.inPause = !p.inPause;
			}

			if( keys[KeyEvent.VK_ESCAPE].mustProcess() ) {
				System.exit(0);
			}
			if( keys[KeyEvent.VK_F].mustProcess() ) {
				w.showFps = ! w.showFps;
			}
		}

	
	
		public void initKeys() {
			
			keys[KeyEvent.VK_Q] = new Key(KeyEvent.VK_Q, 120);
			keys[KeyEvent.VK_D] = new Key(KeyEvent.VK_D, 120);
			keys[KeyEvent.VK_SHIFT] = new Key(KeyEvent.VK_SHIFT, 0);
			keys[KeyEvent.VK_S] = new Key(KeyEvent.VK_S, 170);
			keys[KeyEvent.VK_Z] = new Key(KeyEvent.VK_Z, 170);
			keys[KeyEvent.VK_SPACE] = new Key(KeyEvent.VK_SPACE, 300);
			
//			keys[KeyEvent.VK_LEFT] = new Key(KeyEvent.VK_LEFT, 6);
//			keys[KeyEvent.VK_RIGHT] = new Key(KeyEvent.VK_RIGHT, 6);
//			keys[KeyEvent.VK_INSERT] = new Key(KeyEvent.VK_INSERT, 0);
//			keys[KeyEvent.VK_DOWN] = new Key(KeyEvent.VK_DOWN, 10);
//			keys[KeyEvent.VK_UP] = new Key(KeyEvent.VK_UP, 10);
//			keys[KeyEvent.VK_ENTER] = new Key(KeyEvent.VK_ENTER, 16);
			
			keys[KeyEvent.VK_P] = new Key(KeyEvent.VK_P, 500);
			keys[KeyEvent.VK_ESCAPE] = new Key(KeyEvent.VK_ESCAPE,0);
			keys[KeyEvent.VK_F] = new Key(KeyEvent.VK_F,300);
		}
		
	
	@Override
	public void keyPressed(KeyEvent arg0) {
	
		synchronized (keyLock) {
			keysPressed.add(arg0.getKeyCode());
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		synchronized (keyLock) {
			keysPressed.remove(arg0.getKeyCode());
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		
		
	}
	

	private class Key {

		private int keyEvent;
		private long timeSincePerformed;
		private long millisecUntilPerform;
		
		public Key(int keyEvent, int millisecUntilPerform ) {
			this.keyEvent = keyEvent;
			this.timeSincePerformed = 0;
			this.millisecUntilPerform = millisecUntilPerform;
		}
		
		public boolean isPressed() {
			synchronized (keyLock) {
				return keysPressed.contains(keyEvent);
			}
		}
		
		public boolean mustProcess() {
			if ( this.isPressed() && (timeSincePerformed+millisecUntilPerform <= System.currentTimeMillis()) ) {
				timeSincePerformed = System.currentTimeMillis();
				return true;
			} else {
				return false;
			}
		}
	}


	public void updatePlayer() {
		if(w.playingLeft) this.p = w.leftPlayer;
		else this.p = w.rightPlayer;
		
	}


//	InputMap im = this.getInputMap(WHEN_IN_FOCUSED_WINDOW);
//	im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT,0), "R Right");
//	ActionMap ap = this.getActionMap();
//	ap.put("R Right", new AbstractAction() {
//
//		/**
//		 * 
//		 */
//		private static final long serialVersionUID = 1L;
//
//		@Override
//		public void actionPerformed(ActionEvent arg0) {
////			w.leftPlayer.inPause = !w.leftPlayer.inPause;
////			w.rightPlayer.inPause = !w.rightPlayer.inPause;
//			w.rightPlayer.shape.goRight();
//			w.rightPlayer.processHit(1);
//		}
//		
//	});
//	
//	im.put(KeyStroke.getKeyStroke(KeyEvent.VK_D,0), "L Right");
//	ap.put("L Right", new AbstractAction() {
//
//		/**
//		 * 
//		 */
//		private static final long serialVersionUID = 1L;
//
//		@Override
//		public void actionPerformed(ActionEvent arg0) {
//			w.leftPlayer.shape.goRight();
//			w.leftPlayer.processHit(1);
//		}
//		
//	});
}

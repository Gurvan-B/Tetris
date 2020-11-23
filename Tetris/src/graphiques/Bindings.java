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
		if (w.leftPlayer.start && !w.leftPlayer.inPause) {
			
			if( keys[KeyEvent.VK_Q].mustProcess() ) {
				w.leftPlayer.shape.goLeft();
				w.leftPlayer.processHit(1);
			}
			
			
			if( keys[KeyEvent.VK_D].mustProcess() ) {
				w.leftPlayer.shape.goRight();
				w.leftPlayer.processHit(1);
			}
			
			if( keys[KeyEvent.VK_S].mustProcess() ) {
				w.leftPlayer.shape.rotateLeft(true);
				w.leftPlayer.processHit(1);
			}
			
			if( keys[KeyEvent.VK_Z].mustProcess() ) {
				w.leftPlayer.shape.rotateRight(true);
				w.leftPlayer.processHit(1);
			}
			
			if( keys[KeyEvent.VK_SPACE].mustProcess() ) {
				w.leftPlayer.goMaxDown();
			}
			
			if( keys[KeyEvent.VK_SHIFT].mustProcess() ) {
				w.leftPlayer.goDownFaster = true;
			} else {
				w.leftPlayer.goDownFaster = false;
			}
			
		}
		
			// Right player
		if (w.rightPlayer.start && !w.rightPlayer.inPause) {	
			if( keys[KeyEvent.VK_LEFT].mustProcess() ) {
				w.rightPlayer.shape.goLeft();
				w.rightPlayer.processHit(1);
			}
			
			
			if( keys[KeyEvent.VK_RIGHT].mustProcess() ) {
				w.rightPlayer.shape.goRight();
				w.rightPlayer.processHit(1);
			}
			
			if( keys[KeyEvent.VK_DOWN].mustProcess() ) {
				w.rightPlayer.shape.rotateLeft(true);
				w.rightPlayer.processHit(1);
			}
			
			if( keys[KeyEvent.VK_UP].mustProcess() ) {
				w.rightPlayer.shape.rotateRight(true);
				w.rightPlayer.processHit(1);
			}
			
			if( keys[KeyEvent.VK_ENTER].mustProcess() ) {
				w.rightPlayer.goMaxDown();
			}
			
			if( keys[KeyEvent.VK_INSERT].mustProcess() ) {
				w.rightPlayer.goDownFaster = true;
			} else {
				w.rightPlayer.goDownFaster = false;
			}
			
		}
			// Pause and Escape
			
			if( keys[KeyEvent.VK_P].mustProcess() ) {
				w.leftPlayer.inPause = !w.leftPlayer.inPause;
				w.rightPlayer.inPause = !w.rightPlayer.inPause;
			}

			if( keys[KeyEvent.VK_ESCAPE].mustProcess() ) {
				System.exit(0);
			}
		}

	
	
		public void initKeys() {
			
			keys[KeyEvent.VK_Q] = new Key(KeyEvent.VK_Q, 6);
			keys[KeyEvent.VK_D] = new Key(KeyEvent.VK_D, 6);
			keys[KeyEvent.VK_SHIFT] = new Key(KeyEvent.VK_SHIFT, 0);
			keys[KeyEvent.VK_S] = new Key(KeyEvent.VK_S, 10);
			keys[KeyEvent.VK_Z] = new Key(KeyEvent.VK_Z, 10);
			keys[KeyEvent.VK_SPACE] = new Key(KeyEvent.VK_SPACE, 16);
			
			keys[KeyEvent.VK_LEFT] = new Key(KeyEvent.VK_LEFT, 6);
			keys[KeyEvent.VK_RIGHT] = new Key(KeyEvent.VK_RIGHT, 6);
			keys[KeyEvent.VK_INSERT] = new Key(KeyEvent.VK_INSERT, 0);
			keys[KeyEvent.VK_DOWN] = new Key(KeyEvent.VK_DOWN, 10);
			keys[KeyEvent.VK_UP] = new Key(KeyEvent.VK_UP, 10);
			keys[KeyEvent.VK_ENTER] = new Key(KeyEvent.VK_ENTER, 16);
			
			keys[KeyEvent.VK_P] = new Key(KeyEvent.VK_P, 20);
			keys[KeyEvent.VK_ESCAPE] = new Key(KeyEvent.VK_ESCAPE, 6);
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
		private int frameSincePerformed;
		private int frameUntilPerform;
		
		public Key(int keyEvent, int frameUntilPerform ) {
			this.keyEvent = keyEvent;
			this.frameUntilPerform = frameUntilPerform;
		}
		
		public boolean isPressed() {
			synchronized (keyLock) {
				return keysPressed.contains(keyEvent);
			}
		}
		
		public boolean mustProcess() {
			if ( this.isPressed() && (frameSincePerformed >= frameUntilPerform) ) {
				frameSincePerformed = 0;
				return true;
			} else {
				frameSincePerformed ++;
				return false;
			}
		}
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

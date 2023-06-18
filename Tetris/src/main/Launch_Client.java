package main;

import graphiques.MyFrame;

public class Launch_Client {

	public static void main(String[] args) {
		// Permet de désactiver le DPI Scaling
		System.setProperty("sun.java2d.uiScale.enabled", "false");
		
		MyFrame myFrame = new MyFrame();
		myFrame.setVisible(true);
	}

}

package shape;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class TextureLoader {
	public BufferedImage purple;
	public BufferedImage cyan;
	public BufferedImage blue;
	public BufferedImage orange;
	public BufferedImage yellow;
	public BufferedImage red;
	public BufferedImage green;
	
	public TextureLoader() {
		purple = loadImage("textures/purple");
		cyan = loadImage("textures/cyan");
		blue = loadImage("textures/blue");
		orange = loadImage("textures/orange");
		yellow = loadImage("textures/yellow");
		red = loadImage("textures/red");
		green = loadImage("textures/green");
	}
	
	BufferedImage loadImage(String fn) {	
		InputStream inputStr = this.getClass().getResourceAsStream("/" + fn + ".png");
			
		BufferedImage image = null;
		
		try {
			image = ImageIO.read(inputStr);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}
}

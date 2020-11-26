package display;

import java.io.BufferedInputStream;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

public class AudioFile {

//	private File					soundFile;
	private AudioInputStream		ais;
	private AudioFormat				format;
	private DataLine.Info			info;
	private Clip 					clip;
	private boolean					loop;
	
	public AudioFile (String fileName, boolean loop) {
		
//		String url = "C:\\Users\\GURVAN\\git\\Tetris";
//		System.out.println(url + "ressources\\" + fileName + ".wav");
		
		
        this.loop = loop;
//		soundFile = new File(FileSystems.getDefault().getPath("").toAbsolutePath()+"\\Ressources\\" + fileName + ".wav");
		
//		URL imageURL = this.getClass().getClassLoader().getResource(fileName);
//		if (imageURL == null) {
//			System.out.println("null URL from filname");
//			System.exit(0);
//		}
//		try {
//			soundFile = new File(imageURL.toURI());
//		} catch (URISyntaxException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		
		try {
//			URL is = this.getClass().getResource("../ressources/musique.wav");
//			System.out.println(is);
//			ais = AudioSystem.getAudioInputStream(is);
			InputStream in = this.getClass().getResourceAsStream("/" +fileName + ".wav");
//			System.out.println(in);
			InputStream bufferedIn = new BufferedInputStream(in);
			ais = AudioSystem.getAudioInputStream(bufferedIn);
//			ais = AudioSystem.getAudioInputStream(in);
//			ais = (AudioInputStream) this.getClass().getClassLoader().getResourceAsStream(fileName);
			format = ais.getFormat();
			info = new DataLine.Info(Clip.class, format);
			clip = (Clip) AudioSystem.getLine(info);
			clip.open(ais);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
		
	public void play() {

		clip.start();
		if (loop)
			clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	public void restart() {
		clip.setFramePosition(0);
		clip.start();
		if (loop)
			clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	public void stop() {
		clip.setFramePosition(0);
		clip.stop();
	}
	


}

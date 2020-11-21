package display;

import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

public class AudioFile {

	private File					soundFile;
	private AudioInputStream		ais;
	private AudioFormat				format;
	private DataLine.Info			info;
	private Clip 					clip;
	private boolean					loop;
	
	public AudioFile (String fileName, boolean loop) {
		
		String url = "C:\\Users\\GURVAN\\eclipse-workspace";
//		System.out.println(url + "ressources\\" + fileName + ".wav");
		
		
        this.loop = loop;
		soundFile = new File(url + "\\Tetris\\ressources\\" + fileName + ".wav");
//		soundFile = new File("C:\\Users\\GURVAN\\eclipse-workspace\\Tetris\\ressources\\bloc.wav");
		try {
			ais = AudioSystem.getAudioInputStream(soundFile);
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
	
	public void stop() {
		clip.setFramePosition(0);
		clip.stop();
	}
	


}

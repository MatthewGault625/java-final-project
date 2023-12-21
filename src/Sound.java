import java.net.URL;
import javax.sound.sampled.*;

public class Sound {

	Clip clip;
	URL soundURL[] = new URL[30];
	
	public Sound() {
		// Sound on breaking brick
		soundURL[0] = getClass().getResource("/sound/Explosion1.wav");
		// Music for the game
		soundURL[1] = getClass().getResource("/sound/Gamemusic1.wav");
	}
	
	public void setFile(int i) {
		
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
			clip = AudioSystem.getClip();
			clip.open(ais);
			
		} catch (Exception e) {
			
		}
		
	}
	
	public void play() {
		
		clip.start();
	}
	
	public void loop(){
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	public void stop() {
		clip.stop();
	}
}

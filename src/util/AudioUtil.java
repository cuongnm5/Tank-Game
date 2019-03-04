package util;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Sound tools
 */

@SuppressWarnings("deprecation")
public class AudioUtil {
	/**
	 * ̹Tank birth sound
	 */
	public static final String ADD = "audio/add.wav";
	/**
	 * Explosion sound
	 */
	public static final String BLAST = "audio/blast.wav";
	/**
	 *  Fire sound
	 */
	public static final String FIRE = "audio/fire.wav";
	/**
	 * Gameover sound
	 */
	public static final String GAMEOVER = "audio/gameover.wav";
	/**
	 * hit sound
	 */
	public static final String HIT = "audio/hit.wav";
	/**
	 * start sound
	 */
	public static final String START = "audio/start.wav";

	/**
	 * A method to get a collection of all background sound effects 
	 */
	public static List<AudioClip> getAudios() {
		List<AudioClip> audios = new ArrayList<>();
		try {
			audios.add(Applet.newAudioClip(new File(AudioUtil.START).toURL()));
			audios.add(Applet.newAudioClip(new File(AudioUtil.ADD).toURL()));
			audios.add(Applet.newAudioClip(new File(AudioUtil.BLAST).toURL()));
			audios.add(Applet.newAudioClip(new File(AudioUtil.FIRE).toURL()));
			audios.add(Applet.newAudioClip(new File(AudioUtil.HIT).toURL()));
			audios.add(Applet.newAudioClip(new File(AudioUtil.GAMEOVER).toURL()));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// audios.add(Applet.newAudioClip(AudioUtil.class.getResource(AudioUtil.BGM)));
		return audios;
	}
}
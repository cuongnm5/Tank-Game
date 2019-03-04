package util;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Background music player
 */
public class AudioPlayer {
	private String fileName = null;
	private AudioInputStream cin = null;
	private AudioFormat format = null;
	private DataLine.Info info = null;
	private SourceDataLine line = null;

	private boolean stop = false;

	public AudioPlayer(String fileName) {
		this.fileName = fileName;
	}

	@SuppressWarnings("deprecation")
	public void player() {
		try {
			cin = AudioSystem.getAudioInputStream(new File(fileName).toURL());
			format = cin.getFormat();
			info = new DataLine.Info(SourceDataLine.class, format);
			line = (SourceDataLine) AudioSystem.getLine(info);

			line.open(format);
			line.start();

			int len = 0;
			byte[] buffer = new byte[512];

			while (!stop) {
				len = cin.read(buffer, 0, buffer.length);
				if (len <= 0) {
					break;
				}
				line.write(buffer, 0, len);
			}
			line.drain();
			line.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		if (line != null) {
			line.stop();
			stop = true;
		}
	}

	/**
	 * Internal class implements playing music thread
	 *
	 */
	public class AudioThread extends Thread {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			player();
			super.run();
		}
	}
}

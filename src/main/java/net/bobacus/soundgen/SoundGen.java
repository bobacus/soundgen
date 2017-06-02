package net.bobacus.soundgen;

import java.io.File;
import java.io.IOException;
import java.util.List;

import net.bobacus.soundgen.sampler.Sampler;
import net.bobacus.soundgen.sampler.SamplerParams;

public class SoundGen {

	/**
	 * @param args the first argument is the name of the music file to play
	 */
	public static void main(String[] args) {
		try {
			playMusic(args[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void playMusic(String filename) throws IOException {
		SamplerParams params = new SamplerParams(44100, 16, 1);
		Music music = new Music(new File(filename));
		List<? extends Sampler> samplers = music.getSamplers(params);
		int duration = (int)(music.getDuration()*params.getSampleRate());
		MultiChannelPlayer player = new MultiChannelPlayer();
		player.play(samplers, duration, params);
	}
	
}

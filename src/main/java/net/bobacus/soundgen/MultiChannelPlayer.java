package net.bobacus.soundgen;

import java.util.Iterator;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import net.bobacus.soundgen.sampler.Sample;
import net.bobacus.soundgen.sampler.SampleChunk;
import net.bobacus.soundgen.sampler.Sampler;
import net.bobacus.soundgen.sampler.SamplerParams;

class MultiChannelPlayer {

	/**
	 * @param samplers the samplers to use (one per channel)
	 * @param duration the total duration of audio to play, as the number of samples to be output
	 * @param params the sampler parameters
	 * @throws SoundGenException if a LineUnavailableException was thrown when getting or opening the audio line.
	 */
	void play(List<? extends Sampler> samplers, int duration, SamplerParams params)
	{
		AudioFormat format = new AudioFormat(
				params.getSampleRate(), params.getBits(), params.getChannels(), true, false);
		SourceDataLine line;
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
		try {
			line = (SourceDataLine) AudioSystem.getLine(info);
			line.open(format);
		} catch (LineUnavailableException e) {
			throw new SoundGenException(e);
		}
		line.start();
		int channelCount = samplers.size();
		int chunkSize = params.getSampleRate() / 2;
		for (int p = 0; p < duration; ) {
			int q = (duration - p >= chunkSize ? chunkSize : duration - p);
			double[] samples = new double[q];
			for (Sampler s : samplers) {
				Iterator<SampleChunk> ssIterator = s.getSamples(q, p);
				int i = 0;
				for (; ssIterator.hasNext() && i<q; ) {
					SampleChunk c = ssIterator.next();
					double[] ss = c.getSamples();
					for (int j = 0; i<q && j<ss.length; i ++, j ++) {
						samples[i] += ss[j];
					}
				}
			}
			p += q;
			int dataSize = q*2; 
			byte[] data = new byte[dataSize];
			for (int i = 0; i<q; i ++) {
				double sample = samples[i]/channelCount;
				data[i*2] = Sample.getLittleByte(sample);
				data[i*2+1] = Sample.getBigByte(sample);
			}
			line.write(data, 0, dataSize);
		}
		line.drain();
		line.stop();
		line.close();
	}

}

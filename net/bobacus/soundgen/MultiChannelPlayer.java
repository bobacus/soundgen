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

public class MultiChannelPlayer {

	/**
	 * LineAvailableException may be caught and rethrown inside a SoundGenException
	 * @param samplers
	 * @param duration
	 * @param params
	 * @throws SoundGenException
	 */
	public void play(List<? extends Sampler> samplers, int duration, SamplerParams params)
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
		int d = duration;
		int channelCount = samplers.size();
		int chunkSize = params.getSampleRate() / 2;
		for (int p = 0; p < d; ) {
			int q = (d-p >= chunkSize ? chunkSize : d-p);
			double[] samples = new double[q];
			for (Sampler s : samplers) {
				Iterator<SampleChunk> ssIter = s.getSamples(q, p);
				int i = 0;
				for (; ssIter.hasNext() && i<q; ) {
					SampleChunk c = ssIter.next();
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

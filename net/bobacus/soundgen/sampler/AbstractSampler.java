package net.bobacus.soundgen.sampler;

import java.util.Iterator;


public abstract class AbstractSampler implements Sampler {
	// we always use PCM, signed, little-endian. bits may be 8 or 16. channels is 1 only for now.
	public AbstractSampler(SamplerParams p) {
		mParams = p;
		mSampleRate = p.getSampleRate();
		mBits = p.getBits();
		mChannels = p.getChannels();
	}
	
	public AbstractSampler() {
		this(new SamplerParams(44100, 8, 1));
	}
	

	protected final SamplerParams mParams;
	protected final int mSampleRate;
	protected final int mBits;
	protected final int mChannels;
	
	
	// returns array(s) of samples, for duration-seconds' worth from the specified SynthFunction 
	
	// duration is number of samples
	public Iterator<SampleChunk> getSamples(int duration) {
		return getSamples(duration, 0);
	}

	// duration is time in seconds
	public Iterator<SampleChunk> getSamples(double duration) {
		return getSamples((int)(duration*mSampleRate), 0);
	}

	// use numbers of samples instead of time
	public abstract Iterator<SampleChunk> getSamples(int duration, int start);
}

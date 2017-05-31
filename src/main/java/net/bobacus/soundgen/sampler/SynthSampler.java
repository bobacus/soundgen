package net.bobacus.soundgen.sampler;

import java.util.Iterator;

import net.bobacus.iterators.SingleIterator;
import net.bobacus.soundgen.synth.Synth;

public class SynthSampler extends AbstractSampler {

	public SynthSampler(Synth s, SamplerParams p) {
		super(p);
		mSynth = s;
		xInc = 1.0/mSampleRate;
	}
	
	private final Synth mSynth;
	private final double xInc;
	
	@Override
	public Iterator<SampleChunk> getSamples(int duration, int start) {
		double [] b = new double[duration];
		double x = start/(double)mSampleRate;
		for (int i = 0; i<duration; i++) {
			b[i] = mSynth.getValue(x);
			x += xInc;
		}
		return SingleIterator.create(new SampleChunk(b));
	}

}

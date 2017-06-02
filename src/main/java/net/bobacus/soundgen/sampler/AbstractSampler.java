package net.bobacus.soundgen.sampler;


abstract class AbstractSampler implements Sampler {

	AbstractSampler(SamplerParams p) {
		mParams = p;
		mSampleRate = p.getSampleRate();
    }

	final SamplerParams mParams;
	final int mSampleRate;

}

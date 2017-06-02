package net.bobacus.soundgen.sampler;

import java.util.Iterator;


public interface Sampler {
	/**
	 * 
	 * @param duration number of samples to get, >0
	 * @param start sample to start with
	 * @return a new iterator of sample chunks
	 */
	Iterator<SampleChunk> getSamples(int duration, int start);

}

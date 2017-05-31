package net.bobacus.soundgen.sampler;

import java.util.Iterator;


public interface Sampler {
	/**
	 * 
	 * @param duration number of samples to get, >0
	 * @param start sample to start with
	 * @return
	 */
	public Iterator<SampleChunk> getSamples(int duration, int start);
	
	/**
	 * 
	 * @param duration number of samples to get, >0
	 * @return
	 */
	public Iterator<SampleChunk> getSamples(int duration);
	
	/**
	 * 
	 * @param duration number of seconds' worth to get
	 * @return
	 */
	public Iterator<SampleChunk> getSamples(double duration);
}

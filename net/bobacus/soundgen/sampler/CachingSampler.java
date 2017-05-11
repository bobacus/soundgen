package net.bobacus.soundgen.sampler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CachingSampler extends AbstractSampler {

	public CachingSampler(Sampler sampler) {
		this.sampler = sampler;
		this.cache = new ArrayList<SampleChunk>();
	}
	
	private final Sampler sampler;
	
	private final List<SampleChunk> cache;
	
	private final int cachedChunkSize = 1024;
	
	@Override
	public Iterator<SampleChunk> getSamples(int duration, int start) {
		if (cachedChunkSize*cache.size() > start + duration) {
			extendCache(start + duration);
		}
		List<SampleChunk> chunkList = new ArrayList<SampleChunk>();
		
		// 1. If entirely within one chunk, just copy and return
		
		// 2. a) copy samples from first chunk into new chunk (if any)
		//    b) copy subsequent entire chunks (if any)
		//    c) copy samples from last chunk into new chunk (if any)
		
		int firstChunk = start / cachedChunkSize;
		int lastChunk = (start+duration-1) / cachedChunkSize;
		
		if (firstChunk==lastChunk) {
			double[] samples = new double[duration];
			double[] cachedSamples = cache.get(firstChunk).getSamples();
			int a = start % cachedChunkSize;
			for (int i = a, j = 0; j < duration; i ++, j ++) {
				samples[j] = cachedSamples[i]; 
			}
			chunkList.add(new SampleChunk(samples));
		} else {
			int chunkIndex = firstChunk;
			int a = start % cachedChunkSize;
			if (a>0) {
				double[] samples = new double[cachedChunkSize-a];
				double[] cachedSamples = cache.get(chunkIndex).getSamples();
				for (int i = a, j = 0; i < cachedChunkSize; i ++, j ++) {
					samples[j] = cachedSamples[i];
				}
				chunkList.add(new SampleChunk(samples));
				chunkIndex ++;
			}
			for (; chunkIndex < lastChunk; chunkIndex ++) {
				chunkList.add(cache.get(chunkIndex));
			}
			int b = (start + duration) % cachedChunkSize;
			if (b>0) {
				double[] samples = new double[b];
				double[] cachedSamples = cache.get(chunkIndex).getSamples();
				for (int i = 0, j = 0; i < b; i ++, j ++) {
					samples[j] = cachedSamples[i];
				}
				chunkList.add(new SampleChunk(samples));
			} else {
				chunkList.add(cache.get(chunkIndex));
			}
		}
		
		return chunkList.iterator();
	}

	// FIXME This is broken! The SampleChunks coming in may be arbitrary size! 
	void extendCache(int samples) {
		int chunks = ((samples+cachedChunkSize-1) / cachedChunkSize);
		for (int c = cache.size(); c < chunks; c ++) {
			Iterator<SampleChunk> iter = sampler.getSamples(cachedChunkSize, c * cachedChunkSize);
			while (iter.hasNext()) {
				cache.add(iter.next());
			}
		}
	}
}

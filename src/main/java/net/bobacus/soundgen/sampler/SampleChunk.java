package net.bobacus.soundgen.sampler;

public class SampleChunk {

	SampleChunk(double[] samples) {
		this.samples = samples;
	}
	
	public double[] getSamples() {
		return samples;
	}
	
	private final double[] samples;
}

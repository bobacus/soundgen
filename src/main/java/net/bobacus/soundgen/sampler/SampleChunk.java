package net.bobacus.soundgen.sampler;

/**
 * A sequence of samples, representing a chunk of the whole.
 */
public class SampleChunk {

    SampleChunk(double[] samples) {
        this.samples = samples;
    }

    public double[] getSamples() {
        return samples;
    }

    private final double[] samples;
}

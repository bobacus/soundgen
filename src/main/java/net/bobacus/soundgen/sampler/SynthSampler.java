package net.bobacus.soundgen.sampler;

import java.util.Iterator;

import net.bobacus.iterators.SingleIterator;
import net.bobacus.soundgen.synth.Synth;

public class SynthSampler extends AbstractSampler {

    SynthSampler(Synth s, SamplerParams p) {
        super(p);
        synth = s;
        xInc = 1.0 / params.getSampleRate();
    }

    private final Synth synth;
    private final double xInc;

    @Override
    public Iterator<SampleChunk> getSamples(int duration, int start) {
        double[] b = new double[duration];
        double x = start / (double) params.getSampleRate();
        for (int i = 0; i < duration; i++) {
            b[i] = synth.getValue(x);
            x += xInc;
        }
        return SingleIterator.create(new SampleChunk(b));
    }

}

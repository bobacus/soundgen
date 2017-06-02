package net.bobacus.soundgen.sampler;

import java.util.Iterator;

import net.bobacus.iterators.EmptyIterator;
import net.bobacus.soundgen.synth.Sound;

public class SoundSampler extends SynthSampler {

    SoundSampler(Sound s, SamplerParams p) {
        super(s, p);
        mSound = s;
    }

    private final Sound mSound;

    public Iterator<SampleChunk> getSamples(int duration, int start) {
        if (start >= mSound.getDuration() * mSampleRate) return EmptyIterator.get();
        int maxDuration = (int) (mSound.getDuration() * mSampleRate) - start;
        int d = duration <= maxDuration ? duration : maxDuration;
        return super.getSamples(d, start);
    }

}

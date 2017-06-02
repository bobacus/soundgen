package net.bobacus.soundgen.sampler;

import java.util.Iterator;

import net.bobacus.iterators.EmptyIterator;
import net.bobacus.soundgen.synth.Sound;

public class SoundSampler extends SynthSampler {

    SoundSampler(Sound s, SamplerParams p) {
        super(s, p);
        sound = s;
    }

    private final Sound sound;

    public Iterator<SampleChunk> getSamples(int duration, int start) {
        if (start >= sound.getDuration() * params.getSampleRate()) return EmptyIterator.get();
        int maxDuration = (int) (sound.getDuration() * params.getSampleRate()) - start;
        int d = duration <= maxDuration ? duration : maxDuration;
        return super.getSamples(d, start);
    }

}

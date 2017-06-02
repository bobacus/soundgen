package net.bobacus.soundgen.sampler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.bobacus.fn.PositionalFunction;
import net.bobacus.fn.PositionalFunctionIterator;
import net.bobacus.iterators.IteratorIterator;
import net.bobacus.soundgen.channel.Channel;
import net.bobacus.soundgen.synth.Sound;
import net.bobacus.util.LengthList;

public class ChannelSampler extends AbstractSampler {

    public ChannelSampler(Channel channel, SamplerParams p) {
        super(p);
        mSounds = channel.getSounds();
        analyze();
    }

    private final List<Sound> mSounds;

    private LengthList mLengths;

    public Iterator<SampleChunk> getSamples(int duration, int start) {
        System.out.println("Channel.Sampler.getSamples(duration=" + duration + ",start=" + start + ")");
        assert duration > 0;

        // which sound do we start with?
        int startSound = mLengths.getIndexForPosition(start);
        // where in that sound do we start?
        final int startOffset = mLengths.getOffsetForPosition(start);
        // which sound do we finish with? (inclusively)
        int endSound = mLengths.getIndexForPosition(start + duration - 1);
        // how much of that sound do we play? (what is the exclusive offset of ending)
        final int endOffset = mLengths.getOffsetForPosition(start + duration - 1);

        // First, get an Iterator for the sub-list of Sounds we're using (actually a ListIterator so we can identify first element)
        Iterator<Sound> soundsToUse = mSounds.subList(startSound, endSound + 1).iterator();

        // Given an Iterator<Sound>, construct an Iterator<SampleChunk>

        System.out.println("startSound = " + startSound + ", endSound = " + endSound + ", startOffset = "
                + startOffset + ", endOffset = " + endOffset);

        // Define a PositionalFunction that returns an Iterator<SampleChunk> from a Sound + position
        PositionalFunction<Sound, Iterator<SampleChunk>> fn = (sound, pos) -> {
            System.out.println("Playing sound " + pos.getIndex());
            Sampler sampler = new SoundSampler(sound, mParams);
            int soundStart = (pos.isFirst() ? startOffset : 0);
            int soundEnd = (pos.isLast() ? endOffset : (int) (mSampleRate * sound.getDuration()));
            int soundDuration = soundEnd - soundStart;
            // we get an Iterator<SampleChunk> whose elements need to be obtained and returned
            return sampler.getSamples(soundDuration, soundStart);
        };

        // Create a PositionalFunctionIterator to run the above function
        Iterator<Iterator<SampleChunk>> sampleChunkGenerator = PositionalFunctionIterator.create(soundsToUse, fn);

        // And an IteratorIterator to get those SampleChunks out

        return new IteratorIterator<>(sampleChunkGenerator);
    }

    private void analyze() {
        List<Integer> lengthList = new ArrayList<>(mSounds.size());
        for (Sound s : mSounds) {
            int samples = (int) (s.getDuration() * mSampleRate);
            lengthList.add(samples);
        }
        mLengths = new LengthList(lengthList);
    }

}

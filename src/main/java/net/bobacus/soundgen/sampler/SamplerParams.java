package net.bobacus.soundgen.sampler;

public class SamplerParams {
    public SamplerParams(int sampleRate, int bits, int channels) {
        mSampleRate = sampleRate;
        mBits = bits;
        mChannels = channels;
    }

    private final int mSampleRate;
    private final int mBits;
    private final int mChannels;

    public int getSampleRate() {
        return mSampleRate;
    }

    public int getBits() {
        return mBits;
    }

    public int getChannels() {
        return mChannels;
    }

}

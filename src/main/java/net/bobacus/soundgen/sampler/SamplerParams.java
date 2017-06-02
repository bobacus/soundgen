package net.bobacus.soundgen.sampler;

/**
 * Parameters for audio samplers and output.
 */
public final class SamplerParams {
    /**
     * @param sampleRate the sample rate, in samples per second
     * @param bits the resolution of the samples, either 8 or 16 bits
     * @param channels the number of output channels (1 for mono)
     */
    public SamplerParams(int sampleRate, int bits, int channels) {
        this.sampleRate = sampleRate;
        this.bits = bits;
        this.channels = channels;
    }

    private final int sampleRate;
    private final int bits;
    private final int channels;

    public int getSampleRate() {
        return sampleRate;
    }

    public int getBits() {
        return bits;
    }

    public int getChannels() {
        return channels;
    }

}

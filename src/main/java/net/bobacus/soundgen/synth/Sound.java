package net.bobacus.soundgen.synth;


/**
 * A Sound represents a synthesizer object with duration
 */
public class Sound extends Synth {
    public Sound(Synth synth, double duration) {
        this.duration = duration;
        this.synth = synth;
    }

    public double getValue(double t) {
        return t <= duration ? synth.getValue(t) : 0;
    }

    public double getDuration() {
        return duration;
    }

    private final Synth synth;
    private final double duration;

}

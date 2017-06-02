package net.bobacus.soundgen.synth;


/**
 * A Sound represents a synthesizer object with duration
 */
public class Sound extends Synth {
	public Sound(Synth s, double duration) {
		mDuration = duration;
		mSynth = s;
	}
	
	public double getValue(double t) {
		return t<=mDuration ? mSynth.getValue(t) : 0;
	}
	
	public double getDuration() { return mDuration; }

	private final Synth mSynth;
	private final double mDuration;
	
}

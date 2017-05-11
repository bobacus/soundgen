package net.bobacus.soundgen.synth;


// A Sound represents a synthesizer object with duration
public class Sound extends Synth {
	public Sound(Synth s, double duration) {
		mDuration = duration;
		// we construct a new synth object based on the values provided
		mSynth = s;
	}
	
	public double getValue(double t) {
		return t<=mDuration ? mSynth.getValue(t) : 0;
	}
	
	public double getDuration() { return mDuration; }

	public Synth getSynth() { return mSynth; }
	
	private Synth mSynth;
	private double mDuration;
	
}

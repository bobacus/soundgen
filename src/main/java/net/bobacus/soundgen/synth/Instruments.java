package net.bobacus.soundgen.synth;


public class Instruments {
	public static final Synth silence = new Synth.Zero();
	public static final Synth sine = new Synth.Sine();
	public static final Synth noise = new Synth.Noise();
	public static final Synth brownNoise = new Synth.BrownNoise();

	public static final Synth rapidDecay = new Synth.Exponential(0.97);
	public static final Synth tongued = new Synth.Mix2(new Synth.Offset(0.5),rapidDecay);

	public static final Synth glock =
		new Synth.Mix2(new Synth.MultiMix(rapidDecay,new Synth.Sine(3)),sine);

	public static final Synth clarinet =
		new Synth.Harmonic(new double[] {7.9,0.076,0.99,0.032,0.013,0.004,0.0052,0.001,0.0008,0.0006,0.0002});
	public static final Synth clarinetTongued = new Synth.MultiMix(tongued,clarinet);
	
	public static final Synth secondDecay = new Synth.Exponential(0.5);
	public static final Synth drum1 = new Synth.Composition(brownNoise, new Synth.Offset(0.4));
	public static final Synth drum2 = new Synth.Composition(brownNoise, new Synth.Exponential(0.95));
	public static final Synth drumDecay1 = new Synth.MultiMix(rapidDecay, drum1);
	public static final Synth drumDecay2 = new Synth.MultiMix(rapidDecay, drum2);
	
	public static final Synth bass = new Synth.Pitch(clarinetTongued, 0.125);
	
	public static final Synth bassSlide = new Synth.FM(bass, secondDecay);

	public static final Synth [] mInstruments = {
		silence, sine, glock, drum2, clarinet, clarinetTongued, bass
	};

}

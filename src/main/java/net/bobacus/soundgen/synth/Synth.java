package net.bobacus.soundgen.synth;

import java.util.Arrays;

import net.bobacus.util.CachingFactory;

// abstract out the essence of the synthesizer
public abstract class Synth {

	// returns value from -1 to 1, given a position
	public abstract double getValue(double position);

	static CachingFactory<Pitch> pitchFactory = new CachingFactory<Pitch>(Pitch.class, new Class<?>[] {Synth.class, double.class});
	
	// convenience method for getting a frequency-scaled version of a synth
	public Synth getPitch(double frequency) {
		return pitchFactory.getInstance(Arrays.asList(new Object[]{this,frequency}));
	}
	
		
	// Synthesizer function subclasses:
	public static class Sine extends Synth {
		public Sine() { mFrequency = 1.0; }
		public Sine(double frequency) { mFrequency = frequency; }
		public double getValue(double position) {
			return Math.sin(2*Math.PI*position*mFrequency);
		}
		private final double mFrequency;
	}
	
	// Noise (for percussion, e.g.) N.B. independent of parameter
	public static class Noise extends Synth {
		public Noise() { /* empty default constructor */ }
		public double getValue(double x) {
			return 2*Math.random()-1;
		}
	}
	
	// Brown noise - NB this is stateful
	public static class BrownNoise extends Synth {
		public BrownNoise() { /* empty */ }
		private double current = 0;
		public double getValue(double x) {
			double offset = x*(2.0*Math.random()-1);
			current += offset;
			if (current>1.0)
				current = 1.0;
			else if (current<-1.0) 
				current = -1.0;
			return current;
		}
	}
	
	
	// g = (f1 + f2) / 2
	public static class Mix2 extends Synth {
		public Mix2(Synth f1, Synth f2) { mF1 = f1; mF2 = f2; }
		public double getValue(double position) {
			return (mF1.getValue(position)+mF2.getValue(position))/2;
		}
		private final Synth mF1, mF2;
	}
	
	// g(x) = f(x*n)
	public static class Pitch extends Synth {
		public Pitch(Synth f, double frequency) { mF = f; mFrequency = frequency; }
		public double getValue(double position) {
			return (mF.getValue(position*mFrequency));
		}
		private final Synth mF;
		private final double mFrequency;
	}
	
	// g = 0 (for silence, e.g.)
	public static class Zero extends Synth {
		public Zero() { /* empty default constructor */ }
		public double getValue(double position) { return 0; }
	}
	
	public static class Offset extends Synth {
		public Offset(double value) { mValue = value; }
		public double getValue(double x) { return mValue; }
		private final double mValue;
	}
	
	// sequencer: h(x) = f(x) for 0<x<a, and g(x-a) for x>=a
	public static class Sequence extends Synth {
		public Sequence(Synth f, double fDuration, Synth g) {
			mF = f; mG = g; mFDuration = fDuration;
		}
		public double getValue(double position) {
			return (position < mFDuration) ? mF.getValue(position) : mG.getValue(position-mFDuration);
		}
		private final Synth mF, mG;
		private final double mFDuration;
	}
	
	// multiplicative mixer: h = fg (e.g. for envelopes)
	public static class MultiMix extends Synth {
		public MultiMix(Synth f, Synth g) {
			mF = f; mG = g;
		}
		public double getValue(double position) {
			return mF.getValue(position)*mG.getValue(position);
		}
		private final Synth mF, mG;
	}
	
	// exponential (e.g. for decay)
	public static class Exponential extends Synth {
		public Exponential(double e) { mE = e; }
		public double getValue(double position) { return Math.pow(mE, position); }
		private final double mE;
	}
	
	// harmonics - specify amplitudes
	public static class Harmonic extends Synth {
		public Harmonic(double[] amps) {
			mAmps = amps;
		}
		public double getValue(double x) {
			double c = 0;
			for (int i = 0; i<mAmps.length; i ++) {
				double y = mAmps[i]*Math.sin(2*Math.PI*x*(i+1));
				c += y;
			}
			return c/mAmps.length;
		}
		private final double[] mAmps;
	}
	
	// composition: h = f o g
	public static class Composition extends Synth {
		public Composition(Synth f, Synth g) {
			mF = f; mG = g;
		}
		public double getValue(double x) {
			return mF.getValue(mG.getValue(x));
		}
		private final Synth mF, mG;
	}
	
	// frequency function
	public static class FM extends Synth {
		public FM(Synth s, Synth freq) {
			this.s = s;
			this.freq = freq;
			this.pitch = 1;
		}
		public FM(FM orig, double pitch) {
			this.s = orig.s;
			this.freq = orig.freq;
			this.pitch = pitch;
			System.out.println("FM: s="+s+", freq="+freq+", pitch="+pitch);
		}
		static CachingFactory<FM> fmFactory = new CachingFactory<FM>(FM.class, new Class<?>[] {FM.class, double.class});
		@Override
		public Synth getPitch(double pitch) {
			return fmFactory.getInstance(Arrays.asList(new Object[]{this,pitch}));
		}
		public double getValue(double x) {
			return s.getValue(x*pitch*freq.getValue(x));
		}
		private final Synth s;
		private final Synth freq;
		private final double pitch;
	}
}

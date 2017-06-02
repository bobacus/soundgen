package net.bobacus.soundgen.synth;

// abstract out the essence of the synthesizer
public abstract class Synth {

    // returns value from -1 to 1, given a position
    public abstract double getValue(double position);

    // convenience method for getting a frequency-scaled version of a synth
    public Synth getPitch(double frequency) {
        return new Pitch(this, frequency);
    }


    // Synthesizer function subclasses:
    public static class Sine extends Synth {
        Sine() {
            frequency = 1.0;
        }

        Sine(double frequency) {
            this.frequency = frequency;
        }

        public double getValue(double position) {
            return Math.sin(2 * Math.PI * position * frequency);
        }

        private final double frequency;
    }

    // Noise (for percussion, e.g.) N.B. independent of parameter
    public static class Noise extends Synth {
        Noise() { /* empty default constructor */ }

        public double getValue(double x) {
            return 2 * Math.random() - 1;
        }
    }

    // Brown noise - NB this is stateful
    public static class BrownNoise extends Synth {
        BrownNoise() { /* empty */ }

        private double current = 0;

        public double getValue(double x) {
            double offset = x * (2.0 * Math.random() - 1);
            current += offset;
            if (current > 1.0)
                current = 1.0;
            else if (current < -1.0)
                current = -1.0;
            return current;
        }
    }


    // g = (f1 + f2) / 2
    public static class Mix2 extends Synth {
        Mix2(Synth f1, Synth f2) {
            this.f1 = f1;
            this.f2 = f2;
        }

        public double getValue(double position) {
            return (f1.getValue(position) + f2.getValue(position)) / 2;
        }

        private final Synth f1, f2;
    }

    // g(x) = f(x*n)
    public static class Pitch extends Synth {
        Pitch(Synth f, double frequency) {
            this.f = f;
            this.frequency = frequency;
        }

        public double getValue(double position) {
            return (f.getValue(position * frequency));
        }

        private final Synth f;
        private final double frequency;
    }

    // g = 0 (for silence, e.g.)
    public static class Zero extends Synth {
        Zero() { /* empty default constructor */ }

        public double getValue(double position) {
            return 0;
        }
    }

    public static class Offset extends Synth {
        Offset(double value) {
            this.value = value;
        }

        public double getValue(double x) {
            return value;
        }

        private final double value;
    }

    // multiplicative mixer: h = fg (e.g. for envelopes)
    public static class MultiMix extends Synth {
        MultiMix(Synth f, Synth g) {
            this.f = f;
            this.g = g;
        }

        public double getValue(double position) {
            return f.getValue(position) * g.getValue(position);
        }

        private final Synth f, g;
    }

    // exponential (e.g. for decay)
    public static class Exponential extends Synth {
        Exponential(double e) {
            this.e = e;
        }

        public double getValue(double position) {
            return Math.pow(e, position);
        }

        private final double e;
    }

    // harmonics - specify amplitudes
    public static class Harmonic extends Synth {
        Harmonic(double[] amps) {
            this.amps = amps;
        }

        public double getValue(double x) {
            double c = 0;
            for (int i = 0; i < amps.length; i++) {
                double y = amps[i] * Math.sin(2 * Math.PI * x * (i + 1));
                c += y;
            }
            return c / amps.length;
        }

        private final double[] amps;
    }

    // composition: h = f o g
    public static class Composition extends Synth {
        Composition(Synth f, Synth g) {
            this.f = f;
            this.g = g;
        }

        public double getValue(double x) {
            return f.getValue(g.getValue(x));
        }

        private final Synth f, g;
    }

    // frequency function
    public static class FM extends Synth {
        FM(Synth s, Synth freq) {
            this.s = s;
            this.freq = freq;
            this.pitch = 1;
        }

        FM(FM orig, double pitch) {
            this.s = orig.s;
            this.freq = orig.freq;
            this.pitch = pitch;
            System.out.println("FM: s=" + s + ", freq=" + freq + ", pitch=" + pitch);
        }

        @Override
        public Synth getPitch(double pitch) {
            return new FM(this, pitch);
        }

        public double getValue(double x) {
            return s.getValue(x * pitch * freq.getValue(x));
        }

        private final Synth s;
        private final Synth freq;
        private final double pitch;
    }
}

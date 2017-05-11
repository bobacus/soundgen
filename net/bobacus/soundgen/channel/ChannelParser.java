package net.bobacus.soundgen.channel;

import java.util.ArrayList;

import net.bobacus.soundgen.synth.Sound;
import net.bobacus.soundgen.synth.Synth;


public class ChannelParser {
	// given a string, create a channel
	// The format is instrument,pitch,duration,instrument,pitch,duration etc.
	// pitch is in hertz
	// duration is in seconds
	// instrument is a number: 0 for Zero, 1 for Sinewave
	public static Channel createChannel(String s) {
		ArrayList<Sound> l = new ArrayList<Sound>();
		String[] r = s.split(",");
		int state = 0;
		int i=0;
		double p=0,d=0;
		for (String t : r) {
			switch (state) {
			case 0:
				i = Integer.parseInt(t);
				break;
			case 1:
				p = Double.parseDouble(t);
				break;
			case 2:
				d = Double.parseDouble(t);
				l.add(new Sound(mInstruments[i].getPitch(p),d));
				break;
			}
			state = (state+1) % 3;
		}
		
		return new Channel(l);
	}
	
	static double [] mPitches = { 392,415,440,466,494,523,554,587,622,659,698,740,784,831,880,932,988,1046 };
	static String pitchMap = "bhnjmq2w3er5t6y7ui";
	static public Synth [] mInstruments = {
		new Synth.Zero(),
		new Synth.Sine(),
		new Synth.Mix2(new Synth.MultiMix(new Synth.Exponential(0.97),new Synth.Sine(3)),new Synth.Sine()),
		new Synth.Noise()
	};

	
	public static Channel createChannel2(String s) {
		String[] r = s.split(",");
		int i = Integer.parseInt(r[0]);
		double d = Double.parseDouble(r[1]);
		char[] notes = r[2].toCharArray();
		ArrayList<Sound> l = new ArrayList<Sound>();
		for (char c : notes) {
			if (c=='.') {
				l.add(new Sound(mInstruments[0], d));
			} else {
				double pitch = mPitches[pitchMap.indexOf(c)];
				l.add(new Sound(mInstruments[i].getPitch(pitch), d));
			}
		}
		return new Channel(l);
	}
}

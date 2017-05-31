package net.bobacus.soundgen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.jatha.Jatha;
import org.jatha.dynatype.LispNumber;
import org.jatha.dynatype.LispValue;

import net.bobacus.fn.FilterIterator;
import net.bobacus.fn.Function;
import net.bobacus.fn.FunctionIterator;
import net.bobacus.fn.Predicate;
import net.bobacus.io.LineReader;
import net.bobacus.soundgen.channel.Channel;
import net.bobacus.soundgen.sampler.ChannelSampler;
import net.bobacus.soundgen.sampler.SamplerParams;
import net.bobacus.soundgen.synth.Instruments;
import net.bobacus.soundgen.synth.Sound;
import net.bobacus.soundgen.synth.Synth;
import net.bobacus.util.CachingFactory;

public class Music {

	public Music(File file) throws IOException {
		channels = getMusic(file);
	}
	
	private List<Channel> channels;
	
	public List<? extends ChannelSampler> getSamplers(SamplerParams params) {
		ArrayList<ChannelSampler> samplers = new ArrayList<ChannelSampler>();
		for (Channel c : channels) {
			samplers.add(new ChannelSampler(c, params));
		}
		return samplers;
	}
	
	public double getDuration() {
		if (channels.size()<1)
			return 0;
		double d = 0.0;
		for (Sound s : channels.get(0).getSounds()) {
			d += s.getDuration();
		}
		return d;
	}

	static CachingFactory<Sound> soundFactory = new CachingFactory<Sound>(Sound.class, new Class<?>[] {Synth.class, double.class});
	
	static List<Channel> getMusic(File file) throws IOException {
		LispValue result = evalMusicFile(file);

		ArrayList<Channel> channels = new ArrayList<Channel>();

		final Predicate<LispValue> testNotNull = new Predicate<LispValue>() {
			public boolean test(LispValue v) { return !v.basic_null(); }
		};
		Predicate<LispValue> testListP = new Predicate<LispValue>() {
			public boolean test(LispValue v) { return v.basic_listp(); }
		};

		final Function<LispValue,Sound> soundFromLisp = new Function<LispValue,Sound>() {
			public Sound eval(LispValue v) {
				int instr = (int)((LispNumber)v.first()).getLongValue();
				double pitch = ((LispNumber)v.second()).getDoubleValue();
				double durat = ((LispNumber)v.third()).getDoubleValue();
				return soundFactory.getInstance(Arrays.asList(new Object[]{Instruments.mInstruments[instr].getPitch(pitch),durat}));
			}
		};
		
		Function<LispValue,Channel> channelFromLisp = new Function<LispValue,Channel>() {
			public Channel eval(LispValue v) {
				Iterator<LispValue> iv = new FilterIterator<LispValue>(v.iterator(), testNotNull);
				Iterator<Sound> is = FunctionIterator.create(iv, soundFromLisp);
				return new Channel(is);
			}
		};
		
		Iterator<LispValue> iv = new FilterIterator<LispValue>(result.iterator(), testListP);
		Iterator<Channel> ic = FunctionIterator.create(iv, channelFromLisp);
		
		while (ic.hasNext())
			channels.add(ic.next());

		return channels;
	}

	static LispValue evalMusicFile(File file) throws FileNotFoundException {
		Jatha myLisp = new Jatha(false, false);
		myLisp.init();
		myLisp.start();
		LispValue result = null; 
		String sExpression = "";
		for (String l : new LineReader(file)) {
			if (l.equals(".")) {
				result = myLisp.eval(sExpression);
				sExpression = "";
			} else {
				sExpression += l; 
			}
		}
		if (result==null) 
			throw new SoundGenException("No Lisp evaluation happened");
		return result;
	}

}

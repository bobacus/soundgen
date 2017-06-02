package net.bobacus.soundgen;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jatha.Jatha;
import org.jatha.dynatype.LispNumber;
import org.jatha.dynatype.LispValue;

import net.bobacus.fn.FilterIterator;
import net.bobacus.fn.Function;
import net.bobacus.fn.FunctionIterator;
import net.bobacus.fn.Predicate;
import net.bobacus.soundgen.channel.Channel;
import net.bobacus.soundgen.sampler.ChannelSampler;
import net.bobacus.soundgen.sampler.SamplerParams;
import net.bobacus.soundgen.synth.Instruments;
import net.bobacus.soundgen.synth.Sound;

public class Music {

    public Music(File file) throws IOException {
        channels = getMusic(file);
    }

    private List<Channel> channels;

    List<? extends ChannelSampler> getSamplers(SamplerParams params) {
        ArrayList<ChannelSampler> samplers = new ArrayList<>();
        for (Channel c : channels) {
            samplers.add(new ChannelSampler(c, params));
        }
        return samplers;
    }

    double getDuration() {
        if (channels.size() < 1)
            return 0;
        double d = 0.0;
        for (Sound s : channels.get(0).getSounds()) {
            d += s.getDuration();
        }
        return d;
    }

    private static List<Channel> getMusic(File file) throws IOException {
        LispValue result = evalMusicFile(file);

        ArrayList<Channel> channels = new ArrayList<>();

        final Predicate<LispValue> testNotNull = v -> !v.basic_null();
        Predicate<LispValue> testListP = LispValue::basic_listp;

        final Function<LispValue, Sound> soundFromLisp = v -> {
            int instr = (int) ((LispNumber) v.first()).getLongValue();
            double pitch = ((LispNumber) v.second()).getDoubleValue();
            double duration = ((LispNumber) v.third()).getDoubleValue();
            return new Sound(Instruments.mInstruments[instr].getPitch(pitch), duration);
        };

        Function<LispValue, Channel> channelFromLisp = v -> {
            Iterator<LispValue> iv = new FilterIterator<>(v.iterator(), testNotNull);
            Iterator<Sound> is = FunctionIterator.create(iv, soundFromLisp);
            return new Channel(is);
        };

        Iterator<LispValue> iv = new FilterIterator<>(result.iterator(), testListP);
        Iterator<Channel> ic = FunctionIterator.create(iv, channelFromLisp);

        while (ic.hasNext())
            channels.add(ic.next());

        return channels;
    }

    private static LispValue evalMusicFile(File file) throws IOException {
        Jatha myLisp = new Jatha(false, false);
        myLisp.init();
        myLisp.start();
        LispValue result = null;
        StringBuilder sExpression = new StringBuilder();
        for (String l : Files.readAllLines(file.toPath())) {
            if (l.equals(".")) {
                result = myLisp.eval(sExpression.toString());
                sExpression = new StringBuilder();
            } else {
                sExpression.append(l);
            }
        }
        if (result == null) {
            throw new SoundGenException("No Lisp evaluation happened");
        }
        return result;
    }

}

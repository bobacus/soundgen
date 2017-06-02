package net.bobacus.soundgen.channel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.bobacus.soundgen.synth.Sound;

/**
 * A Channel is a sequence of Sounds
 */
public class Channel {

    public Channel(Iterator<Sound> i) {
        mSounds = new ArrayList<>();
        while (i.hasNext())
            mSounds.add(i.next());
    }


    private final ArrayList<Sound> mSounds;

    public List<Sound> getSounds() {
        return mSounds;
    }
}


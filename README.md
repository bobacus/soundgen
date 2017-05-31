# Overview

SoundGen is a simple synthesiser and sequencer written in Java. It allows you
to write music in a subset of Common Lisp.

To compile it, you need Java (JDK 1.5 or later) and Maven.

e.g. try this command:

```mvn test exec:java -Dexec.mainClass=net.bobacus.soundgen.SoundGen -Dexec.args=tune1.txt```

This will compile, run any unit tests, and if successful, will execute the main SoundGen program, 
passing in `tune1.txt` to play.

It's essentially undocumented, but the basic idea is that the final s-expression in
the tune file should return a list of channels, each of which is a list of sounds.
Each sound is a triple (instrument, pitch, duration). The channels are played
in parallel. See [Music.java](src/main/java/net/bobacus/soundgen/Music.java) for the actual detail.

Instruments are defined in [Instruments.java](src/main/java/net/bobacus/soundgen/synth/Instruments.java), 
with the `mInstruments` array implying the instrument numbers used in the Lisp 
output structure.

# History

* 2006: Initial Java 5 version, using Jatha 2.7.1
* 2017: Project resurrected, put onto github

# Licence and Copyright

SoundGen is copyright Robert Adamson.

SoundGen incorporates and uses the [Jatha](http://jatha.sourceforge.net/) library, copyright (C) 1997-2003 Micheal 
Scott Hewett, Palo Alto, CA. The distribution and use of Jatha is 
[licensed](license/jatha-license.txt) under the terms of the 
[GNU Lesser GPL v2.1](license/lgpl.txt)

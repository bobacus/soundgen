package net.bobacus.soundgen.sampler;

public class Sample {

	public static byte getBigByte(double value) {
		int i = (int)(32767*value);
		return (byte)(i>>8);
	}

	// returns value between -127 and 127
	public static byte getByte(double value) {
		return (byte)(127*value);
	}

	// returns little and big bytes of value converted to 16 bits signed
	public static byte getLittleByte(double value) {
		int i = (int)(32767*value);
		return (byte)(i & 0xFF);
	}

}

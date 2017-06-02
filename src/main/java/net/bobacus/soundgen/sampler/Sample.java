package net.bobacus.soundgen.sampler;

/**
 * Methods to return little and big bytes of value converted to 16 bits signed
 */
public class Sample {

	public static byte getBigByte(double value) {
		int i = (int)(32767*value);
		return (byte)(i>>8);
	}

	public static byte getLittleByte(double value) {
		int i = (int)(32767*value);
		return (byte)(i & 0xFF);
	}

}

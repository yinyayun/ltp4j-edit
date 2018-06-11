package edu.hit.ir.ltp4j;

import java.util.List;

import org.yinyayun.env.LTPNativeLibrary;

public class Segmentor {
	static {
		LTPNativeLibrary.load();
	}

	public final native int create(String modelPath);

	public final native int create(String modelPath, String lexiconPath);

	public final native int segment(String sent, List<String> words);

	public final native void release();

	public final static native int loaded();
}

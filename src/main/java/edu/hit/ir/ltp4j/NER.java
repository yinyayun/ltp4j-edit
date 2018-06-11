package edu.hit.ir.ltp4j;

import java.util.List;

import org.yinyayun.env.LTPNativeLibrary;

public class NER {

	static {
		LTPNativeLibrary.load();
	}

	public final native int create(String modelPath);

	public final native int recognize(List<String> words, List<String> postags, List<String> ners);

	public final native void release();

	public final static native int loaded();
}

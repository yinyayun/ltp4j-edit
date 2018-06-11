package edu.hit.ir.ltp4j;

import java.util.List;

import org.yinyayun.env.LTPNativeLibrary;

public class Parser {
	static {
		LTPNativeLibrary.load();
	}

	public final native int create(String modelPath);

	public final native int parse(List<String> words, List<String> tags, List<Integer> heads, List<String> deprels);

	public final native void release();

	public final static native int loaded();
}

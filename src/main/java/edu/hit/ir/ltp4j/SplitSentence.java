package edu.hit.ir.ltp4j;

import java.util.List;

import org.yinyayun.env.LTPNativeLibrary;

public class SplitSentence {
	static {
		LTPNativeLibrary.load();
	}

	public final native void splitSentence(String sent, List<String> sents);
}

package org.yinyayun.nlp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.yinyayun.nlp.common.LTPContext;

import edu.hit.ir.ltp4j.Postagger;

/**
 * 词性识别
 * 
 * @author yinyayun
 *
 */
public class LTPPostagger extends LTPBaseModel {
	private Postagger postagger;

	public LTPPostagger() {
		postagger = new Postagger();
	}

	public void doAction(LTPContext context) {
		if (context.getWords() == null) {
			throw new RuntimeException("please run TOKEN task first!");
		}
		List<String> tags = new ArrayList<String>();
		postagger.postag(context.getWords(), tags);
		context.setTags(tags);
	}

	@Override
	public void loadModel(File modelFile) {
		if (Postagger.loaded() == -1)
			postagger.create(modelFile.getAbsolutePath());
	}

	@Override
	public String getModelName() {
		return "model/pos.zip";
	}

	@Override
	public boolean isloaded() {
		try {
			return Postagger.loaded() == 1;
		} catch (UnsatisfiedLinkError e) {
			return false;
		}
	}

}
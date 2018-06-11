package org.yinyayun.nlp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.yinyayun.nlp.common.LTPContext;

import edu.hit.ir.ltp4j.NER;

/**
 * 命名实体识别
 * 
 * @author yinyayun
 *
 */
public class LTPNER extends LTPBaseModel {
	private NER ner;

	public LTPNER() {
		this.ner = new NER();
	}

	public void doAction(LTPContext context) {
		if (context.getWords() == null || context.getTags() == null) {
			throw new RuntimeException("please run TOKEN and POS task first!");
		}
		List<String> ners = new ArrayList<String>();
		ner.recognize(context.getWords(), context.getTags(), ners);
		context.setNers(ners);
	}

	@Override
	public void loadModel(File modelFile) {
		if (NER.loaded() == -1) {
			ner.create(modelFile.getAbsolutePath());
		}
	}

	@Override
	public String getModelName() {
		return "model/ner.zip";
	}

	@Override
	public boolean isloaded() {
		try {
			return NER.loaded() == 1;
		} catch (UnsatisfiedLinkError e) {
			return false;
		}
	}

}

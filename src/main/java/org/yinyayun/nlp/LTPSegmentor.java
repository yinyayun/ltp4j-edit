package org.yinyayun.nlp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.yinyayun.nlp.common.LTPContext;

import edu.hit.ir.ltp4j.Segmentor;

/**
 * 分词
 * 
 * @author yinyayun
 *
 */
public class LTPSegmentor extends LTPBaseModel {
	private Segmentor segmentor;

	public LTPSegmentor() {
		segmentor = new Segmentor();
	}

	public void doAction(LTPContext context) {
		List<String> tokens = new ArrayList<String>();
		segmentor.segment(context.getText(), tokens);
		context.setWords(tokens);
	}

	@Override
	public void loadModel(File modelFile) {
		if (Segmentor.loaded() == -1)
			segmentor.create(modelFile.getAbsolutePath());
	}

	@Override
	public String getModelName() {
		return "model/cws.zip";
	}

	@Override
	public boolean isloaded() {
		try {
			return Segmentor.loaded() == 1;
		} catch (UnsatisfiedLinkError e) {
			return false;
		}
	}

}
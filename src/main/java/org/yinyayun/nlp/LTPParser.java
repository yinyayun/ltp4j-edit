package org.yinyayun.nlp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.yinyayun.nlp.common.LTPContext;

import edu.hit.ir.ltp4j.Parser;

/**
 * 句法分析
 * 
 * @author yinyayun
 *
 */
public class LTPParser extends LTPBaseModel {
	private Parser parser;

	public LTPParser() {
		this.parser = new Parser();
	}

	public void doAction(LTPContext context) {
		if (context.getWords() == null || context.getTags() == null) {
			throw new RuntimeException("please run TOKEN and POS task first!");
		}
		List<Integer> heads = new ArrayList<Integer>();
		List<String> deprels = new ArrayList<String>();
		parser.parse(context.getWords(), context.getTags(), heads, deprels);
		context.setHeads(heads);
		context.setDeprels(deprels);
	}

	@Override
	public void loadModel(File modelFile) {
		if (Parser.loaded() == -1)
			parser.create(modelFile.getAbsolutePath());
	}

	@Override
	public String getModelName() {
		return "model/parser.zip";
	}

	@Override
	public boolean isloaded() {
		try {
			return Parser.loaded() == 1;
		} catch (UnsatisfiedLinkError e) {
			return false;
		}
	}
}

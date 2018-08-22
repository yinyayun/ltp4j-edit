package org.yinyayun.nlp.user;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yinyayun.nlp.common.LTPContext;

/**
 * 同义词转换
 * 
 * @author yinyayun
 *
 */
public class Lemmatzation extends UserModel {
	private Map<String, String> synonymsDict = new HashMap<String, String>();

	public Lemmatzation() {
	}

	public Lemmatzation(String modelFilePath) {
		super(modelFilePath);
	}

	@Override
	public void doAction(LTPContext context) {
		List<String> words = context.getWords();
		if (words == null) {
			throw new RuntimeException("you must give a tokenizer before lemma!");
		}
		List<String> lemmas = new ArrayList<String>(words.size());
		for (String word : words) {
			if (synonymsDict.containsKey(word)) {
				lemmas.add(synonymsDict.get(word));
			} else {
				lemmas.add("NA");
			}
		}
		context.setLemmas(lemmas);
	}

	@Override
	public void loadModel(File modelFile) {
		try {
			List<String> lines = Files.readAllLines(modelFile.toPath());
			for (String x : lines) {
				if (x != null && x.length() > 0 && !x.startsWith("#")) {
					String[] part = x.split("\\/");
					synonymsDict.put(part[0].trim(), part[1].trim());
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(String.format("load %s error!", modelFile.getName()), e);
		}
	}

	@Override
	public void load(String word, String other) {
		synonymsDict.put(word, other);
	}

}

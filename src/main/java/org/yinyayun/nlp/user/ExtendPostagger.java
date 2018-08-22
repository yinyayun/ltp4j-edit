package org.yinyayun.nlp.user;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yinyayun.nlp.common.LTPContext;

/**
 * 基于用户自定义词典进行词性更正
 * 
 * @author yinyayun
 *
 */
public class ExtendPostagger extends UserModel {
	private Map<String, String> dict = new HashMap<String, String>();

	@Override
	public void doAction(LTPContext context) {
		List<String> words = context.getWords();
		List<String> tags = context.getTags();
		if (tags == null) {
			throw new RuntimeException("this task must after postagger task!");
		}
		for (int i = 0; i < words.size(); i++) {
			String tag = dict.get(words.get(i));
			if (tag != null && tag.length() > 0) {
				tags.set(i, tag);
			}
		}
	}

	@Override
	public void loadModel(File modelFile) {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(modelFile), "utf-8"),
				1024)) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("#") || line.trim().length() == 0) {
					continue;
				}
				String[] parts = line.split("/");
				if (parts.length == 2) {
					dict.put(parts[0].trim().toLowerCase(), parts[1].trim());
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@Override
	public void load(String word, String other) {
		dict.put(word, other);
	}

}

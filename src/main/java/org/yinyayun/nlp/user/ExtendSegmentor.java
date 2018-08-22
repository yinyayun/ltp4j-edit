package org.yinyayun.nlp.user;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.yinyayun.nlp.common.LTPContext;

/**
 * 使用用户自定义词典进行分词
 * 
 * @author yinyayun
 *
 */
public class ExtendSegmentor extends UserModel {
	private Set<String> dict = new HashSet<String>();

	@Override
	public void doAction(LTPContext context) {
		List<String> tokens = context.getWords();
		if (tokens == null || context.getTags() != null) {
			throw new RuntimeException("this task must after token task before postagger task!");
		}
		List<String> thins = new ArrayList<String>();
		// 单次逆向拆分
		for (String token : tokens) {
			boolean find = false;
			if (!dict.contains(token)) {
				for (int index = Math.min(token.length() - 1, 4); index > 0; index--) {
					String prefix = token.substring(0, index);
					String suffix = token.substring(index);
					if (dict.contains(prefix) || dict.contains(suffix)) {
						thins.add(prefix);
						thins.add(suffix);
						find = true;
						break;
					}
				}
			}
			if (!find) {
				thins.add(token);
			}
		}
		// 单次正向扫描合并
		StringBuilder builder = new StringBuilder();
		List<String> thicks = new ArrayList<String>();
		for (int index = 0; index <= thins.size(); index++) {
			int end = Math.min(index + 3, thins.size());
			for (; end > index; end--) {
				combine(builder, thins, index, end);
				String combine = builder.toString();
				if (dict.contains(combine)) {
					thicks.add(combine);
					index = end - 1;
					break;
				} else if (end == index + 1) {
					thicks.add(thins.get(index));
				}
			}
		}
		context.setWords(thicks);

	}

	private void combine(StringBuilder builder, List<String> words, int start, int end) {
		builder.setLength(0);
		for (int i = start; i < end; i++) {
			builder.append(words.get(i));
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
				dict.add(parts[0].trim().toLowerCase());
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@Override
	public void load(String word, String other) {
		dict.add(word);
	}

}

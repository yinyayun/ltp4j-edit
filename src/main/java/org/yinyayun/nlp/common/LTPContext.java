package org.yinyayun.nlp.common;

import java.util.List;

public class LTPContext {
	private String text;
	private List<String> words;
	private List<String> lemmas;
	private List<String> tags;
	private List<String> ners;

	private List<Integer> heads;
	private List<String> deprels;

	public LTPContext(String text) {
		this.text = text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setWords(List<String> words) {
		this.words = words;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public void setNers(List<String> ners) {
		this.ners = ners;
	}

	public void setHeads(List<Integer> heads) {
		this.heads = heads;
	}

	public void setDeprels(List<String> deprels) {
		this.deprels = deprels;
	}

	public String getText() {
		return text;
	}

	public List<String> getWords() {
		return words;
	}

	public List<String> getTags() {
		return tags;
	}

	public List<String> getNers() {
		return ners;
	}

	public List<Integer> getHeads() {
		return heads;
	}

	public List<String> getDeprels() {
		return deprels;
	}

	public List<String> getLemmas() {
		return lemmas;
	}

	public void setLemmas(List<String> lemmas) {
		this.lemmas = lemmas;
	}
}

package org.yinyayun.nlp.common;

import java.util.ArrayList;
import java.util.List;

public class LTPResult {
	private List<LTPTerm> terms = new ArrayList<LTPTerm>(5);
	private List<Integer> heads;
	private List<String> deprels;

	public void addTerm(String word, String tag, String lemma) {
		terms.add(new LTPTerm(word, tag, lemma));
	}

	public void setHeads(List<Integer> heads) {
		this.heads = heads;
	}

	public void setDeprels(List<String> deprels) {
		this.deprels = deprels;
	}

	public List<LTPTerm> getTerms() {
		return terms;
	}

	public List<Integer> getHeads() {
		return heads;
	}

	public List<String> getDeprels() {
		return deprels;
	}

}

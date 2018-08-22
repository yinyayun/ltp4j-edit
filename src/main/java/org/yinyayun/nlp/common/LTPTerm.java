package org.yinyayun.nlp.common;

/**
 * 词条属性封装
 * 
 * @author yinyayun
 *
 */
public class LTPTerm {
	public String word;
	public String tag;
	public String lemma;

	public LTPTerm(String word, String tag, String lemma) {
		super();
		this.word = word;
		this.tag = tag;
		this.lemma = lemma;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(word).append("/");
		if (tag != null)
			builder.append(tag);
		else
			builder.append("NA");
		builder.append("/");
		if (lemma != null)
			builder.append(lemma);
		else
			builder.append("NA");
		return builder.toString();
	}

}

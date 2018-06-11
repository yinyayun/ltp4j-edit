package org.yinyayun.nlp.common;

import org.yinyayun.nlp.LTPBaseModel;
import org.yinyayun.nlp.LTPSegmentor;
import org.yinyayun.nlp.LTPPostagger;
import org.yinyayun.nlp.LTPNER;
import org.yinyayun.nlp.LTPParser;

public enum LTP_TASK {
	TOKEN(1, "TOKEN", LTPSegmentor.class), POS(2, "POS", LTPPostagger.class), NER(3, "NER", LTPNER.class), PARSER(4,
			"PARSER", LTPParser.class);

	public int value;
	public String name;
	private Class<? extends LTPBaseModel> clazz;

	private LTP_TASK(int init, String name, Class<? extends LTPBaseModel> clazz) {
		this.value = 1 << init;
		this.name = name;
		this.clazz = clazz;
	}

	public Class<? extends LTPBaseModel> clazz() {
		return clazz;
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}
}

package org.yinyayun.nlp.common;

import org.yinyayun.nlp.LTPBaseModel;
import org.yinyayun.nlp.LTPSegmentor;
import org.yinyayun.nlp.LTPPostagger;
import org.yinyayun.nlp.LTPParser;
import org.yinyayun.nlp.user.Lemmatzation;
import org.yinyayun.nlp.user.*;

public enum LTP_TASK {
	TOKEN(1, "TOKEN", LTPSegmentor.class), //
	USER_TOKEN(2, "USER_TOKEN", ExtendSegmentor.class), //
	LEMMA(3, "LEMMA", Lemmatzation.class), //
	POS(4, "POS", LTPPostagger.class), //
	USER_POS(5, "USER_POS", ExtendPostagger.class), //
	PARSER(6, "PARSER", LTPParser.class);

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

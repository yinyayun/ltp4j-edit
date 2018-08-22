package org.yinyayun.nlp.common;

import java.util.HashMap;
import java.util.Map;

/**
 * LTP使用的标签注释
 * 
 * @author yinyayun
 *
 */
public class LTPHelper {
	public final static String[][] POS_TAGS = { { "a", "adjective", "美丽" }, { "ni", "organization name", "保险公司" },
			{ "b", "other noun-modifier", "大型, 西式" }, { "nl", "location noun", "城郊" }, { "c", "conjunction", "和, 虽然" },
			{ "ns", "geographical name", "北京" }, { "d", "adverb", "很" }, { "nt", "temporal noun", "近日, 明代" },
			{ "e", "exclamation", "哎" }, { "nz", "other proper noun", "诺贝尔奖" }, { "g", "morpheme", "茨, 甥" },
			{ "o", "onomatopoeia", "哗啦" }, { "h", "prefix", "阿, 伪" }, { "p", "preposition", "在, 把" },
			{ "i", "idiom", "百花齐放" }, { "q", "quantity", "个" }, { "j", "abbreviation", "公检法" },
			{ "r", "pronoun", "我们" }, { "k", "suffix", "界, 率" }, { "u", "auxiliary", "的, 地" },
			{ "m", "number", "一, 第一" }, { "v", "verb", "跑, 学习" }, { "n", "general noun", "苹果" },
			{ "wp", "punctuation", "，。！" }, { "nd", "direction noun", "右侧" }, { "ws", "foreign words", "CPU" },
			{ "nh", "person name", "杜甫, 汤姆" }, { "x", "non-lexeme", "萄, 翱" } };

	public final static String[][] PARSERS = { { "主谓关系", "SBV", "subject-verb", "我送她一束花 (我 <– 送)" },
			{ "动宾关系", "VOB", "直接宾语，verb-object", "我送她一束花 (送 –> 花)" },
			{ "间宾关系", "IOB", "间接宾语，indirect-object", "我送她一束花 (送 –> 她)" },
			{ "前置宾语", "FOB", "前置宾语，fronting-object", "他什么书都读 (书 <– 读)" }, { "兼语", "DBL", "double", "他请我吃饭 (请 –> 我)" },
			{ "定中关系", "ATT", "attribute", "红苹果 (红 <– 苹果)" }, { "状中结构", "ADV", "adverbial", "非常美丽 (非常 <– 美丽)" },
			{ "动补结构", "CMP", "complement", "做完了作业 (做 –> 完)" }, { "并列关系", "COO", "coordinate", "大山和大海 (大山 –> 大海)" },
			{ "介宾关系", "POB", "preposition-object", "在贸易区内 (在 –> 内)" },
			{ "左附加关系", "LAD", "left adjunct", "大山和大海 (和 <– 大海)" }, { "右附加关系", "RAD", "right adjunct", "孩子们 (孩子 –> 们)" },
			{ "独立结构", "IS", "independent structure", "两个单句在结构上彼此独立" }, { "核心关系", "HED", "head", "指整个句子的核心" }, };

	public final static String[] UNIMPORTANCES = { "lm", "c", "e", "g", "h", "i", "k", "o", "p", "u", "wp", "x" };

	private static Map<String, DesAndExample> POSMAP = new HashMap<String, DesAndExample>();
	private static Map<String, String> NERMAP = new HashMap<String, String>();

	static {
		for (String[] tag : POS_TAGS) {
			POSMAP.put(tag[0], new DesAndExample(tag[1], tag[2]));
		}
		NERMAP.put("Nh", "人名");
		NERMAP.put("Ni", "机构名");
		NERMAP.put("Ns", "地名");
	}

	public static String posDesc(String tag) {
		return POSMAP.get(tag).toString();
	}

	public static String nerDesc(String ner) {
		return NERMAP.get(ner);
	}

	/**
	 * 名词，包含通常理解上的，位置名词，以及方位名词
	 * 
	 * @param tag
	 * @return
	 */
	public static boolean isNoun(String tag) {
		if ("n".equals(tag) || "nd".equals(tag) || "nl".equals(tag) || "j".equals(tag))
			return true;
		return false;
	}

	/**
	 * 一般名词，即通常理解上的名词
	 * 
	 * @param tag
	 * @return
	 */
	public static boolean isGeneralNoun(String tag) {
		if ("ws".equals(tag))
			return true;
		return false;
	}

	/**
	 * 是否为动词
	 * 
	 * @param tag
	 * @return
	 */
	public static boolean isV(String tag) {
		if ("v".equals(tag))
			return true;
		return false;
	}

	public static boolean isUnImportant(String tag) {
		for (String t : UNIMPORTANCES) {
			if (t.equals(tag))
				return true;
		}
		return false;
	}

	/**
	 * 是否为专有名词
	 * 
	 * @param tag
	 * @return
	 */
	public static boolean isProperNoun(String tag) {
		if ("ni".equals(tag) || "ns".equals(tag) || "nt".equals(tag) || "nz".equals(tag))
			return true;
		return false;
	}

	/**
	 * 是否为地名
	 * 
	 * @param tag
	 * @return
	 */
	public static boolean isGeoName(String tag) {
		if ("ns".equals(tag))
			return true;
		return false;
	}

	/**
	 * 是否为人名
	 * 
	 * @param tag
	 * @return
	 */
	public static boolean isPersonName(String tag) {
		if ("nh".equals(tag))
			return true;
		return false;
	}

	/**
	 * 是否为标点符号
	 * 
	 * @param tag
	 * @return
	 */
	public static boolean isPun(String tag) {
		if ("wp".equals(tag))
			return true;
		return false;
	}

	/**
	 * 是否为人名
	 *
	 * @return
	 */
	public boolean isForeignWords(String tag) {
		if ("ws".equals(tag))
			return true;
		return false;
	}

	static class DesAndExample {
		String desc;
		String example;

		public DesAndExample(String desc, String example) {
			this.desc = desc;
			this.example = example;
		}

		@Override
		public String toString() {
			return "DESC:[" + desc + "],Example:[" + example + "]";
		}
	}
}

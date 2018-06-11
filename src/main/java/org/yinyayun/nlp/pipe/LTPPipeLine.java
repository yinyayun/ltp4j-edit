package org.yinyayun.nlp.pipe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yinyayun.nlp.LTPBaseModel;
import org.yinyayun.nlp.common.LTPContext;
import org.yinyayun.nlp.common.LTPResult;
import org.yinyayun.nlp.common.LTP_TASK;

/**
 * 按照管道的方式对词法分析、句法分析任务进行处理
 * 
 * @author yinyayun
 *
 */
public class LTPPipeLine {
	private LTP_TASK[] nlps;
	private Map<LTP_TASK, LTPBaseModel> models;

	public LTPPipeLine() {
		this(LTP_TASK.TOKEN, LTP_TASK.POS, LTP_TASK.NER);
	}

	public LTPPipeLine(LTP_TASK... nlps) {
		if (nlps == null || !nlps[0].equals(LTP_TASK.TOKEN)) {
			throw new RuntimeException("tokenizer task must be first step!");
		}
		try {
			this.nlps = nlps;
			this.models = new HashMap<LTP_TASK, LTPBaseModel>();
			ClassLoader classLoader = LTPPipeLine.class.getClassLoader();
			for (LTP_TASK task : nlps) {
				LTPBaseModel model = (LTPBaseModel) classLoader.loadClass(task.clazz().getName()).newInstance();
				model.loadModel();
				models.put(task, model);
			}
		} catch (Exception e) {
			throw new RuntimeException("load fnlp models has error!", e);
		}
	}

	public LTPResult parser(String text) {
		LTPContext context = new LTPContext(text);
		for (LTP_TASK task : nlps) {
			models.get(task).doAction(context);
		}
		List<String> tokens = context.getWords();
		List<String> tags = context.getTags();
		List<String> ners = context.getNers();
		List<Integer> heads = context.getHeads();
		List<String> deprels = context.getDeprels();

		LTPResult result = new LTPResult();
		for (int i = 0; i < tokens.size(); i++) {
			result.addTerm(tokens.get(i), tags == null ? "NA" : tags.get(i), ners == null ? "NA" : ners.get(i), null);
		}
		result.setHeads(heads);
		result.setDeprels(deprels);
		return result;
	}
}

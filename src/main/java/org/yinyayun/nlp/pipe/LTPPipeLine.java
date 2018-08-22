package org.yinyayun.nlp.pipe;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.yinyayun.nlp.LTPBaseModel;
import org.yinyayun.nlp.common.LTPContext;
import org.yinyayun.nlp.common.LTPResult;
import org.yinyayun.nlp.common.LTP_TASK;
import org.yinyayun.nlp.user.ExtendPostagger;
import org.yinyayun.nlp.user.ExtendSegmentor;
import org.yinyayun.nlp.user.Lemmatzation;

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
		this(LTP_TASK.TOKEN, LTP_TASK.USER_TOKEN, LTP_TASK.LEMMA, LTP_TASK.POS, LTP_TASK.USER_POS);
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

	/**
	 * 加载用户自定义同义词典
	 * 
	 * @param userDefineSynonyms
	 * @return true 加载成功
	 */
	public boolean loadSynonymsDict(Map<String, String> synonyms) {
		Lemmatzation baseModel = (Lemmatzation) models.get(LTP_TASK.LEMMA);
		if (baseModel != null) {
			for (Entry<String, String> entrys : synonyms.entrySet()) {
				baseModel.load(entrys.getKey(), entrys.getValue());
			}
			return true;
		}
		return false;
	}

	/**
	 * 从classpath路径查找词典进行同义加载
	 * 
	 * @param userDefineSynonyms
	 * @return true 加载成功
	 */
	public boolean loadSynonymsDict(String sourceFileInclassPath) throws IOException {
		Lemmatzation baseModel = (Lemmatzation) models.get(LTP_TASK.LEMMA);
		if (baseModel != null) {
			baseModel.loadModel(sourceFileInclassPath);
			return true;
		}
		return false;
	}

	/**
	 * 从classpath路径查找词典进行同义加载
	 * 
	 * @param userDefineSynonyms
	 * @return true 加载成功
	 */
	public boolean loadSynonymsDict(String modelAlias, InputStream inputStream) throws IOException {
		Lemmatzation baseModel = (Lemmatzation) models.get(LTP_TASK.LEMMA);
		if (baseModel != null) {
			baseModel.loadModel(modelAlias, inputStream);
			return true;
		}
		return false;
	}

	/**
	 * 加载扩展词典
	 * 
	 * @param wordTags
	 */
	public void loadExtend(Map<String, String> wordTags) {
		ExtendSegmentor extendSegmentor = (ExtendSegmentor) models.get(LTP_TASK.USER_TOKEN);
		ExtendPostagger extendPostagger = (ExtendPostagger) models.get(LTP_TASK.USER_POS);
		for (Entry<String, String> entry : wordTags.entrySet()) {
			if (extendSegmentor != null)
				extendSegmentor.load(entry.getKey(), entry.getValue());
			if (extendPostagger != null)
				extendPostagger.load(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * 从class路径查找扩展词典并加载
	 * 
	 * @param wordTags
	 */
	public void loadExtend(String sourceFileInclassPath) throws IOException {
		ExtendSegmentor extendSegmentor = (ExtendSegmentor) models.get(LTP_TASK.USER_TOKEN);
		ExtendPostagger extendPostagger = (ExtendPostagger) models.get(LTP_TASK.USER_POS);
		if (extendSegmentor != null)
			extendSegmentor.loadModel(sourceFileInclassPath);
		if (extendPostagger != null)
			extendPostagger.loadModel(sourceFileInclassPath);
	}

	/**
	 * 直接从流加载
	 * 
	 * @param wordTags
	 */
	public void loadExtend(String modelAlias, InputStream inputStream) throws IOException {
		ExtendSegmentor extendSegmentor = (ExtendSegmentor) models.get(LTP_TASK.USER_TOKEN);
		ExtendPostagger extendPostagger = (ExtendPostagger) models.get(LTP_TASK.USER_POS);
		if (extendSegmentor != null)
			extendSegmentor.loadModel(modelAlias, inputStream);
		if (extendPostagger != null)
			extendPostagger.loadModel(modelAlias, inputStream);
	}

	public LTPResult parser(String text) {
		LTPContext context = new LTPContext(text);
		for (LTP_TASK task : nlps) {
			models.get(task).doAction(context);
		}
		List<String> tokens = context.getWords();
		List<String> lemmas = context.getLemmas();
		List<String> tags = context.getTags();
		List<Integer> heads = context.getHeads();
		List<String> deprels = context.getDeprels();

		LTPResult result = new LTPResult();
		for (int i = 0; i < tokens.size(); i++) {
			result.addTerm(tokens.get(i), tags == null ? "NA" : tags.get(i), lemmas == null ? "NA" : lemmas.get(i));
		}
		result.setHeads(heads);
		result.setDeprels(deprels);
		return result;
	}
}

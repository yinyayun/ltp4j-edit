package edu.hit.ir.ltp4j;

import org.yinyayun.nlp.common.LTPResult;
import org.yinyayun.nlp.common.LTP_TASK;
import org.yinyayun.nlp.pipe.LTPPipeLine;

public class LtpTest {
	public static void main(String[] args) {
		LTPPipeLine defaultPipe = new LTPPipeLine();
		LTPResult result = defaultPipe.parser("我的邮箱坏了，找谁处理");
		System.out.println(result.getTerms());
		LTPPipeLine ltpPipeLine = new LTPPipeLine(LTP_TASK.TOKEN, LTP_TASK.POS);
		System.out.println(ltpPipeLine.parser("我的邮箱坏了，找谁处理").getTerms());
		Segmentor.loaded();
	}
}

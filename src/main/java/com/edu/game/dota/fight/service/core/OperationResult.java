package com.eyu.snm.module.fight.service.core;

import com.eyu.snm.module.fight.model.report.ActionReport;
import com.eyu.snm.module.fight.service.op.Operation;

/**
 * 行动结果对象
 * 
 * @author Frank
 */
public class OperationResult {

	/** 构造方法 */
	public static OperationResult valueOf(ActionReport report, Operation next) {
		OperationResult ret = new OperationResult();
		ret.report = report;
		ret.next = next;
		return ret;
	}

	/** 行动战报 */
	private ActionReport report;
	/** 后续行动 */
	private Operation next;
	
	// Getter and Setter ...

	public ActionReport getReport() {
		return report;
	}

	public void setReport(ActionReport report) {
		this.report = report;
	}

	public Operation getNext() {
		return next;
	}

	public void setNext(Operation next) {
		this.next = next;
	}

}

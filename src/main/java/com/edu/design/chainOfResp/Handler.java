package com.edu.design.chainOfResp;

/**
 * 抽象处理者角色ß
 * @author zuohuai
 *
 */
public abstract class Handler {
	/**持有后续处理者引用*/
	private Handler successor;
	/**
	 * 实际处理的业务请求
	 */
	public abstract void handleRequest();
	
	public Handler getSuccessor() {
		return successor;
	}
	
	
}

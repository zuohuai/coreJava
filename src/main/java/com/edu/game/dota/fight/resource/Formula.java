package com.edu.game.dota.fight.resource;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.mvel2.MVEL;
import org.mvel2.ParserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edu.game.resource.anno.Id;
import com.edu.game.resource.anno.Resource;

/**
 * 公式对象
 * @author frank
 */
@Resource("dota")
public class Formula {

	private static final Logger logger = LoggerFactory.getLogger(Formula.class);

	private static final ParserContext context = new ParserContext();
	static {
		/** 导入 {@link Math} 中的全部静态方法 */
		context.addImport(Math.class);
		for (Method method : Math.class.getMethods()) {
			int mod = method.getModifiers();
			if (Modifier.isStatic(mod) && Modifier.isPublic(mod)) {
				String name = method.getName();
				context.addImport(name, method);
			}
		}
	}

	/** 唯一标识 */
	@Id
	private String id;
	/** 公式内容 */
	private String content;
	/** 结果类型 */
	private Class<?> returnClz;
	/** 上下文类型 */
	private Class<?> ctxClz;

	public Formula() {
	}

	public Formula(String content, Class<?> returnClz) {
		this.content = content;
		this.returnClz = returnClz;
	}

	/** 编译后的公式 */
	private volatile Serializable exp;

	/**
	 * 根据上下文计算表达式值
	 * @param ctx 上下文对象
	 * @return
	 */
	public Object calculate(Object ctx) {
		try {
			if (exp == null) {
				synchronized (this) {
					if (exp == null) {
						exp = MVEL.compileExpression(content, context);
					}
				}
			}
			Object result = MVEL.executeExpression(exp, ctx, returnClz);
			if (logger.isDebugEnabled()) {
				logger.debug("公式[{}] 内容[{}] 结果[{}]", new Object[] { id, content, result });
			}
			return result;
		} catch (RuntimeException e) {
			logger.error("公式[{}] 内容[{}] 执行错误", new Object[] { id, content, e });
			throw e;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map toMap(Object... keyValues) {
		Map result = new HashMap();
		for (int i = 0; i < keyValues.length; i = i + 2) {
			result.put(keyValues[i], keyValues[i + 1]);
		}
		return result;
	}

	// Getter ...

	public String getId() {
		return id;
	}

	public String getContent() {
		return content;
	}

	public Class<?> getReturnClz() {
		return returnClz;
	}

	public Class<?> getCtxClz() {
		return ctxClz;
	}

}

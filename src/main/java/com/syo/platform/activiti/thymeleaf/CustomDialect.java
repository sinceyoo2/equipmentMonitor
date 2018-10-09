package com.syo.platform.activiti.thymeleaf;

import java.util.HashSet;
import java.util.Set;

import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;

public class CustomDialect extends AbstractProcessorDialect {
	
	private static final String DIALECT_NAME = "Act Form Dialect";
    private static final String PREFIX = "act";
    public static final int PROCESSOR_PRECEDENCE = 1000;

	protected CustomDialect() {
		// 我们将设置此方言与“方言处理器”优先级相同
        // 标准方言, 以便处理器执行交错。
		super(DIALECT_NAME, PREFIX, PROCESSOR_PRECEDENCE);
	}

	@Override
	public Set<IProcessor> getProcessors(final String dialectPrefix) {
		Set<IProcessor> processors = new HashSet<IProcessor>();
        processors.add(new ActFormTagProcessor(dialectPrefix));//添加我们定义的标签
        return processors;
	}

}

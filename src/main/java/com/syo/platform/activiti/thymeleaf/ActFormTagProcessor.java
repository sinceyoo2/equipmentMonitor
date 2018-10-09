package com.syo.platform.activiti.thymeleaf;

import java.util.HashMap;
import java.util.Map;

import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IModelFactory;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;
import org.thymeleaf.templatemode.TemplateMode;

public class ActFormTagProcessor extends AbstractAttributeTagProcessor {
	
	private static final String ATTR_NAME = "asText";//属性名称  
    private static final int PRECEDENCE = 10000;//同一方言中的优先级

	public ActFormTagProcessor(final String dialectPrefix) {  
        super(  
            TemplateMode.HTML, // 此处理器将仅应用于HTML模式  
            dialectPrefix,     // 要应用于名称的匹配前缀  
            null,              // 无标签名称：匹配任何标签名称  
            false,             // 没有要应用于标签名称的前缀  
            ATTR_NAME,         // 将匹配的属性的名称  
            true,              // 将方言前缀应用于属性名称  
            PRECEDENCE,        // 优先(内部方言自己的优先)  
            true);             // 之后删除匹配的属性  
    }  
  
  
    @Override  
    protected void doProcess(  
            final ITemplateContext context, final IProcessableElementTag tag,  
            final AttributeName attributeName, final String attributeValue,  
            final IElementTagStructureHandler structureHandler) {  
    	
//        ApplicationContext appCtx = SpringContextUtils.getApplicationContext(context);  
//        MatterMapper mapper=appCtx.getBean(MatterMapper.class);  
  
        final IEngineConfiguration configuration = context.getConfiguration();  
  
        /* 
         * 获得Thymeleaf标准表达式解析器 
         */  
        final IStandardExpressionParser parser = StandardExpressions.getExpressionParser(configuration);  
  
        /* 
         * 将属性值解析为Thymeleaf标准表达式 
         */  
        final IStandardExpression expression = parser.parseExpression(context, attributeValue);  
  
        /* 
         * 执行刚刚解析的表达式 
         */  
//        final Integer position = (Integer) expression.execute(context);  
        Object position = expression.execute(context);  
        if(position==null) {
        	position = "";
        }
        

        Map<String, Map<String, Boolean>> formProperties = context.getVariable("formProperties")==null?new HashMap<>():(HashMap<String, Map<String, Boolean>>) context.getVariable("formProperties");
        Map<String, Boolean> writables = formProperties.get("writable")==null?new HashMap<>():formProperties.get("writable");

        String formId = tag.getAttributeValue("id");
        if(writables.get(formId)!=null && writables.get(formId)) {
        	if(tag.getElementCompleteName().equals("textarea")) {
        		structureHandler.setBody(position.toString(), true);
        	} else {
        		structureHandler.setAttribute("value",  position.toString());
        	}
        	
        }  else {
        	 /*
             *  创建将替换自定义标签的DOM结构。
             * logo将显示在“<div>”标签内, 因此必须首先创建, 
             * 然后必须向其中添加一个节点。
             */
            final IModelFactory modelFactory = context.getModelFactory();

            final IModel model = modelFactory.createModel();

            model.add(modelFactory.createOpenElementTag("span"));
            model.add(modelFactory.createText(position+""));
            model.add(modelFactory.createCloseElementTag("span"));

            /*
             * 指示引擎用指定的模型替换整个元素。
             */
            structureHandler.replaceWith(model, false);
        }
  
    }   

}

package de.mhus.lib.core.pojo;

import java.lang.annotation.Annotation;

public class DefaultStrategy implements PojoStrategy {

	AttributesStrategy attributeStrategy;
	FunctionsStrategy functionsStrategy;
	
	public DefaultStrategy() {
		this(true,".", null);
	}
	
	public DefaultStrategy(boolean embedded, String embedGlue, Class<? extends Annotation>[] annotationMarker) {
		attributeStrategy = new AttributesStrategy(embedded,true,embedGlue,annotationMarker);
		functionsStrategy = new FunctionsStrategy(embedded, true, embedGlue, false, annotationMarker);
	}
	
	@Override
	public void parseObject(PojoParser parser, Object pojo, PojoModelImpl model) {
		Class<?> clazz = pojo.getClass();
		parse(parser, clazz, model);
	}

	@Override
	public void parse(PojoParser parser, Class<?> clazz, PojoModelImpl model) {
		functionsStrategy.parse(parser, clazz, model);
		attributeStrategy.parse(parser, clazz, model);
	}

}

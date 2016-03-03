package de.mhus.lib.core.pojo;

import java.lang.annotation.Annotation;

/**
 * <p>DefaultStrategy class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class DefaultStrategy implements PojoStrategy {

	AttributesStrategy attributeStrategy;
	FunctionsStrategy functionsStrategy;
	
	/**
	 * <p>Constructor for DefaultStrategy.</p>
	 */
	public DefaultStrategy() {
		this(true,".", null);
	}
	
	/**
	 * <p>Constructor for DefaultStrategy.</p>
	 *
	 * @param embedded a boolean.
	 * @param embedGlue a {@link java.lang.String} object.
	 * @param annotationMarker an array of {@link java.lang.Class} objects.
	 */
	public DefaultStrategy(boolean embedded, String embedGlue, Class<? extends Annotation>[] annotationMarker) {
		attributeStrategy = new AttributesStrategy(embedded,true,embedGlue,annotationMarker);
		functionsStrategy = new FunctionsStrategy(embedded, true, embedGlue, false, annotationMarker);
	}
	
	/** {@inheritDoc} */
	@Override
	public void parseObject(PojoParser parser, Object pojo, PojoModelImpl model) {
		Class<?> clazz = pojo.getClass();
		parse(parser, clazz, model);
	}

	/** {@inheritDoc} */
	@Override
	public void parse(PojoParser parser, Class<?> clazz, PojoModelImpl model) {
		functionsStrategy.parse(parser, clazz, model);
		attributeStrategy.parse(parser, clazz, model);
	}

}

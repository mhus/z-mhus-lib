package de.mhus.lib.core.pojo;

import java.io.IOException;
import java.lang.annotation.Annotation;

/**
 * Usage new Parser().parse(object).filter(new DefautFilter()).getModel()
 * @author mikehummel
 *
 */
public class PojoParser {

	private PojoModelImpl model;
	
	public PojoParser parse(Object pojo) {
		return parse(pojo, (PojoStrategy)null);
	}
	
	public PojoParser parse(Object pojo, String embedGlue, Class<? extends Annotation>[] annotationMarker ) {
		return parse(pojo, new DefaultStrategy(true, embedGlue, annotationMarker) );
	}
	
	public PojoParser parse(Object pojo, PojoStrategy strategy) {
		if (model == null) model = new PojoModelImpl(pojo.getClass());
		if (strategy == null) strategy = new DefaultStrategy();
		if (pojo instanceof Class) {
			strategy.parse(this, (Class<?>)pojo, model);
		} else {
			strategy.parseObject(this, pojo, model);
		}
		return this;
	}
	
	public PojoParser filter(PojoFilter filter) {
		filter.filter(model);
		return this;
	}
	
	public PojoModel getModel() {
		return model;
	}

	public static Object checkParent(PojoAttribute<Object> parent,
			Object pojo) throws IOException {
		Object out = pojo;
		if (parent !=  null) {
			out = parent.get(pojo);
			if (out == null) {
				try {
					out = parent.getType().newInstance();
					parent.set(pojo, out);
				} catch (InstantiationException | IllegalAccessException e) {
					throw new IOException("can't create parent: " + parent.getName(), e);
				}
			}
		}
		return out;
	}
	
}

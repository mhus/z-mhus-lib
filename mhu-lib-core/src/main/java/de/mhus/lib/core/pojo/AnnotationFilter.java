package de.mhus.lib.core.pojo;

import java.lang.annotation.Annotation;

/**
 * <p>AnnotationFilter class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class AnnotationFilter implements PojoFilter {

	private Class<? extends Annotation>[] allowed;
	/**
	 * <p>Constructor for AnnotationFilter.</p>
	 *
	 * @param allowed a {@link java.lang.Class} object.
	 */
	@SafeVarargs
	public AnnotationFilter(Class<? extends Annotation> ... allowed) {
		this.allowed = allowed;
	}
	/** {@inheritDoc} */
	@Override
	public void filter(PojoModelImpl model) {
		for (String name : model.getAttributeNames()) {
			PojoAttribute<?> attr = model.getAttribute(name);
			boolean done = false;
			for (Class<? extends Annotation> a : allowed)
				if (attr.getAnnotation(a) != null) {
					done = true;
					break;
				}
			if (!done) {
				model.removeAttribute(name);
			}
		}
		
		for (String name : model.getActionNames()) {
			PojoAction attr = model.getAction(name);
			boolean done = false;
			for (Class<? extends Annotation> a : allowed)
				if (attr.getAnnotation(a) != null) {
					done = true;
					break;
				}
			if (!done) {
				model.removeAction(name);
			}
		}
		
	}

}

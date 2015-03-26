package de.mhus.lib.core.pojo;

import java.lang.annotation.Annotation;

public class AnnotationFilter implements PojoFilter {

	private Class<? extends Annotation>[] allowed;
	public AnnotationFilter(Class<? extends Annotation> ... allowed) {
		this.allowed = allowed;
	}
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

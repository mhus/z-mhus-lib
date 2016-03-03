package de.mhus.lib.core.pojo;

import java.io.IOException;
import java.lang.annotation.Annotation;

/**
 * Usage new Parser().parse(object).filter(new DefautFilter()).getModel()
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class PojoParser {

	private PojoModelImpl model;
	
	/**
	 * <p>parse.</p>
	 *
	 * @param pojo a {@link java.lang.Object} object.
	 * @return a {@link de.mhus.lib.core.pojo.PojoParser} object.
	 */
	public PojoParser parse(Object pojo) {
		return parse(pojo, (PojoStrategy)null);
	}
	
	/**
	 * <p>parse.</p>
	 *
	 * @param pojo a {@link java.lang.Object} object.
	 * @param embedGlue a {@link java.lang.String} object.
	 * @param annotationMarker an array of {@link java.lang.Class} objects.
	 * @return a {@link de.mhus.lib.core.pojo.PojoParser} object.
	 */
	public PojoParser parse(Object pojo, String embedGlue, Class<? extends Annotation>[] annotationMarker ) {
		return parse(pojo, new DefaultStrategy(true, embedGlue, annotationMarker) );
	}
	
	/**
	 * <p>parse.</p>
	 *
	 * @param pojo a {@link java.lang.Object} object.
	 * @param strategy a {@link de.mhus.lib.core.pojo.PojoStrategy} object.
	 * @return a {@link de.mhus.lib.core.pojo.PojoParser} object.
	 */
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
	
	/**
	 * <p>filter.</p>
	 *
	 * @param removeHidden a boolean.
	 * @param removeEmbedded a boolean.
	 * @param removeWriteOnly a boolean.
	 * @param removeReadOnly a boolean.
	 * @param removeNoActions a boolean.
	 * @return a {@link de.mhus.lib.core.pojo.PojoParser} object.
	 * @since 3.2.9
	 */
	public PojoParser filter(boolean removeHidden, boolean removeEmbedded, boolean removeWriteOnly, boolean removeReadOnly, boolean removeNoActions ) {
		return filter(new DefaultFilter(removeHidden, removeEmbedded, removeWriteOnly, removeReadOnly, removeNoActions));
	}
	
	/**
	 * <p>filter.</p>
	 *
	 * @param filter a {@link de.mhus.lib.core.pojo.PojoFilter} object.
	 * @return a {@link de.mhus.lib.core.pojo.PojoParser} object.
	 */
	public PojoParser filter(PojoFilter filter) {
		filter.filter(model);
		return this;
	}
	
	/**
	 * <p>Getter for the field <code>model</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.core.pojo.PojoModel} object.
	 */
	public PojoModel getModel() {
		return model;
	}

	/**
	 * <p>checkParent.</p>
	 *
	 * @param parent a {@link de.mhus.lib.core.pojo.PojoAttribute} object.
	 * @param pojo a {@link java.lang.Object} object.
	 * @return a {@link java.lang.Object} object.
	 * @throws java.io.IOException if any.
	 */
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

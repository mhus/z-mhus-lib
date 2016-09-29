package de.mhus.lib.core.matcher;

import java.util.LinkedList;
import java.util.Map;

/**
 * <p>Abstract ModelComposit class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public abstract class ModelComposit extends ModelPart {

	protected LinkedList<ModelPart> components = new LinkedList<>();
	
	/**
	 * <p>add.</p>
	 *
	 * @param part a {@link de.mhus.lib.core.matcher.ModelPart} object.
	 */
	public void add(ModelPart part) {
		components.add(part);
	}
	
	/**
	 * <p>size.</p>
	 *
	 * @return a int.
	 */
	public int size() {
		return components.size();
	}

	/** {@inheritDoc} */
	protected abstract boolean matches(Map<String,?> map);

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return getOperatorName() + (isNot() ? " not" : "") + components;
	}
	
	/**
	 * <p>getOperatorName.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public abstract String getOperatorName();
	
}

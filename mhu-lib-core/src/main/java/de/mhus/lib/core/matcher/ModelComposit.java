package de.mhus.lib.core.matcher;

import java.util.LinkedList;

/**
 * <p>Abstract ModelComposit class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
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

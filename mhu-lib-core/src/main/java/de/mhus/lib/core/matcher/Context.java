package de.mhus.lib.core.matcher;

/**
 * <p>Context class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class Context {

	public Context parentContext;
	public ModelPart first;
	ModelComposit current = null;
	ModelComposit parent = null;
	public boolean not;
	ModelComposit root = null;
	/**
	 * <p>findRoot.</p>
	 *
	 * @return a {@link de.mhus.lib.core.matcher.ModelComposit} object.
	 */
	public ModelComposit findRoot() {
		if (root == null && first != null) {
			root = new ModelAnd();
			root.add(first);
		}
		return root;
	}
	/**
	 * <p>append.</p>
	 *
	 * @param next a {@link de.mhus.lib.core.matcher.ModelComposit} object.
	 */
	public void append(ModelComposit next) {
		if (root == null) {
			root = next;
			current = next;
			parent = next;
		} else {
			if (current == null) current = parent;
			current.add(next);
		}
		if (first != null) next.add(first);
		current = next;
		parent  = next;
		first = null;
		not = false;
	}
	
}

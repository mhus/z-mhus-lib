package de.mhus.lib.core.matcher;

public class Context {

	public Context parentContext;
	public ModelPart first;
	ModelComposit current = null;
	ModelComposit parent = null;
	public boolean not;
	ModelComposit root = null;
	public ModelComposit findRoot() {
		if (root == null && first != null) {
			root = new ModelAnd();
			root.add(first);
		}
		return root;
	}
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

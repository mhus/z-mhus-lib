package de.mhus.lib.form;

import java.util.LinkedList;

/**
 * <p>Abstract LayoutBuilderWithStack class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public abstract class LayoutBuilderWithStack<T> extends UiBuilder {

	protected T currentComponent;
	private LinkedList<T> stack = new LinkedList<T>();

	/**
	 * <p>push.</p>
	 *
	 * @param cur a T object.
	 */
	protected void push(T cur) {
		stack.add(cur);
		currentComponent = cur;
	}
	
	/**
	 * <p>pop.</p>
	 *
	 * @return a T object.
	 */
	protected T pop() {
		stack.removeLast();
		currentComponent = null;
		if (stack.size() > 0) currentComponent = stack.getLast();
		return currentComponent;
	}

}

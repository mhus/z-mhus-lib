package de.mhus.lib.form;

import java.util.LinkedList;

public abstract class LayoutBuilderWithStack<T> extends UiBuilder {

	protected T currentComponent;
	private LinkedList<T> stack = new LinkedList<T>();

	protected void push(T cur) {
		stack.add(cur);
		currentComponent = cur;
	}
	
	protected T pop() {
		stack.removeLast();
		currentComponent = null;
		if (stack.size() > 0) currentComponent = stack.getLast();
		return currentComponent;
	}

}

package de.mhus.test.forms04;

import de.mhus.lib.vaadin.container.MhuBeanItemContainer;

public class MyDataContainer extends MhuBeanItemContainer<MyData> {

	public MyDataContainer() throws IllegalArgumentException {
		super(MyData.class);
	}

}

package de.mhus.lib.vaadin.form;

import java.util.Properties;

import de.mhus.lib.form.binding.MemoryDataSource;

public class MyDataSource extends MemoryDataSource {

	public MyDataSource(Properties properties) {
		super(properties);
	}

	public void setPropertyData(String key, Object value) {
		System.out.println("DataSource: " + key +"=" + value);
		super.setPropertyData(key, value);
	}
}

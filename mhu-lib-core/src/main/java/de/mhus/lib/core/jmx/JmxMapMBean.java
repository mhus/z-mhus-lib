package de.mhus.lib.core.jmx;

public interface JmxMapMBean extends JmxObjectMBean {

	public int getSize();
	
	public String[] getEntries();
	
	public void clear();
	
	public void remove(String key);
	
}

package de.mhus.lib.core.jmx;

public interface JmxListMBean extends JmxObjectMBean {

	public int getSize();
	
	public String[] getEntries();
	
	public void clear();
	
	public void remove(int nr);
	
}

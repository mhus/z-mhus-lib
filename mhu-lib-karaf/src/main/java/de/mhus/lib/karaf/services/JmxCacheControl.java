package de.mhus.lib.karaf.services;

import aQute.bnd.annotation.component.Component;
import de.mhus.lib.annotations.jmx.JmxManaged;
import de.mhus.lib.core.console.ConsoleTable;
import de.mhus.lib.core.jmx.JmxObjectMBean;
import de.mhus.lib.core.jmx.MJmx;
import de.mhus.lib.karaf.MOsgi;

// http://localhost:8181/jolokia/read/de.mhus.lib.core.jmx.JmxObject:name=de.mhus.lib.karaf.services.JmxCacheControl,type=JmxCacheControl
@Component(immediate=true,provide=JmxObjectMBean.class)
@JmxManaged(descrition = "Cache Control Service")
public class JmxCacheControl extends MJmx {

	@JmxManaged
	public String[][] getTable() {
		ConsoleTable table = new ConsoleTable();
		table.setHeaderValues("Name","Size","Enabled","Status");
		for (CacheControlIfc c : MOsgi.getServices(CacheControlIfc.class, null))
			try {
				table.addRowValues(c.getName(), c.getSize(),c.isEnabled(), "ok");
			} catch (Throwable t) {
				log().d(c,t);
				table.addRowValues(c.getName(), "","", t.getMessage());
			}
		return table.toStringMatrix(false);
	}

}

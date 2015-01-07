package de.mhus.lib.form.control;

import de.mhus.lib.core.MXml;
import de.mhus.lib.form.DataSource;
import de.mhus.lib.form.LayoutElement;

public class InformationFocus implements FocusManager {

	private String name = DataSource.NAME_INFORMATION;

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public void focused(LayoutElement element) {
		
		String msg = "";
		String t = element.getTitle();
		if (t != null) msg = msg + "<b>" + MXml.encode(t) + "</b><br/>";
		
		t = element.getErrorMessage();
		if (t != null) msg = msg + "<font color=red>" + MXml.encode(t) + "</font><br/>";
		t = element.getDescription();
		if (t != null) msg = msg + MXml.encode(t) + "<br/>";
		
		try {
			element.getDataSource().setString( name, msg );
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

}

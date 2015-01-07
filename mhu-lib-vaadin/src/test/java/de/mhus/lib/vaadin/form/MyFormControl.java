package de.mhus.lib.vaadin.form;

import java.util.Date;

import de.mhus.lib.core.MXml;
import de.mhus.lib.form.LayoutElement;
import de.mhus.lib.form.SimpleFormControl;

public class MyFormControl extends SimpleFormControl {

	@Override
	public void wizzard(LayoutElement element) {
		System.out.println("error .. now");
		element.setErrorMessage("error_mist", new Date());
	}

	@Override
	public void focused(LayoutElement element) {
		// element.setErrorMessage(null);
		System.out.println("Focus: " + element.getNlsPrefix());
		try {
			String msg = "";
			String t = element.getTitle();
			if (t != null) msg = msg + "<b>" + MXml.encode(t) + "</b><br/>";
			
			t = element.getErrorMessage();
			if (t != null) msg = msg + "<font color=red>" + MXml.encode(t) + "</font><br/>";
			t = element.getDescription();
			if (t != null) msg = msg + MXml.encode(t) + "<br/>";
			
			element.getDataSource().setString("_information", msg);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}

package de.mhus.lib.vaadin.aqua;

import com.vaadin.ui.UI;

public class GuiUtil {

	public static AquaApi getApi() {
		return (AquaApi) UI.getCurrent();
	}
	
}

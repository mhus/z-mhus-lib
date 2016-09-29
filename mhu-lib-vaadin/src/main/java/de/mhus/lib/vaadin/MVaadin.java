package de.mhus.lib.vaadin;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Window;

public class MVaadin {
	
	public static boolean closeDialog(AbstractComponent component) {
		while (component != null) {
			if (component instanceof Window) {
				((Window)component).close();
				return true;
			}
		}
		return false;
	}
	
}

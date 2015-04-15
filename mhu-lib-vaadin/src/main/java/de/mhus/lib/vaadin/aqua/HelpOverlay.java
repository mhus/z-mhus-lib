/**
 * Taken from vaadin demo. will be rewritten and extended. Currently it's a placeholder.
 */
package de.mhus.lib.vaadin.aqua;

import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Window;

public class HelpOverlay extends Window {

	private static final long serialVersionUID = 1L;

	public HelpOverlay() {
        setContent(new CssLayout());
        setPrimaryStyleName("help-overlay");
        setDraggable(false);
        setResizable(false);
    }

    public void addComponent(Component c) {
        ((CssLayout) getContent()).addComponent(c);
    }

}

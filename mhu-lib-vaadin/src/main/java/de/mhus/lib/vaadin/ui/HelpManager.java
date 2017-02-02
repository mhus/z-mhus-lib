/**
 * Taken from vaadin demo. will be rewritten and extended. Currently it's a placeholder.
 * 
 * @author jouni@vaadin.com
 * 
 */

package de.mhus.lib.vaadin.ui;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;

public class HelpManager {

    private UI ui;
    private List<HelpOverlay> overlays = new ArrayList<HelpOverlay>();

    public HelpManager(UI ui) {
        this.ui = ui;
    }

    public void closeAll() {
        for (HelpOverlay overlay : overlays) {
            overlay.close();
        }
        overlays.clear();
    }

    public void showHelp(String caption, String text, String style) {
    	HelpOverlay w = addOverlay(caption, text, style);
        w.center();
        if (ui != null) ui.addWindow(w);
    }
    
    protected HelpOverlay addOverlay(String caption, String text, String style) {
        HelpOverlay o = new HelpOverlay();
        o.setCaption(caption);
        if (text != null) o.addComponent(new Label(text, ContentMode.HTML));
        if (style != null) o.setStyleName(style);
        overlays.add(o);
        return o;
    }

}

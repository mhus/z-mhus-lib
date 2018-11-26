/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * Taken from vaadin demo. will be rewritten and extended. Currently it's a placeholder.
 * 
 * @author jouni@vaadin.com
 * 
 */

package de.mhus.lib.vaadin.ui;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.UI;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Label;

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

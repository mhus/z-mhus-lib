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
package de.mhus.lib.vaadin.layouter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractSplitPanel;
import com.vaadin.ui.Layout;

import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.errors.MException;

public class LayUtil {

	private static final Pattern sizePattern = Pattern
            .compile("^(-?\\d+(\\.\\d+)?)(%|px|em|ex|in|cm|mm|pt|pc)?$");
	
	public static void configure(AbstractComponent layout, ResourceNode<?> config) throws MException {
		if (config.getBoolean(LayoutBuilder.FULL_SIZE, false))
			layout.setSizeFull();
		else {
			String width = config.getString(LayoutBuilder.WIDTH, null);
			if (width != null) layout.setWidth(width);
			String height = config.getString(LayoutBuilder.HEIGHT, null);
			if (height != null) layout.setHeight(height);
		}
		
		// margin
//TODO		if (layout instanceof Layout && config.isProperty(LayoutBuilder.MARGIN))
//			((Layout)layout).setMargin(config.getBoolean(LayoutBuilder.MARGIN, false));
		// spacing
		if (layout instanceof Layout.SpacingHandler && config.isProperty(LayoutBuilder.SPACING))
			((Layout.SpacingHandler)layout).setSpacing(config.getBoolean(LayoutBuilder.SPACING, false));
		// styles
		if (config.isProperty(LayoutBuilder.STYLE)) {
			layout.setStyleName(config.getExtracted(LayoutBuilder.STYLE));
		}
		// hidden
		if (config.getBoolean(LayoutBuilder.HIDDEN, false))
			layout.setVisible(false);
		
		// split
		if (layout instanceof AbstractSplitPanel) {
			String a = config.getString(LayoutBuilder.SPLIT_MIN,null);
			if (a != null) { 
				float[] s = parseStringSize(a);
				if (s[0] >=0)
					((AbstractSplitPanel)layout).setMinSplitPosition(s[0],Unit.values()[(int)s[1]]);
			}
			a = config.getString(LayoutBuilder.SPLIT_MAX,null);
			if (a != null) { 
				float[] s = parseStringSize(a);
				if (s[0] >=0)
					((AbstractSplitPanel)layout).setMaxSplitPosition(s[0],Unit.values()[(int)s[1]]);
			}
			a = config.getString(LayoutBuilder.SPLIT_POS,null);
			if (a != null) { 
				float[] s = parseStringSize(a);
				if (s[0] >=0)
					((AbstractSplitPanel)layout).setSplitPosition(s[0],Unit.values()[(int)s[1]]);
			}
		}
		
	}
	
    /*
     * Taken from com.vaadin.ui.AbstractComponent:
 *
 * Copyright 2011 Vaadin Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
     * Returns array with size in index 0 unit in index 1. Null or empty string
     * will produce {-1,UNITS_PIXELS}
     */
    public static float[] parseStringSize(String s) {
        float[] values = { -1, Unit.PIXELS.ordinal() };
        if (s == null) {
            return values;
        }
        s = s.trim();
        if ("".equals(s)) {
            return values;
        }

        Matcher matcher = sizePattern.matcher(s);
        if (matcher.find()) {
            values[0] = Float.parseFloat(matcher.group(1));
            if (values[0] < 0) {
                values[0] = -1;
            } else {
                String unit = matcher.group(3);
                if (unit == null) {
                    values[1] = Unit.PIXELS.ordinal();
                } else if (unit.equals("px")) {
                    values[1] = Unit.PIXELS.ordinal();
                } else if (unit.equals("%")) {
                    values[1] = Unit.PERCENTAGE.ordinal();
                } else if (unit.equals("em")) {
                    values[1] = Unit.EM.ordinal();
                } else if (unit.equals("ex")) {
                    values[1] = Unit.EX.ordinal();
                } else if (unit.equals("in")) {
                    values[1] = Unit.INCH.ordinal();
                } else if (unit.equals("cm")) {
                    values[1] = Unit.CM.ordinal();
                } else if (unit.equals("mm")) {
                    values[1] = Unit.MM.ordinal();
                } else if (unit.equals("pt")) {
                    values[1] = Unit.POINTS.ordinal();
                } else if (unit.equals("pc")) {
                    values[1] = Unit.PICAS.ordinal();
                }
            }
        } else {
            throw new IllegalArgumentException("Invalid size argument: \"" + s
                    + "\" (should match " + sizePattern.pattern() + ")");
        }
        return values;
    }

}

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
package de.mhus.lib.vaadin;

import com.vaadin.ui.Button;
import com.vaadin.ui.themes.ValoTheme;

import de.mhus.lib.core.MXml;
import de.mhus.lib.vaadin.ui.Border;

public class CardButton extends Button {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String caption;
	private Border border;
	private String backgroundColor;
	private String foregroundColor;
	private Border margin = new Border(1,1,1,1);
	private Border padding = new Border(5,5,5,5);
	private boolean centerVertical = true;

	//Button b2 = new Button("<div style='color:red;border:1px solid gray' width='100%' height='30'>&nbsp;  2</div>");

	public CardButton(String caption) {
		this();
		setCaption(caption);
	}
	public CardButton() {
		setCaptionAsHtml(true);
		setStyleName(ValoTheme.BUTTON_LINK);
	}
	
	@Override
	public void setCaption(String caption) {
		this.caption = caption;
		updateCaption();
	}

	@SuppressWarnings("deprecation")
	public void updateCaption() {
		
		if (getIcon() != null) {
			setCaption("");
			return;
		}
		
		String c = "<div style='";
		
		int innerWidth = 0;
		int innerHeight = 0;
		
		if (getHeightUnits() == UNITS_PIXELS) {
			innerHeight = (int)getHeight();
			if (margin != null)
				innerHeight-= (margin.top + margin.bottom);
			if (padding != null)
				innerHeight-= (padding.top + padding.bottom);
			if (border != null)
				innerHeight-= (border.top + border.bottom);
		}
		
		if (getWidthUnits() == UNITS_PIXELS) {
			innerWidth = (int)getWidth();
			if (margin != null)
				innerWidth-= (margin.left + margin.right);
			if (padding != null)
				innerWidth-= (padding.left + padding.right);
			if (border != null)
				innerWidth-= (border.left + border.right);
		}
		
		if (innerWidth > 0)
			c+="width:" + innerWidth + "px;";
		else
			c+="width:auto;";
		
		if (innerHeight > 0)
			c+="height:" + innerHeight + "px;";
		else
			c+="height:auto;";
			
		if (margin != null)
			c+="margin:" + margin.toStyle();
		if (padding != null)
			c+="padding:" + padding.toStyle();
		if (border != null)
			c+=border.toBorderStyle() + ";";
		if (backgroundColor != null)
			c+="background-color:" + backgroundColor + ";";
		if (foregroundColor != null)
			c+="color:" + foregroundColor + ";";

		c+="'>";
		if (centerVertical)
			c+="<div style='display:table-cell;vertical-align:middle;height:"+innerHeight+"px'>";
		c+= MXml.encode(caption);
		if (centerVertical)
			c+="</div>";
		c+="</div>";
		
		super.setCaption(c);
	}

	public Border getBorder() {
		return border;
	}

	public void setBorder(Border border) {
		this.border = border;
	}

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public String getForegroundColor() {
		return foregroundColor;
	}

	public void setForegroundColor(String foregroundColor) {
		this.foregroundColor = foregroundColor;
	}
	public Border getMargin() {
		return margin;
	}
	public void setMargin(Border margin) {
		this.margin = margin;
	}
	public Border getPadding() {
		return padding;
	}
	public void setPadding(Border padding) {
		this.padding = padding;
	}
	public boolean isCenterVertical() {
		return centerVertical;
	}
	public void setCenterVertical(boolean centerVertical) {
		this.centerVertical = centerVertical;
	}
	
}

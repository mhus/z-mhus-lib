package de.mhus.lib.vaadin.form2;

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.TextArea;

import de.mhus.lib.errors.MException;

public class UiTextArea extends UiText {

	protected AbstractField createTextField() throws MException {
		TextArea out = new TextArea();
		out.setHeight(getElement().getConfig().getInt("height", 200),Sizeable.UNITS_PIXELS);
		return out;
	}

}

package de.mhus.lib.vaadin.form2;

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.RichTextArea;

import de.mhus.lib.errors.MException;

public class UiRichText extends UiText {

	protected AbstractField createTextField() throws MException {
		RichTextArea out = new RichTextArea();
		out.setHeight(getElement().getConfig().getInt("height", 300),  Sizeable.UNITS_PIXELS);
		return out;
	}

}

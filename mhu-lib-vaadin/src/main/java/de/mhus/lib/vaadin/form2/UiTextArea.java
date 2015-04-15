package de.mhus.lib.vaadin.form2;

import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.TextArea;

import de.mhus.lib.errors.MException;

public class UiTextArea extends UiText {

	@SuppressWarnings("rawtypes")
	@Override
	protected AbstractField createTextField() throws MException {
		TextArea out = new TextArea();
		out.setHeight(getElement().getConfig().getInt("height", 200),Unit.PIXELS);
		return out;
	}

}

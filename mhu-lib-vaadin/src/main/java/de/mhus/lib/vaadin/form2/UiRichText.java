package de.mhus.lib.vaadin.form2;

import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.RichTextArea;

import de.mhus.lib.errors.MException;
import de.mhus.lib.form.definition.FmElement;

public class UiRichText extends UiText {

	@Override
	@SuppressWarnings("rawtypes")
	protected AbstractField createTextField() throws MException {
		RichTextArea out = new RichTextArea();
		out.setHeight(getElement().getConfig().getInt(FmElement.HEIGHT, 300),  Unit.PIXELS);
		return out;
	}

}

package de.mhus.lib.vaadin.form;

import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.errors.MException;
import de.mhus.lib.form.ComponentAdapter;
import de.mhus.lib.form.ComponentDefinition;
import de.mhus.lib.form.Form;
import de.mhus.lib.form.UiComponent;
import de.mhus.lib.form.definition.FmNumber;
import de.mhus.lib.vaadin.aqua.SpinnerNumberField;

public class UiNumber extends UiVaadin {

	private String type;

	@Override
	protected void setValue(Object value) throws MException {
		((SpinnerNumberField)getComponentEditor()).setValue(MCast.toString(value));
	}

	@Override
	public Component createEditor() {
		SpinnerNumberField<?> spinner = null;
		
		type = getConfig().getString(FmNumber.NUMBER_TYPE,"").toUpperCase();
		if (type.equals(FmNumber.TYPES.DOUBLE.name()))
			spinner = new SpinnerNumberField<Double>(Double.class);
		else
		if (type.equals(FmNumber.TYPES.FLOAT.name()))
			spinner = new SpinnerNumberField<Float>(Float.class);
		else
		if (type.equals(FmNumber.TYPES.LONG.name()))
			spinner = new SpinnerNumberField<Long>(Long.class);
		else
			spinner = new SpinnerNumberField<Integer>(Integer.class);

		spinner.setAllowNegative(getConfig().getBoolean("allow_negative", true));
        spinner.setAllowNull(getConfig().getBoolean("allow_null", false));
        spinner.setMaxVal(getConfig().getLong("max", Long.MAX_VALUE));
        spinner.setMinVal(getConfig().getLong("min", Long.MIN_VALUE));
//        if (getElement().getConfig().isProperty("format") )
//        	spinner.setNumberFormat(NumberFormat.valueOf(getElement().getConfig().getString("format","")));
//		spinner.setTextAlignment(TextAlignment.RIGHT);
		return spinner;

	}

	@Override
	protected Object getValue() throws MException {
		return ((SpinnerNumberField)getComponentEditor()).getValue();
	}

	public static class Adapter implements ComponentAdapter {

		@Override
		public UiComponent createAdapter(IConfig config) {
			return new UiNumber();
		}

		@Override
		public ComponentDefinition getDefinition() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}

}

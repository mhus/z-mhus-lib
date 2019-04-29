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
package de.mhus.lib.vaadin.form;

import com.vaadin.ui.Component;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.errors.MException;
import de.mhus.lib.form.ComponentAdapter;
import de.mhus.lib.form.ComponentDefinition;
import de.mhus.lib.form.UiComponent;
import de.mhus.lib.form.definition.FmNumber;
import de.mhus.lib.vaadin.ui.SpinnerNumberField;

@SuppressWarnings("deprecation")
public class UiNumber extends UiVaadin {

	private String type;

	@SuppressWarnings("rawtypes")
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

	@SuppressWarnings("rawtypes")
	@Override
	protected Object getValue() throws MException {
		return ((SpinnerNumberField)getComponentEditor()).getNumberValue();
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

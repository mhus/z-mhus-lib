package de.mhus.lib.vaadin.form;

import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.TextField;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.errors.MException;
import de.mhus.lib.form.ComponentAdapter;
import de.mhus.lib.form.ComponentDefinition;
import de.mhus.lib.form.Form;
import de.mhus.lib.form.UiComponent;
import de.mhus.lib.form.ui.FmDate;
import de.mhus.lib.form.ui.FmDate.FORMATS;

public class UiDate extends UiVaadin {

	@Override
	protected void setValue(Object value) throws MException {
		((DateField)getComponentEditor()).setValue(MCast.toDate(value, null));
	}

	@Override
	public Component createEditor() {
		DateField ret = new DateField();
		ret.setLocale(getForm().getLocale());
		FORMATS format = FmDate.FORMATS.valueOf(getConfig().getString("format",FmDate.FORMATS.DATE.name()).toUpperCase());
		switch (format) {
		case DATE:
			ret.setResolution(Resolution.DAY);
			break;
		case DATETIME:
			ret.setResolution(Resolution.MINUTE);
			break;
		case DATETIMESECONDS:
			ret.setResolution(Resolution.SECOND);
			break;
		case TIME:
			ret.setDateFormat("HH:mm");
			ret.setResolution(Resolution.MINUTE);
			break;
		case TIMESECONDS:
			ret.setDateFormat("HH:mm:ss");
			ret.setResolution(Resolution.SECOND);
			break;
		default:
			break;
		}
		return ret;
	}

	@Override
	protected Object getValue() throws MException {
		return ((DateField)getComponentEditor()).getValue();
	}

	public static class Adapter implements ComponentAdapter {

		@Override
		public UiComponent createAdapter(IConfig config) {
			return new UiDate();
		}

		@Override
		public ComponentDefinition getDefinition() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}

}

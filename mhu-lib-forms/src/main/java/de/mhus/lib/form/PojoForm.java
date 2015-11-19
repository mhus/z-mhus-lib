package de.mhus.lib.form;

import java.util.Locale;

import de.mhus.lib.annotations.form.ALayoutModel;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.definition.DefRoot;
import de.mhus.lib.core.pojo.DefaultFilter;
import de.mhus.lib.core.pojo.PojoAction;
import de.mhus.lib.core.pojo.PojoModel;
import de.mhus.lib.core.pojo.PojoParser;
import de.mhus.lib.core.util.MNls;

public class PojoForm extends Form {

	public PojoForm(Locale locale, ComponentAdapterProvider adapterProvider, Object pojo) throws Exception {
		super(locale, adapterProvider, createModel(pojo));
		setDataSource(new PojoDataSource(pojo));
	}

	private static IConfig createModel(Object pojo) throws Exception {
		
		String modelName = "";

		PojoModel pojoModel = new PojoParser().parse(pojo).filter(new DefaultFilter()).getModel();

		DefRoot definition = null;
		MNls nls = null;
		// looking for models
		for (String actionName : pojoModel.getActionNames()) {
			PojoAction action = pojoModel.getAction(actionName);
			if (action.getAnnotation(ALayoutModel.class) != null) {
				if (((ALayoutModel)action.getAnnotation(ALayoutModel.class)).value().equals(modelName) && action.getReturnType().equals(DefRoot.class)) {
					definition = ((DefRoot) action.doExecute(pojo));
				} else
				if (((ALayoutModel)action.getAnnotation(ALayoutModel.class)).value().equals(modelName) && action.getReturnType() == MNls.class) {
					nls = (MNls) action.doExecute(pojo);
				}
			}
		}		
		return null;
	}

}

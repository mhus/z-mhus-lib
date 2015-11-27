package de.mhus.lib.form;

import de.mhus.lib.annotations.form.ALayoutModel;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.definition.DefRoot;
import de.mhus.lib.core.pojo.DefaultFilter;
import de.mhus.lib.core.pojo.PojoAction;
import de.mhus.lib.core.pojo.PojoModel;
import de.mhus.lib.core.pojo.PojoParser;
import de.mhus.lib.core.util.MNls;

public class PojoForm extends Form {

	public PojoForm(Object pojo) throws Exception {
		this(pojo, "");
	}
	
	public PojoForm(Object pojo, String modelName) throws Exception {
		model = createModel(pojo, modelName);
		
		if (pojo instanceof FormControl)
			setControl((FormControl) pojo);

		if (pojo instanceof DataSource)
			setDataSource((DataSource) pojo);
		else
			setDataSource(new ModelDataSource( new PojoDataSource(pojo)) );
			
	}

	protected IConfig createModel(Object pojo, String modelName) throws Exception {
		
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
		if (nls != null) setNls(nls);
		return definition;
	}

}

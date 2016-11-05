package de.mhus.lib.form;

import de.mhus.lib.annotations.form.ALayoutModel;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.definition.DefRoot;
import de.mhus.lib.core.pojo.DefaultFilter;
import de.mhus.lib.core.pojo.PojoAction;
import de.mhus.lib.core.pojo.PojoModel;
import de.mhus.lib.core.pojo.PojoParser;
import de.mhus.lib.core.util.MNlsBundle;

public class PojoForm extends MForm {

	public PojoForm(PojoProvider pojo) throws Exception {
		this(pojo, "");
		//setNlsBundle(new MNlsFactory().setOwner(pojo.getPojo()));
	}
	
	public PojoForm(PojoProvider  pojo, String modelName) throws Exception {
		model = createModel(pojo.getPojo(), modelName);

		if (pojo instanceof FormControl)
			setControl((FormControl) pojo);

		if (pojo instanceof DataSource)
			setDataSource((DataSource) pojo);
		else
			setDataSource(new ModelDataSource( new PojoDataSource(pojo)) );
			
	}

	protected IConfig createModel(Object pojo, String modelName) throws Exception {
		
		PojoModel pojoModel = new PojoParser().parse(pojo).filter(new DefaultFilter(true, false, true, false, false)).getModel();

		DefRoot definition = null;
		MNlsBundle nls = null;
		FormControl control = null;
		// looking for models
		for (String actionName : pojoModel.getActionNames()) {
			PojoAction action = pojoModel.getAction(actionName);
			if (action.getAnnotation(ALayoutModel.class) != null) {
				if (((ALayoutModel)action.getAnnotation(ALayoutModel.class)).value().equals(modelName) && action.getReturnType().equals(DefRoot.class)) {
					definition = ((DefRoot) action.doExecute(pojo));
				} else
				if (((ALayoutModel)action.getAnnotation(ALayoutModel.class)).value().equals(modelName) && action.getReturnType() == MNlsBundle.class) {
					nls = (MNlsBundle) action.doExecute(pojo);
				} else
				if (((ALayoutModel)action.getAnnotation(ALayoutModel.class)).value().equals(modelName) && action.getReturnType() == FormControl.class) {
					control = (FormControl) action.doExecute(pojo);
				}
					
			}
		}
		if (nls != null) setNlsBundle(nls);
		if (control != null) setControl(control);
		if (definition != null) definition.build();
		return definition;
	}

}

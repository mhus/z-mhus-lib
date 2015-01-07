package de.mhus.lib.form.pojo;

import de.mhus.lib.annotations.form.ALayoutModel;
import de.mhus.lib.core.definition.DefRoot;
import de.mhus.lib.core.pojo.DefaultFilter;
import de.mhus.lib.core.pojo.PojoAction;
import de.mhus.lib.core.pojo.PojoModel;
import de.mhus.lib.core.pojo.PojoParser;
import de.mhus.lib.core.util.MNls;
import de.mhus.lib.form.DataSource;
import de.mhus.lib.form.LayoutModelByConfig;
import de.mhus.lib.form.binding.MemoryDataSource;
import de.mhus.lib.form.binding.PackagingDataSource;
import de.mhus.lib.form.binding.PojoDataSource;

public class LayoutModelByPojo extends LayoutModelByConfig {

	private Object pojo;
	private String modelName = "";
	private PojoDataSource pojoDS;
	private PackagingDataSource ds;
	
	public LayoutModelByPojo() {
		
	}
	
	public LayoutModelByPojo(Object pojo) {
		setPojo(pojo);
	}

	@Override
	public void doBuild() throws Exception {
		PojoModel pojoModel = new PojoParser().parse(pojo).filter(new DefaultFilter()).getModel();
		ds = new PackagingDataSource();
		ds.setDefaultSource(new MemoryDataSource());
		pojoDS = new PojoDataSource(pojo, pojoModel);
		ds.addPackage(DataSource.PACKAGE_PERSISTENT, pojoDS);
		setDataSource(ds);
		
		if (root == null) {
			// looking for models
			for (String actionName : pojoModel.getActionNames()) {
				PojoAction action = pojoModel.getAction(actionName);
				if (action.getAnnotation(ALayoutModel.class) != null) {
					if (((ALayoutModel)action.getAnnotation(ALayoutModel.class)).value().equals(modelName) && action.getReturnType().equals(DefRoot.class)) {
						setDefinition( ((DefRoot) action.doExecute(pojo)) );
					} else
					if (((ALayoutModel)action.getAnnotation(ALayoutModel.class)).value().equals(modelName) && action.getReturnType() == MNls.class) {
						setNls( (MNls) action.doExecute(pojo));
					}
				}
			}
			
			// fallback
			// TODO alternative call action with modelName as attribute
			
			
			if (getNls() == null && (getDefinition() instanceof DefRoot)) {
				setNls(((DefRoot)getDefinition()).createNls());
			}
			
			if (getDefinition() != null)
				super.doBuild();
			else
				log().i("model not found");
		}

		

	}

	public Object getPojo() {
		return pojo;
	}

	public void setPojo(Object pojo) {
		this.pojo = pojo;
		if (pojoDS != null) {
			pojoDS.setPojoObject(pojo);
		}
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

}

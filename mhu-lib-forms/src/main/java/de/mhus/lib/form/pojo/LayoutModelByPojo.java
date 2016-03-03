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

/**
 * <p>LayoutModelByPojo class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class LayoutModelByPojo extends LayoutModelByConfig {

	private Object pojo;
	private String modelName = "";
	private PojoDataSource pojoDS;
	private PackagingDataSource ds;
	
	/**
	 * <p>Constructor for LayoutModelByPojo.</p>
	 */
	public LayoutModelByPojo() {
		
	}
	
	/**
	 * <p>Constructor for LayoutModelByPojo.</p>
	 *
	 * @param pojo a {@link java.lang.Object} object.
	 */
	public LayoutModelByPojo(Object pojo) {
		setPojo(pojo);
	}

	/** {@inheritDoc} */
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

	/**
	 * <p>Getter for the field <code>pojo</code>.</p>
	 *
	 * @return a {@link java.lang.Object} object.
	 */
	public Object getPojo() {
		return pojo;
	}

	/**
	 * <p>Setter for the field <code>pojo</code>.</p>
	 *
	 * @param pojo a {@link java.lang.Object} object.
	 */
	public void setPojo(Object pojo) {
		this.pojo = pojo;
		if (pojoDS != null) {
			pojoDS.setPojoObject(pojo);
		}
	}

	/**
	 * <p>Getter for the field <code>modelName</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getModelName() {
		return modelName;
	}

	/**
	 * <p>Setter for the field <code>modelName</code>.</p>
	 *
	 * @param modelName a {@link java.lang.String} object.
	 */
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

}

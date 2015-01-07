package de.mhus.lib.form;

import de.mhus.lib.core.lang.MObject;
import de.mhus.lib.core.util.MNls;

public abstract class LayoutModel extends MObject {

	protected LayoutRoot root;
	private MNls nls;
	private DataSource dataSource;
	private FormControl formControl;
	private LayoutFactory formFactory;

	public LayoutRoot getModelRoot() {
		return root;
	}

	public MNls getNls() {
		return nls;
	}

	public abstract void doBuild() throws Exception;
	
	public LayoutRoot getRoot() {
		return root;
	}

	public void setNls(MNls nls) {
		this.nls = nls;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public FormControl getFormControl() {
		return formControl;
	}

	public void setFormControl(FormControl formControl) {
		this.formControl = formControl;
	}

	public LayoutFactory getFormFactory() {
		return formFactory;
	}

	public void setFormFactory(LayoutFactory formFactory) {
		this.formFactory = formFactory;
	}

}

package de.mhus.lib.form;

import de.mhus.lib.core.lang.MObject;
import de.mhus.lib.core.util.MNls;

/**
 * <p>Abstract LayoutModel class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public abstract class LayoutModel extends MObject {

	protected LayoutRoot root;
	private MNls nls;
	private DataSource dataSource;
	private FormControl formControl;
	private LayoutFactory formFactory;

	/**
	 * <p>getModelRoot.</p>
	 *
	 * @return a {@link de.mhus.lib.form.LayoutRoot} object.
	 */
	public LayoutRoot getModelRoot() {
		return root;
	}

	/**
	 * <p>Getter for the field <code>nls</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.core.util.MNls} object.
	 */
	public MNls getNls() {
		return nls;
	}

	/**
	 * <p>doBuild.</p>
	 *
	 * @throws java.lang.Exception if any.
	 */
	public abstract void doBuild() throws Exception;
	
	/**
	 * <p>Getter for the field <code>root</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.form.LayoutRoot} object.
	 */
	public LayoutRoot getRoot() {
		return root;
	}

	/**
	 * <p>Setter for the field <code>nls</code>.</p>
	 *
	 * @param nls a {@link de.mhus.lib.core.util.MNls} object.
	 */
	public void setNls(MNls nls) {
		this.nls = nls;
	}

	/**
	 * <p>Getter for the field <code>dataSource</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.form.DataSource} object.
	 */
	public DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * <p>Setter for the field <code>dataSource</code>.</p>
	 *
	 * @param dataSource a {@link de.mhus.lib.form.DataSource} object.
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * <p>Getter for the field <code>formControl</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.form.FormControl} object.
	 */
	public FormControl getFormControl() {
		return formControl;
	}

	/**
	 * <p>Setter for the field <code>formControl</code>.</p>
	 *
	 * @param formControl a {@link de.mhus.lib.form.FormControl} object.
	 */
	public void setFormControl(FormControl formControl) {
		this.formControl = formControl;
	}

	/**
	 * <p>Getter for the field <code>formFactory</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.form.LayoutFactory} object.
	 */
	public LayoutFactory getFormFactory() {
		return formFactory;
	}

	/**
	 * <p>Setter for the field <code>formFactory</code>.</p>
	 *
	 * @param formFactory a {@link de.mhus.lib.form.LayoutFactory} object.
	 */
	public void setFormFactory(LayoutFactory formFactory) {
		this.formFactory = formFactory;
	}

}

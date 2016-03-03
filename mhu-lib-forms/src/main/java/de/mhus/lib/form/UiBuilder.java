package de.mhus.lib.form;

import de.mhus.lib.core.activator.MutableActivator;
import de.mhus.lib.core.form.IUiBuilder;
import de.mhus.lib.core.lang.MObject;
import de.mhus.lib.errors.MException;

/**
 * <p>Abstract UiBuilder class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public abstract class UiBuilder extends MObject implements IUiBuilder {

	private LayoutRoot root;
	private LayoutFactory formFactory;
	
	/**
	 * <p>Setter for the field <code>root</code>.</p>
	 *
	 * @param root a {@link de.mhus.lib.form.LayoutRoot} object.
	 */
	public void setRoot(LayoutRoot root) {
		this.root = root;
	}
	
	/**
	 * <p>Getter for the field <code>root</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.form.LayoutRoot} object.
	 */
	public LayoutRoot getRoot() {
		return root;
	}

	/** {@inheritDoc} */
	@Override
	public void doBuild() throws Exception {
		root.build((IUiBuilder)this);
	}
	
	/**
	 * <p>initActivator.</p>
	 *
	 * @param actovator a {@link de.mhus.lib.core.activator.MutableActivator} object.
	 */
	public abstract void initActivator(MutableActivator actovator);

	/**
	 * <p>createCompositStart.</p>
	 *
	 * @param composite a {@link de.mhus.lib.form.LayoutComposite} object.
	 */
	public abstract void createCompositStart(LayoutComposite composite);

	/**
	 * <p>createCompositStop.</p>
	 *
	 * @param composite a {@link de.mhus.lib.form.LayoutComposite} object.
	 */
	public abstract void createCompositStop(LayoutComposite composite);

	/**
	 * <p>createRootStart.</p>
	 *
	 * @param root a {@link de.mhus.lib.form.LayoutRoot} object.
	 */
	public abstract void createRootStart(LayoutRoot root);

	/**
	 * <p>createRootStop.</p>
	 *
	 * @param root a {@link de.mhus.lib.form.LayoutRoot} object.
	 */
	public abstract void createRootStop(LayoutRoot root);

	/**
	 * <p>createSimpleElement.</p>
	 *
	 * @param element a {@link de.mhus.lib.form.LayoutElement} object.
	 */
	public abstract void createSimpleElement(LayoutElement element);

	/**
	 * <p>createDataElement.</p>
	 *
	 * @param element a {@link de.mhus.lib.form.LayoutDataElement} object.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	public abstract void createDataElement(LayoutDataElement element) throws MException;

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
		if (formFactory != null && formFactory.getActivator() instanceof MutableActivator) initActivator((MutableActivator)formFactory.getActivator());
	}
		
}

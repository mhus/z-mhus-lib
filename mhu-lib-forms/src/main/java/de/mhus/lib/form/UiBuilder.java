package de.mhus.lib.form;

import de.mhus.lib.core.activator.MutableActivator;
import de.mhus.lib.core.form.IUiBuilder;
import de.mhus.lib.core.lang.MObject;
import de.mhus.lib.errors.MException;

public abstract class UiBuilder extends MObject implements IUiBuilder {

	private LayoutRoot root;
	private LayoutFactory formFactory;
	
	public void setRoot(LayoutRoot root) {
		this.root = root;
	}
	
	public LayoutRoot getRoot() {
		return root;
	}

	@Override
	public void doBuild() throws Exception {
		root.build((IUiBuilder)this);
	}
	
	public abstract void initActivator(MutableActivator actovator);

	public abstract void createCompositStart(LayoutComposite composite);

	public abstract void createCompositStop(LayoutComposite composite);

	public abstract void createRootStart(LayoutRoot root);

	public abstract void createRootStop(LayoutRoot root);

	public abstract void createSimpleElement(LayoutElement element);

	public abstract void createDataElement(LayoutDataElement element) throws MException;

	public LayoutFactory getFormFactory() {
		return formFactory;
	}

	public void setFormFactory(LayoutFactory formFactory) {
		this.formFactory = formFactory;
		if (formFactory != null && formFactory.getActivator() instanceof MutableActivator) initActivator((MutableActivator)formFactory.getActivator());
	}
		
}

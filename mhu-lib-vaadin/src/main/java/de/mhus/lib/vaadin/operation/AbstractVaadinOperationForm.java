package de.mhus.lib.vaadin.operation;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.definition.DefRoot;
import de.mhus.lib.core.util.MNlsBundle;
import de.mhus.lib.form.MForm;
import de.mhus.lib.form.PropertiesDataSource;
import de.mhus.lib.vaadin.form.VaadinForm;

public abstract class AbstractVaadinOperationForm extends AbstractVaadinOperationEditor {

	private static final long serialVersionUID = 1L;
	protected AbstractVaadinOperation operation;
	protected VaadinForm model;
	private PropertiesDataSource dataSource;

	public AbstractVaadinOperationForm(AbstractVaadinOperation operation) {
		this.operation = operation;
	}
	
	@Override
	protected void initUI() {
		model = createForm();
		model.setShowInformation(true);
        if (model.getForm().getNlsBundle() == null)
        	model.getForm().setNlsBundle(MNlsBundle.lookup(this));
//        model.doBuild(getActivator());
        try {
			model.doBuild();
			this.addComponent(model);
		} catch (Exception e) {
			log.e(e);
		}

	}

	protected VaadinForm createForm() {
		try {
			DefRoot formDef = operation.getDescription().getForm();
			VaadinForm form = new VaadinForm();
			form.setForm( new MForm( formDef ) );
			dataSource = new PropertiesDataSource();
			initDataSource(dataSource);
			if (dataSource.getProperties() == null) dataSource.setProperties(new MProperties());
			form.getForm().setDataSource(dataSource);
			return form;
		} catch (Throwable t) {
			log.w(t);
		}
		return null;
	}

	protected abstract void initDataSource(PropertiesDataSource ds);

	@Override
	public void fillOperationParameters(IProperties param) {
		param.putAll(dataSource.getProperties());
	}

}

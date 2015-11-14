package de.mhus.lib.vaadin.form2;

import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.GridLayout;

import de.mhus.lib.core.activator.MutableActivator;
import de.mhus.lib.errors.MException;
import de.mhus.lib.form.LayoutBuilderWithStack;
import de.mhus.lib.form.LayoutComposite;
import de.mhus.lib.form.LayoutDataElement;
import de.mhus.lib.form.LayoutElement;
import de.mhus.lib.form.LayoutRoot;
import de.mhus.lib.form.UiComponent;
import de.mhus.lib.form.ui.FmText;

public class VaadinFormBuilder extends LayoutBuilderWithStack<UiVaadinComposite> {

	private GridLayout rootComposit;
	private ComponentContainer informationPane;

	@Override
	public void createCompositStart(LayoutComposite composite) {
		log().d("createCompositStart", composite);
		
//		if (composite.getConfig().getName().equals("split")) return; // split is transparent - no separate grid
		UiVaadinComposite ui = (UiVaadinComposite) composite.getUi();
				
		if (ui == null) {
			ui = new UiVaadinComposite();
			composite.setUi(ui);
		}
			
		ui.createUi(this);
					
		if (ui.isTransparent()) return;
		
		push(ui);
	}

	public void addComposite(LayoutComposite composite,Component layout) {
		
		if (currentComponent == null) return;
		
		int row = 0;
		
		if (composite.getOffset() > 0 && currentComponent.getGrid() != null) {
			row = currentComponent.getGrid().getRows()-1;
		} else {
			row = currentComponent.createRow();
		}
		log().d("add comp",composite.getOffset(),row,composite.getOffset()+composite.getColumns()-1,row);
		currentComponent.addComponent(composite,layout,composite.getOffset(),row,composite.getOffset()+composite.getColumns()-1,row);
		
	}
	
	@Override
	public void createCompositStop(LayoutComposite composite) {
		try {
			log().d("createCompositStop", composite.getConfig().getName());
		} catch (MException e) {
			log().w(e);
		}

		UiComponent ui = composite.getUi();
		if (ui != null) {
			if ( ((UiVaadinComposite)ui).isTransparent()) return;
		}
//		if (composite.getConfig().getName().equals("split")) return;
		
		pop();
	}

	@Override
	public void createRootStart(LayoutRoot root) {
		UiVaadinComposite ui = new UiVaadinComposite();
		root.setUi(ui);
		ui.createUi(this);
		rootComposit = ui.getGrid();
		push(ui);
	}

	@Override
	public void createRootStop(LayoutRoot root) {
		pop();
	}

	@Override
	public void createSimpleElement(LayoutElement element) {
		
	}

	@Override
	public void createDataElement(LayoutDataElement element) throws MException {
		log().d("createDataElement", element);
		UiVaadin ui = (UiVaadin)element.getUi();
		ui.createUi(this);
	}

	@Override
	public void initActivator(MutableActivator act) {

		act.addMap(UiComponent.class,"text", UiText.class);
		act.addMap(UiComponent.class,"password", UiPassword.class);
		act.addMap(UiComponent.class,"richtext", UiRichText.class);
		act.addMap(UiComponent.class,"textarea", UiTextArea.class);
		act.addMap(UiComponent.class,"checkbox", UiCheckbox.class);
		act.addMap(UiComponent.class,"combobox", UiCombobox.class);
		act.addMap(UiComponent.class,"date", UiDate.class);
		act.addMap(UiComponent.class,"number", UiNumber.class);

		act.addMap(UiComponent.class,"information", UiInformation.class);
		
		act.addMap(LayoutElement.class,"composite", UiVaadinComposite.class);
		act.addMap(LayoutElement.class,"split", UiVaadinSplit.class);
		act.addMap(LayoutElement.class,"actions", UiVaadinActions.class);
		
		act.addMap(UiComponent.class,"group", UiGroup.class);
		act.addMap(UiComponent.class,"tabsheet", UiTabsheet.class);
		act.addMap(UiComponent.class,"accordion", UiAccordion.class);
		
		act.addMap(UiComponent.class,"root", UiVaadinComposite.class);
		
	}

	public GridLayout getRootComposit() {
		return rootComposit;
	}
	
	public UiVaadinComposite getCurrentComposite() {
		return currentComponent;
	}

	public ComponentContainer getInformationPane() {
		return this.informationPane;
	}

	public void setInformationPane(ComponentContainer informationPane) {
		this.informationPane = informationPane;
	}
	
}

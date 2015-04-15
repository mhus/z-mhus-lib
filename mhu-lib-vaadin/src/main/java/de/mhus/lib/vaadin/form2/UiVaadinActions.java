package de.mhus.lib.vaadin.form2;


import com.vaadin.event.FieldEvents;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.VerticalLayout;

import de.mhus.lib.form.FormAction;
import de.mhus.lib.form.LayoutActions;
import de.mhus.lib.form.UiAction;
import de.mhus.lib.vaadin.aqua.PopupButton;

public class UiVaadinActions extends UiVaadinComposite {

	PopupButton secButton = null;
	public VerticalLayout secLayout;
	
	@Override
	public void createUi(VaadinFormBuilder builder) {
		
		int row = builder.getCurrentComposite().createRow();

		for (FormAction a : ((LayoutActions)getElement()).getActions()) {
			new UiButton(a,builder,row);
		}
	}

	@Override
	public boolean isTransparent() {
		return true;
	}

	private class UiButton extends UiAction {
		//TODO separate primary, close and other
		private FormAction a;
		private Button button;

		@SuppressWarnings("deprecation")
		public UiButton(FormAction action, VaadinFormBuilder builder, int row) {
			this.a = action;
			a.setUi(this);
			
			button = new Button(a.getTitle());
			button.setWidth("100%");
			button.setEnabled(a.isEnabled());
			button.addListener(new Button.ClickListener() {
				
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					getElement().getFormControl().action(a);
				}
			});
			button.addListener(new FieldEvents.FocusListener() {

				private static final long serialVersionUID = 1L;

				@Override
				public void focus(FocusEvent event) {
					getElement().getFormControl().focused(getElement());
				}
				
			});
			
			log().i("button",a.getOffset(),row,a.getOffset()+a.getColumns()-1,row);
			
			if (action.isSecondary()) {
				if (secButton == null) {
					secButton = new PopupButton(getElement().find("secondary"));
					builder.getCurrentComposite().addComponent(getElement(), secButton,a.getOffset(),row,a.getOffset()+a.getColumns()-1,row);
					secLayout = new VerticalLayout();
					secLayout.setWidth("200px");
					secButton.addComponent(secLayout);
				}
				secLayout.addComponent(button);
			} else {
				builder.getCurrentComposite().addComponent(getElement(), button,a.getOffset(),row,a.getOffset()+a.getColumns()-1,row);
			}
		}
		
		@Override
		public void doUpdateEnabled() {
			button.setEnabled(a.isEnabled());
		}

		
	}
}

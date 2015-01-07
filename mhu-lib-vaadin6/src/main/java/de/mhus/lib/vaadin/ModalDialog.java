package de.mhus.lib.vaadin;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public abstract class ModalDialog extends Window {

	private static final long serialVersionUID = 1L;

	public final static Action CLOSE = new CloseAction("close", "Close");
	public final static Action OK = new Action("ok", "OK");
	
	protected Action[] actions = new Action[] {OK,CLOSE};
	protected HorizontalLayout buttonBar;
	
	public void show(Window ui) throws Exception {
		ui.addWindow(this);
	}

	protected void initUI() throws Exception {
		setModal(true);
		
		VerticalLayout layout = new VerticalLayout();
		setContent(layout);
		
        layout.setMargin(true);
        layout.setSpacing(true);

        setWidth("650px");
        //setHeight("600px");
        
        initContent(layout);
        
        buttonBar = new HorizontalLayout();
        buttonBar.setSpacing(true);
        updateButtons();
        
        layout.addComponent(buttonBar);
        layout.setComponentAlignment(buttonBar, Alignment.MIDDLE_RIGHT);
        
        
	}

	
	protected abstract void initContent(VerticalLayout layout) throws Exception;

	protected void updateButtons() {
		buttonBar.removeAllComponents();
		for (final Action a : actions) {
			Button b = new Button();
			b.setCaption(a.title);
			b.addListener(new ClickListener() {
				
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					a.doAction(ModalDialog.this);
				}
			});
			buttonBar.addComponent(b);
		}
	}


	/**
	 * 
	 * @param action
	 * @return true if the dialog should close
	 */
	protected abstract boolean doAction(Action action);

	public static class Action {

		private String id;
		private String title;

		public Action(String id, String title) {
			this.id = id;
			this.title = title;
		}

		public String getTitle() {
			return title;
		}
		
		public boolean equals(Object in) {
			if (in == null) return false;
			if (in instanceof Action) {
				return ((Action)in).id.equals(id);
			}
			return super.equals(in);
		}
		
		public String toString() {
			return id;
		}
		
		public void doAction(ModalDialog dialog) {
			if (dialog.doAction(this))
				dialog.close();
		}
		
	}
	
	public static class CloseAction extends Action {

		public CloseAction(String id, String title) {
			super(id, title);
		}
		
		public void doAction(ModalDialog dialog) {
				dialog.close();
		}
		
	}
	
}

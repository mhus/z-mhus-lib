package de.mhus.lib.vaadin;

import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class ConfirmDialog extends ModalDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Action confirm;
	private Action cancel;
	private String message;
	private Action result;
	private Listener listener;
	protected Label label;

	public ConfirmDialog(String title, String message, String txtConfirm, String txtCancel, Listener listener) throws Exception {

		this.message = message;
		this.listener = listener;
		confirm = new Action("confirm", txtConfirm);
		cancel = new Action("cancel", txtCancel);
		actions = new Action[] {confirm,cancel};
		initUI();
		setCaption(title);
		
	}
	
	public Label getLabel() {
		return label;
	}
	
	@Override
	protected void initContent(VerticalLayout layout) throws Exception {
		label = new Label(message);
		label.setContentMode(Label.CONTENT_XHTML);
		layout.addComponent(label);
	}

	@Override
	protected boolean doAction(Action action) {
		result = action;
		if (listener != null) listener.onClose(this);
		return true;
	}

	public static void show(Window ui, String title, String message, String txtConfirm, String txtCancel, Listener listener) {
		try {
			new ConfirmDialog(title,message,txtConfirm,txtCancel,listener).show(ui);
		} catch (Exception e) {
		}
	}

	public static interface Listener {

		public void onClose(ConfirmDialog dialog);
		
	}

	public boolean isConfirmed() {
		return result == confirm;
	}
	
	public boolean isCancel() {
		return result == cancel;
	}
	
}

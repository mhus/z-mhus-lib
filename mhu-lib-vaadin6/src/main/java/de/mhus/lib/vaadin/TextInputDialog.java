package de.mhus.lib.vaadin;

import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class TextInputDialog extends ModalDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;
	private Listener listener;
	private Action confirm;
	private Action cancel;
	protected Label label;
	private TextField textField;
	private String txtInput;
	private Action result;

	public TextInputDialog(String title, String message, String txtInput, String txtConfirm, String txtCancel, Listener listener) throws Exception {

		this.message = message;
		this.listener = listener;
		this.txtInput = txtInput;
		confirm = new Action("confirm", txtConfirm);
		cancel = new Action("cancel", txtCancel);
		actions = new Action[] {confirm,cancel};
		initUI();
		setCaption(title);
		
	}
	
	public Label getLabel() {
		return label;
	}
	
	public TextField getTextField() {
		return textField;
	}
	
	@Override
	protected void initContent(VerticalLayout layout) throws Exception {
		label = new Label(message);
		label.setContentMode(Label.CONTENT_XHTML);
		layout.addComponent(label);
		
		textField = new TextField();
		textField.setValue(txtInput);
		textField.setWidth("100%");
		textField.focus();
		
		textField.addShortcutListener(new ShortcutListener("Confirm", ShortcutAction.KeyCode.ENTER, null) {
			private static final long serialVersionUID = 1L;

				@Override
			    public void handleAction(Object sender, Object target) {
			    	confirm.doAction(TextInputDialog.this);
			    }
			});
		textField.setImmediate(true);
		
		layout.addComponent(textField);
		txtInput = null;
	}

	@Override
	protected boolean doAction(Action action) {
		result = action;
		if (action.equals(confirm)) txtInput = (String) textField.getValue();
		if (listener != null) {
			if (action.equals(confirm) && !listener.validate(txtInput))
				return false;
			listener.onClose(this);
		}
		return true;
	}

	public static void show(Window parent, String title, String txtInput, String message, String txtConfirm, String txtCancel, Listener listener) {
		try {
//			if (parent == null) parent = UI.getCurrent();
			new TextInputDialog(title,message,txtInput,txtConfirm,txtCancel,listener).show(parent);
		} catch (Exception e) {
		}
	}

	public static interface Listener {

		public boolean validate(String txtInput);
		
		public void onClose(TextInputDialog dialog);
		
	}

	public boolean isConfirmed() {
		return result == confirm;
	}
	
	public boolean isCancel() {
		return result == cancel;
	}

	public String getInputText() {
		return txtInput;
	}

}

package de.mhus.lib.vaadin.aqua;

import com.vaadin.terminal.Resource;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;

import de.mhus.lib.core.directory.ResourceNode;

@SuppressWarnings("unchecked")
public class DefaultLogin extends ActionButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HorizontalLayout container;
	@SuppressWarnings("unused")
	private Desktop desktop;

	public Component getComponent() {
		container = new HorizontalLayout();
		container.setSpacing(true);
		container.setMargin(true);
		container.addComponent(new Label("User"));
		container.addComponent(new TextField());
		container.addComponent(new Label("Password"));
		container.addComponent(new PasswordField());
		return container;
	}

	public void setDesktop(Desktop desktop) {
		this.desktop = desktop;
	}

	@SuppressWarnings("serial")
	@Override
	public void setConfig(ResourceNode config) {
		Resource res = new ThemeResource("icons/on-off-16.png");
//		setCaption("L");
		setIcon(res);
		getWindow();
		addListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if (event.isAltKey()) {
					getWindow().detach(); //?
				} else
				if (workArea != null) {
					if (workArea.isOwner(DefaultLogin.this))
						workArea.setComponent(null, null);
					else
						workArea.setComponent(DefaultLogin.this, container);
				}
			}
		});
	}

}

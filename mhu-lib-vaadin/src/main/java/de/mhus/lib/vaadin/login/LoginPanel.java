/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.vaadin.login;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import de.mhus.lib.core.util.MNls;
import de.mhus.lib.core.util.MNlsProvider;
import de.mhus.lib.vaadin.ui.HelpManager;

@SuppressWarnings("deprecation")
public class LoginPanel extends VerticalLayout implements MNlsProvider {

	private static final long serialVersionUID = 1L;
	private HelpManager helpManager;
	private Listener listener;
	private MNls nls;
	private Label error;
	
	public LoginPanel() {
		addAttachListener(new AttachListener() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void attach(AttachEvent event) {
				doContent();
			}
		});
	}
	
	protected void doContent() {
				
		helpManager = new HelpManager(getUI());
        helpManager.closeAll();
        
        
		String welcomeTxt = MNls.find(this, "login.help.title");
		if (welcomeTxt != null) {
			helpManager.showHelp(
							welcomeTxt,
	                		MNls.find(this, "login.help.description"),
	                        "login");
		}
		
        addStyleName("login");

        VerticalLayout loginLayout = this;
        loginLayout.setSizeFull();
        loginLayout.addStyleName("login-layout");
        loginLayout.setWidth("450px");

        final VerticalLayout loginPanel = new VerticalLayout();
        loginPanel.addStyleName("login-panel");

        HorizontalLayout labels = new HorizontalLayout();
        labels.setWidth("100%");
        labels.setMargin(true);
        labels.addStyleName("labels");
        loginPanel.addComponent(labels);

        Label welcome = new Label(MNls.find(this, "login.welcome=Welcome"));
        welcome.setSizeUndefined();
        welcome.addStyleName("h4");
        labels.addComponent(welcome);
        labels.setComponentAlignment(welcome, Alignment.MIDDLE_LEFT);

        Label title = new Label(MNls.find(this, "login.title="));
        title.setSizeUndefined();
        title.addStyleName("h2");
        title.addStyleName("light");
        labels.addComponent(title);
        labels.setComponentAlignment(title, Alignment.MIDDLE_RIGHT);

        HorizontalLayout fields = new HorizontalLayout();
        fields.setSpacing(true);
        fields.setMargin(true);
        fields.addStyleName("fields");

        final TextField username = new TextField(MNls.find(this, "login.username=Username"));
        username.focus();
        fields.addComponent(username);

        final PasswordField password = new PasswordField(MNls.find(this, "login.password=Password"));
        fields.addComponent(password);

        final Button signin = new Button(MNls.find(this, "login.signin=Sign In"));
        signin.addStyleName("default");
        fields.addComponent(signin);
        fields.setComponentAlignment(signin, Alignment.BOTTOM_LEFT);

        error = new Label("",ContentMode.HTML);
        error.addStyleName("error");
        error.setSizeUndefined();
        error.addStyleName("light");
        // Add animation
        error.addStyleName("v-animate-reveal");
        loginPanel.addComponent(error);
        loginPanel.setComponentAlignment(error, Alignment.MIDDLE_CENTER);

        final ShortcutListener enter = new ShortcutListener(MNls.find(this, "login.signin=Sign In"),
                KeyCode.ENTER, null) {
					private static final long serialVersionUID = 1L;

			@Override
            public void handleAction(Object sender, Object target) {
                signin.click();
            }
        };

        signin.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
            public void buttonClick(ClickEvent event) {
            	
            	if ("".equals(password.getValue())) {
            		password.focus();
            		return;
            	}
            	
                error.setCaption("");
                
                if (listener != null && listener.doLogin(username.getValue(),password.getValue())) {
                    signin.removeShortcutListener(enter);
                } else {
                    // Add new error message
                    error.setCaption(MNls.find(LoginPanel.this, "login.error=Wrong username or password."));
                    username.focus();
                }
            }
        });

        signin.addShortcutListener(enter);

        loginPanel.addComponent(fields);

        loginLayout.addComponent(loginPanel);
        loginLayout.setComponentAlignment(loginPanel, Alignment.MIDDLE_CENTER);
		
        doCustomize(loginPanel, labels, fields);
	}
	
	protected void doCustomize(VerticalLayout loginPanel, HorizontalLayout labels,
			HorizontalLayout fields) {
		
	}

	public Listener getListener() {
		return listener;
	}

	public void setListener(Listener listener) {
		this.listener = listener;
	}

	@Override
	public MNls getNls() {
		return nls;
	}

	public void setNls(MNls nls) {
		this.nls = nls;
	}

	public static interface Listener {

		public boolean doLogin(String username, String password);
		
	}
}

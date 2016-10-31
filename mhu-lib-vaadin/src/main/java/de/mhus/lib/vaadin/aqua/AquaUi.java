package de.mhus.lib.vaadin.aqua;

import java.util.HashMap;
import java.util.TreeMap;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import de.mhus.lib.core.cfg.CfgString;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.security.AccessControl;
import de.mhus.lib.vaadin.VaadinAccessControl;

public abstract class AquaUi extends UI implements AquaApi {

	private static Log log = Log.getLog(AquaUi.class);
	private static CfgString realm = new CfgString(AquaUi.class, "realm", "karaf");
	private MenuBar menuBar;
	private AccessControl accessControl;
	protected Desktop desktop;
	protected TreeMap<String,AquaSpace> spaceList = new TreeMap<String, AquaSpace>();
	protected HashMap<String, AbstractComponent> spaceInstanceList = new HashMap<String, AbstractComponent>(); 

	
	@Override
	protected void init(VaadinRequest request) {
		VerticalLayout content = new VerticalLayout();
		setContent(content);
		content.setSizeFull();
        content.addStyleName("view-content");
        content.setMargin(true);
        content.setSpacing(true);

        accessControl = new VaadinAccessControl(realm.value());

        if (!accessControl.isUserSignedIn()) {
            setContent(new LoginScreen(accessControl, new LoginScreen.LoginListener() {
                @Override
                public void loginSuccessful() {
                    showMainView();
                }
            }));
        } else {
            showMainView();
        }

	}

	private void showMainView() {
        addStyleName(ValoTheme.UI_WITH_MENU);
        desktop = new Desktop(this);
        setContent(desktop);
		synchronized (this) {
			desktop.refreshSpaceList(spaceList);
		}
	}

	public AbstractComponent getSpaceComponent(String name) {
		AquaSpace space = spaceList.get(name);
		if (space == null) return null;
		AbstractComponent instance = spaceInstanceList.get(name);
		if (instance == null) {
			instance = space.createSpace();
			if (instance == null) return null;
			if (instance instanceof AquaLifecycle) ((AquaLifecycle)instance).doInitialize();
			spaceInstanceList.put(name, instance);
		}
		return instance;
	}

	public AquaSpace getSpace(String name) {
		return spaceList.get(name);
	}
	
	public AccessControl getAccessControl() {
		return accessControl;
	}

	public void removeSpaceComponent(String name) {
		AbstractComponent c = spaceInstanceList.remove(name);
		if (c != null && c instanceof AquaLifecycle) ((AquaLifecycle)c).doDestroy();
	}

	@Override
	public boolean openSpace(String spaceId, String subSpace, String search) {
		AquaSpace space = getSpace(spaceId);
		if (space == null) return false;
		desktop.showSpace(space, subSpace, search);
		
		return true;
	}
	
	@Override
	public boolean hasSpace(String spaceId, String subSpace) {
		AquaSpace space = getSpace(spaceId);
		if (space == null) return false;
		desktop.hasSpace(space, subSpace);
		
		return true;
	}
	
}

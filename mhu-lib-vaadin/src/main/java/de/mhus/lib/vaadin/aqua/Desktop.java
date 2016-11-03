package de.mhus.lib.vaadin.aqua;

import java.util.LinkedList;
import java.util.Map;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import de.mhus.lib.core.MString;
import de.mhus.lib.core.logging.Log;

public class Desktop extends CssLayout {

	private AquaUi ui;
	private MenuBar menuBar;
	private MenuItem menuSpaces;
	private VerticalLayout contentScreen;
	private MenuItem menuCurrent;
	private MenuItem menuLeave;
	private MenuItem menuUser;
	private MenuItem menuLogout;
	protected AquaSpace currentSpace;
	private MenuItem menuOverview;
	private GridLayout overView;
	private static Log log = Log.getLog(Desktop.class);

	public Desktop(AquaUi aquaUi) {
		ui = aquaUi;
		initGui();
	}

	private void initGui() {
		
		overView = new GridLayout();
		overView.setSizeFull();
		overView.setMargin(true);
		overView.setSpacing(true);
		overView.setStyleName("overview");
		
		menuBar = new MenuBar();
		menuSpaces = menuBar.addItem("Bereiche", null);

		menuCurrent = menuBar.addItem("", null);
		
		menuUser = menuBar.addItem( ui.getAccessControl().getName(), null);
		menuUser.setStyleName("right");
		menuLogout = menuUser.addItem("Logout", new MenuBar.Command() {
			
			@Override
			public void menuSelected(MenuItem selectedItem) {
				try {
					ui.getAccessControl().signOut();
				} catch (Throwable t) {
					log.d(t);
				}
				try {
					UI.getCurrent().close();
				} catch (Throwable t) {
					log.d(t);
				}
				UI.getCurrent().getPage().reload();
			}
		});
		
		setStyleName("desktop-screen");
		menuBar.setStyleName("menubar");
		
		addComponent(menuBar);
		
		contentScreen = new VerticalLayout();
		contentScreen.addStyleName("content");
		contentScreen.setSizeFull();
		addComponent(contentScreen);
		setSizeFull();
		
		showOverview();
	}

	public void refreshSpaceList(Map<String, AquaSpace> spaceList) {
		menuSpaces.removeChildren();
		overView.removeAllComponents();
		
		menuOverview = menuSpaces.addItem("Übersicht", new MenuBar.Command() {
			
			@Override
			public void menuSelected(MenuItem selectedItem) {
				showOverview();
			}
		});

		menuSpaces.addSeparator();
		
		LinkedList<AquaSpace> componentList = new LinkedList<>();
		for (AquaSpace space : spaceList.values()) {
			
			if (!space.hasAccess(ui.getAccessControl())) continue;
			componentList.add(space);
		}
		

		overView.setColumns(Math.max(1, (int)Math.sqrt( componentList.size() ) ) );
		overView.setRows( overView.getColumns() );

		for (final AquaSpace space : componentList ) {
			NativeButton button = new NativeButton();
			button.setHtmlContentAllowed(false);
			button.setCaption( space.getDisplayName());
			button.setStyleName("thumbnail");
			overView.addComponent(button);
			button.addClickListener(new NativeButton.ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					showSpace(space, null, null);
				}
			});
			
			MenuItem item = menuSpaces.addItem(space.getDisplayName(), new MenuBar.Command() {
				
				@Override
				public void menuSelected(MenuItem selectedItem) {
					showSpace(space, null, null);
				}
			});
			item.setEnabled(true);
		}
		
		if (componentList.size() > 0)
			menuSpaces.addSeparator();
		
		menuLeave = menuSpaces.addItem("Verlassen", new MenuBar.Command() {
			
			@Override
			public void menuSelected(MenuItem selectedItem) {
				if (currentSpace == null) return;
				ui.removeSpaceComponent(currentSpace.getName());
				currentSpace = null;
				showOverview();
			}
		});
		menuLeave.setEnabled(false);

	}

	protected void showSpace(AquaSpace space, String subSpace, String search) {
		AbstractComponent component = ui.getSpaceComponent(space.getName());
		
		contentScreen.removeAllComponents();
		menuCurrent.removeChildren();
		
		if (component == null) {
			contentScreen.addComponent(new Label("Der Space ist aktuell nicht erreichbar " + space.getName()));
			addComponent(contentScreen);
			return;
		}
		
		component.setSizeFull();
		contentScreen.addComponent(component);
		
		menuCurrent.setText(space.getDisplayName());
		menuLeave.setEnabled(true);
		currentSpace = space;
		space.createMenu(menuCurrent);
		
		if (component instanceof Navigatable && (MString.isSet(subSpace) || MString.isSet(search)))
			((Navigatable)component).navigateTo(subSpace, search);
	}

	protected boolean hasSpace(AquaSpace space, String subSpace) {
		AbstractComponent component = ui.getSpaceComponent(space.getName());
		if (component == null) return false;

		if (component instanceof Navigatable && MString.isSet(subSpace) )
			return ((Navigatable)component).hasSubSpace(subSpace);
		
		return true;
	}
	
	protected void showOverview() {
		if (menuLeave != null) menuLeave.setEnabled(false);
		contentScreen.removeAllComponents();
		menuCurrent.setText("Übersicht");
		currentSpace = null;
		contentScreen.addComponent(overView);
	}

}

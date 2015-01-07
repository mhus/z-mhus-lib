package de.mhus.lib.vaadin.aqua;

import java.util.Observable;

import com.vaadin.ui.Component;

import de.mhus.lib.core.MActivator;
import de.mhus.lib.core.activator.MutableActivator;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.core.lang.Injector;
import de.mhus.lib.core.lang.InjectorMap;
import de.mhus.lib.core.lang.MObject;
import de.mhus.lib.core.util.WeakObservable;
import de.mhus.lib.vaadin.layouter.LayModel;
import de.mhus.lib.vaadin.layouter.LayoutBuilder;
import de.mhus.lib.vaadin.layouter.XLayElement;

public class Desktop extends MObject {

//	private Filesystem filesystem;
	private WeakObservable observable = new WeakObservable();
	private NavigationSelection selectedPath;
//	private UserManager userManager;
	private ContentArea contentArea;
	private LayModel model;

	public Desktop(ResourceNode layout) throws Exception {
		
		if (getActivator() instanceof MutableActivator) initDefaultActivator((MutableActivator)getActivator());

		InjectorMap injector = new InjectorMap();
		injector.put(DesktopInject.class, new Injector() {
			
			@Override
			public void doInject(Object obj) throws Exception {
				((DesktopInject)obj).setDesktop(Desktop.this);
			}
		});
		getActivator().addInjector(injector);
		model = new LayoutBuilder().doBuild(layout).getModel();
		
		//filesystem = loadModule(Filesystem.class);
		//userManager = loadModule(UserManager.class);
		
	}
	
	public static void initDefaultActivator(MutableActivator activator) {
		LayoutBuilder.initDefaultActivator(activator);
		activator.addMap(XLayElement.class, "header", DefaultHeader.class);
		activator.addMap(XLayElement.class, "navigation", DefaultNavigation.class);
		activator.addMap(XLayElement.class, "content", DefaultContentArea.class);
		activator.addMap(XLayElement.class, "breadcrumb", DefaultBreadcrumb.class);
		activator.addMap(XLayElement.class, "login", DefaultLogin.class);
		activator.addMap(XLayElement.class, "preview", PreviewContentPane.class);
		
	}

	public Component getRoot() {
		return model.getRoot();
	}
	
	@Deprecated
	public <T> T loadModule(Class<? extends T> clazz) throws Exception {
		T out = (T) getActivator().getObject(clazz,"");
		if (out instanceof DesktopInject)
			((DesktopInject) out).setDesktop(this);
		return out;
	}

	public MActivator getActivator() {
		return base(MActivator.class);
	}
	
	public void setSelectedPath(String pathAsString) {
//		NavigationNode path = filesystem.getPath(pathAsString);
//		setSelectedPath(path);
	}
	
	public void setSelectedPath(NavigationNode path) {
		if (!isSelectedPath(path)) {
			observable.setChanged();
			selectedPath = new NavigationSelection(path);
			observable.notifyObservers(selectedPath);
		}
	}

	public ContentArea getContentArea() throws Exception {
		return contentArea;
	}

	public boolean isSelectedPath(NavigationNode path) {
		return ! (selectedPath == null || !selectedPath.getSelectedNode().equals(path));
	}

	public void openNode(NavigationNode path) {
		try {
			path.doOpenNode(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String pathToString(NavigationNode path) {
		StringBuffer sb = new StringBuffer();
		NavigationNode cur = path;
		while (cur != null) {
			sb.insert(0, " > ");
			sb.insert(0, cur.getTitle());
			cur = cur.getParent();
		}
		return sb.toString();
	}

	public void setContentArea(ContentArea defaultContentArea) {
		contentArea = defaultContentArea;
	}
	
	public Observable getObserverable() {
		return observable;
	}

}

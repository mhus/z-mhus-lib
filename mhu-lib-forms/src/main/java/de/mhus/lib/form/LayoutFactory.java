package de.mhus.lib.form;

import de.mhus.lib.core.MActivator;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.core.lang.MObject;
import de.mhus.lib.errors.MException;

public class LayoutFactory extends MObject {

	public static final String VALIDATOR_NOT_EMPTY = "not_empty";
	public static final String ELEMENT = "element";
	public static final String ELEMENT_COMPOSITE = "composite";
	public static final String ELEMENT_SPLIT = "split";
	public static final String ELEMENT_OVERLAY = "overlay";
	public static final String ELEMENT_ACTIONS = "actions";
	private MActivator activator;
	
		
	public LayoutFactory() {
	}
		
	public LayoutElement doBuildChild(LayoutComposite parent,
			ResourceNode subConfig) throws Exception {
		LayoutElement out = null;
		if (subConfig.getName().equals(ELEMENT))
			out = new LayoutDataElement();
		else
		if (subConfig.getName().equals(ELEMENT_COMPOSITE))
			out = new LayoutComposite();
		else
		if (subConfig.getName().equals(ELEMENT_SPLIT))
			out = new LayoutSplit();
		else
		if (subConfig.getName().equals(ELEMENT_OVERLAY))
			out = new LayoutOverlay();
		else
		if (subConfig.getName().equals(ELEMENT_ACTIONS))
			out = new LayoutActions();
		else
			out = getActivator().createObject(LayoutElement.class,subConfig.getName());
		if (out != null) out.init(parent, subConfig);
		return out;
	}

	public void setActivator(MActivator acivator) {
		this.activator = acivator;
	}
	
	public MActivator getActivator() {
		if (activator == null)
			activator = base(MActivator.class);
		return activator;
	}

	public UiElement doBuildUi(LayoutElement element) throws Exception {
		try {
			UiElement out = null;
			if (MString.isEmpty(element.getType())) {
				try {
					out = getActivator().createObject(UiElement.class,element.getConfig().getName());
				} catch (ClassNotFoundException | InstantiationException cnf) {
					log().i(element.getConfig().getName(),cnf.toString());
				}
			} else {
				out = getActivator().createObject(UiElement.class,element.getType());
			}
			return out;
		} catch (Throwable t) {
			throw new MException("build failed", element.getConfig().getName(), element.getType(), t);
		}
	}
}

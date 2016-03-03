package de.mhus.lib.form;

import de.mhus.lib.core.MActivator;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.core.lang.MObject;
import de.mhus.lib.errors.MException;

/**
 * <p>LayoutFactory class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class LayoutFactory extends MObject {

	/** Constant <code>VALIDATOR_NOT_EMPTY="not_empty"</code> */
	public static final String VALIDATOR_NOT_EMPTY = "not_empty";
	/** Constant <code>ELEMENT="element"</code> */
	public static final String ELEMENT = "element";
	/** Constant <code>ELEMENT_COMPOSITE="composite"</code> */
	public static final String ELEMENT_COMPOSITE = "composite";
	/** Constant <code>ELEMENT_SPLIT="split"</code> */
	public static final String ELEMENT_SPLIT = "split";
	/** Constant <code>ELEMENT_OVERLAY="overlay"</code> */
	public static final String ELEMENT_OVERLAY = "overlay";
	/** Constant <code>ELEMENT_ACTIONS="actions"</code> */
	public static final String ELEMENT_ACTIONS = "actions";
	private MActivator activator;
	
		
	/**
	 * <p>Constructor for LayoutFactory.</p>
	 */
	public LayoutFactory() {
	}
		
	/**
	 * <p>doBuildChild.</p>
	 *
	 * @param parent a {@link de.mhus.lib.form.LayoutComposite} object.
	 * @param subConfig a {@link de.mhus.lib.core.directory.ResourceNode} object.
	 * @return a {@link de.mhus.lib.form.LayoutElement} object.
	 * @throws java.lang.Exception if any.
	 */
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

	/**
	 * <p>Setter for the field <code>activator</code>.</p>
	 *
	 * @param acivator a {@link de.mhus.lib.core.MActivator} object.
	 * @since 3.2.9
	 */
	public void setActivator(MActivator acivator) {
		this.activator = acivator;
	}
	
	/**
	 * <p>Getter for the field <code>activator</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.core.MActivator} object.
	 */
	public MActivator getActivator() {
		if (activator == null)
			activator = base(MActivator.class);
		return activator;
	}

	/**
	 * <p>doBuildUi.</p>
	 *
	 * @param element a {@link de.mhus.lib.form.LayoutElement} object.
	 * @return a {@link de.mhus.lib.form.UiElement} object.
	 * @throws java.lang.Exception if any.
	 */
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

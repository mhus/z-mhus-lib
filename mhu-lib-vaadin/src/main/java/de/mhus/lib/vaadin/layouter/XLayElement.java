package de.mhus.lib.vaadin.layouter;

import com.vaadin.ui.Component;

import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.errors.MException;


public interface XLayElement extends Component {

	void setConfig(ResourceNode config) throws MException;

	void doAppendChild(XLayElement child, ResourceNode cChild) throws MException;

}

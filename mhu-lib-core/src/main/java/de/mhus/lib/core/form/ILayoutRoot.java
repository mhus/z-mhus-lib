package de.mhus.lib.core.form;

import de.mhus.lib.errors.MException;

public interface ILayoutRoot {
	
	void build(IUiBuilder builder) throws MException;
	
}

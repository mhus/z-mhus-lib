package de.mhus.lib.form;

import de.mhus.lib.errors.MException;


public class LayoutDataElement extends LayoutElement {

//	@Override
//	public void update(Observable o, Object arg) {
//		
//	}
		
//	public void dump(PrintStream out, int level) {
//		super.dump(out,level);
//	}

	@Override
	public void build(UiBuilder builder) throws MException {
		builder.createDataElement(this);
	}

}

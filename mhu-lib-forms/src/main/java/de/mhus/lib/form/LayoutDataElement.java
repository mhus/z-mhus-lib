package de.mhus.lib.form;

import de.mhus.lib.errors.MException;


/**
 * <p>LayoutDataElement class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class LayoutDataElement extends LayoutElement {

//	@Override
//	public void update(Observable o, Object arg) {
//		
//	}
		
//	public void dump(PrintStream out, int level) {
//		super.dump(out,level);
//	}

	/** {@inheritDoc} */
	@Override
	public void build(UiBuilder builder) throws MException {
		builder.createDataElement(this);
	}

}

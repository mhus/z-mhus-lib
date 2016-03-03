package de.mhus.lib.form;

import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.core.form.ILayoutRoot;
import de.mhus.lib.core.form.IUiBuilder;
import de.mhus.lib.core.util.MNls;
import de.mhus.lib.errors.MException;

/**
 * <p>LayoutRoot class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class LayoutRoot extends LayoutComposite implements ILayoutRoot {

//	private DataSource dataSource;

	/**
	 * <p>Constructor for LayoutRoot.</p>
	 *
	 * @param factory a {@link de.mhus.lib.form.LayoutFactory} object.
	 * @param dataSource a {@link de.mhus.lib.form.DataSource} object.
	 * @param control a {@link de.mhus.lib.form.FormControl} object.
	 * @param nls a {@link de.mhus.lib.core.util.MNls} object.
	 * @param config a {@link de.mhus.lib.core.directory.ResourceNode} object.
	 * @throws java.lang.Exception if any.
	 */
	public LayoutRoot(LayoutFactory factory, DataSource dataSource, FormControl control, MNls nls,ResourceNode config) throws Exception {
		setNls(nls);
		setLayoutFactory(factory);
		setDataSource(dataSource);
		setFormControl(control);
		
		init(null,config);
		
		doInit();
		
	}
	
	/** {@inheritDoc} */
	@Override
	public void build(IUiBuilder builder) throws MException {
		((UiBuilder)builder).createRootStart(this);
		for (LayoutElement c : elements)
			c.build((UiBuilder)builder);
		((UiBuilder)builder).createRootStop(this);
	}

}

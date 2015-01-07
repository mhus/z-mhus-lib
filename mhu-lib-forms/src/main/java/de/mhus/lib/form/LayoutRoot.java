package de.mhus.lib.form;

import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.core.form.ILayoutRoot;
import de.mhus.lib.core.form.IUiBuilder;
import de.mhus.lib.core.util.MNls;
import de.mhus.lib.errors.MException;

public class LayoutRoot extends LayoutComposite implements ILayoutRoot {

//	private DataSource dataSource;

	public LayoutRoot(LayoutFactory factory, DataSource dataSource, FormControl control, MNls nls,ResourceNode config) throws Exception {
		setNls(nls);
		setLayoutFactory(factory);
		setDataSource(dataSource);
		setFormControl(control);
		
		init(null,config);
		
		doInit();
		
	}
	
	@Override
	public void build(IUiBuilder builder) throws MException {
		((UiBuilder)builder).createRootStart(this);
		for (LayoutElement c : elements)
			c.build((UiBuilder)builder);
		((UiBuilder)builder).createRootStop(this);
	}

}

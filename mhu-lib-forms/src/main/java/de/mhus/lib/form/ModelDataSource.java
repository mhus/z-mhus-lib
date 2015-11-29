package de.mhus.lib.form;

public class ModelDataSource implements DataSource {

	private DataSource next;
	
	public ModelDataSource() {
	}
	
	public ModelDataSource(DataSource next) {
		setNext(next);
	}
	
	@Override
	public boolean getBoolean(UiComponent component, String name, boolean def) {
		if (isHandled(name) && component.getConfig().isProperty(name))
			return component.getConfig().getBoolean(name, def);
		
		if (next != null)
			return next.getBoolean(component, name, def);
		
		return def;
	}

	private boolean isHandled(String name) {
		switch(name) {
		case DataSource.CAPTION:
		case DataSource.DESCRIPTION:
		case DataSource.EDITOR_EDITABLE:
		case DataSource.ENABLED:
		case DataSource.VISIBLE:
			return true;
		}
		return false;
	}

	@Override
	public int getInt(UiComponent component, String name, int def) {
		if (isHandled(name) && component.getConfig().isProperty(name))
			return component.getConfig().getInt(name, def);
		
		if (next != null)
			return next.getInt(component, name, def);
		
		return def;
	}

	@Override
	public String getString(UiComponent component, String name, String def) {
		if (isHandled(name) && component.getConfig().isProperty(name))
			return component.getConfig().getString(name, def);
		
		if (next != null)
			return next.getString(component, name, def);
		
		return def;
	}

	@Override
	public Object getObject(UiComponent component, String name, Object def) {
		if (isHandled(name) && component.getConfig().isProperty(name))
			return component.getConfig().getString(name, def != null ? String.valueOf( def ) : null );
		
		if (next != null)
			return next.getObject(component, name, def);
		
		return def;
	}

	@Override
	public void setObject(UiComponent component, String name, Object value) throws Exception {
		if (next != null)
			next.setObject(component, name, value);
	}
	

	public DataSource getNext() {
		return next;
	}

	public void setNext(DataSource chain) {
		this.next = chain;
	}

}

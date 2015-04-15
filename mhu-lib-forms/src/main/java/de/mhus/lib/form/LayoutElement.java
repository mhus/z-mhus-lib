package de.mhus.lib.form;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.core.lang.MObject;
import de.mhus.lib.core.util.MNls;
import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.MRuntimeException;
import de.mhus.lib.form.definition.FmDataSource;

public class LayoutElement extends MObject implements Observer {

	private boolean isFullWidth = false;
	private boolean isTitleInside = false;
	private MNls nls;
	private LayoutComposite parent;
	private DataSource dataSource;
	private FormControl formControl;
	private int maxCols = -1;
	protected ResourceNode config;
	private LayoutFactory factory;
	HashMap<String,DataConnector> sources = null;
	private String nlsPrefix;
	private UiElement ui;
	private String title;
	private String description;
	private String type;
	private String errorMessage;
	private String name;
	
	protected void init(LayoutComposite parent, ResourceNode config) {
		this.parent = parent;
		this.config = config;
	}
	
	public boolean isFullWidth() {
		return isFullWidth;
	}
	public void setFullWidth(boolean isFullWidth) {
		this.isFullWidth = isFullWidth;
	}
	public boolean isTitleInside() {
		return isTitleInside;
	}
	public void setTitleInside(boolean isTitleInside) {
		this.isTitleInside = isTitleInside;
	}

	public MNls getNls() {
		if (nls == null && parent != null) return parent.getNls();
		return nls;
	}
	protected void setNls(MNls nls) {
		this.nls = nls;
	}
	public LayoutComposite getParent() {
		return parent;
	}
	public String getName() {
		return name;
	}
	public DataSource getDataSource() {
		if (dataSource == null && parent != null) return parent.getDataSource();
		return dataSource;
	}

	protected void setDataSource(DataSource dataSource) {
		if (this.dataSource != null) this.dataSource.setConnected(false);
		this.dataSource = dataSource;
		if (this.dataSource != null) this.dataSource.setConnected(true);
	}

	public int getMaxCols() {
		return maxCols;
	}

	public void setMaxCols(int maxCols) {
		this.maxCols = maxCols;
	}
	
	protected void setLayoutFactory(LayoutFactory factory) {
		this.factory = factory;
	}
	
	public LayoutFactory getLayoutFactory() {
		if (factory == null && parent != null)
			return parent.getLayoutFactory();
		return factory;
	}
	
	protected void doInit() throws Exception {
		
		isFullWidth = config.getBoolean("fullwidth", false);
		isTitleInside = config.getBoolean("titleinside", false);
		
		try {
			ResourceNode cSource = config.getNode("sources");
			if (cSource != null) {
				for ( ResourceNode srcConf : cSource.getNodes()) {
					DataConnector con = getDataSource().createDataConnector(this,srcConf);
					if (con != null) {
						if (sources == null) sources = new HashMap<String,DataConnector>();
						sources.put(con.getTaskName(), con);
						con.addObserver(this);
						
						// element enabled
						if (con.getTaskName().equals(DataSource.CONNECTOR_TASK_ENABLED) && config.isProperty("enabled")) {
							con.setBoolean(config.getBoolean("enabled", true));
						}
						
					}
				}
			}
		} catch (Throwable t) {
			log().i(t);
		}
		
		nlsPrefix = config.getExtracted("nls");
		
		title = find("title");
		description = find("description");
		type = config.getExtracted("type");
		name = config.getExtracted("name");
		
		setUi(getLayoutFactory().doBuildUi(this));

		doFallback();
	}

	protected void doFallback() {
		if (this instanceof LayoutDataElement && name != null) {
			if (sources == null || sources.get(DataSource.CONNECTOR_TASK_DATA) == null) {
				try {
					FmDataSource srcConf = new FmDataSource(name);
					DataConnector con = getDataSource().createDataConnector(this,srcConf);
					if (con != null) {
						if (sources == null) sources = new HashMap<String,DataConnector>();
						sources.put(con.getTaskName(), con);
						con.addObserver(this);
					}
				} catch (Exception e){}
			}
			if (nlsPrefix == null) {
				nlsPrefix = name;
				title = find("title");
				description = find("description");
			}
			if (title == null) {
				title = "[" + name + "]";
			}
		}
	}

	public void dump(PrintStream out, int level) {
		out.println(MString.getRepeatig(level, ' ') + getClass() + " (");
		if (nlsPrefix != null ) out.println(MString.getRepeatig(level+1, ' ') + "NLSPrefix: "+ nlsPrefix);
		if (title != null) out.println(MString.getRepeatig(level+1, ' ') + "Title: "+ title);
		if (description != null) out.println(MString.getRepeatig(level+1, ' ') + "Description: "+ description);
		if (sources != null) {
			for (DataConnector con : sources.values())
				out.println(MString.getRepeatig(level+1, ' ') + "Source: "+ con);
		}
		out.println(MString.getRepeatig(level, ' ') + ")");
	}

	public String getNlsPrefix() {
		return nlsPrefix;
	}

	@Override
	public void update(Observable o, Object arg) {
		if (ui != null)
			try {
				ui.doUpdate((DataConnector)o);
			} catch (MException e) {
				log().w(e);
			}
	}
	
	public void build(UiBuilder builder) throws MException {
		builder.createSimpleElement(this);
	}

	public UiElement getUi() {
		return ui;
	}

	public void setUi(UiElement ui) {
		if (ui != null) ui.setElement(null);
		this.ui = ui;
		if (ui != null) ui.setElement(this);
	}

	public FormControl getFormControl() {
		if (parent != null && formControl == null) return parent.getFormControl();
		return formControl;
	}

	protected void setFormControl(FormControl formControl) {
		this.formControl = formControl;
	}

	public ResourceNode getConfig() {
		return config;
	}

	public DataConnector getDataConnector(String taskName) {
		return sources.get(taskName);
	}

	public void fireAllDataSources() {
		if (sources != null) {
			for (DataConnector con : sources.values())
				con.fireDataChanged(null);
		}
	}

	public String getTitle() {
		return title;
	}
	
	public String getDescription() {
		return description;
	}

	public String getType() {
		return type;
	}
	
	@Override
	public String toString() {
		try {
			return config.getName() + "=n:" + name + ",t:" + type + ",fw:" + isFullWidth + ",ti:" + isTitleInside;
		} catch (MException e) {
			throw new MRuntimeException(e);
		}
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String nls, Object ... params) {
		if (nls == null) {
			setErrorMessageDirect(null);
			return;
		}
		HashMap<String, Object> attr = new HashMap<String, Object>();
		for (int i = 0; i < params.length; i++)
			attr.put(String.valueOf(i), MCast.objectToString(params[i]));
		setErrorMessageDirect(getNls().find(nls, attr));
	}
	
	public void setErrorMessageDirect(String errorMessage) {
		if (ui != null) ui.setErrorMessage(errorMessage);
		this.errorMessage = errorMessage;
	}

	public String find(String suffix, String ... attr) {
		return getNls().find(getNlsPrefix() + "_" + suffix + '=' + config.getString(suffix,""), attr);
	}
}

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
import de.mhus.lib.form.definition.FmElement;

/**
 * <p>LayoutElement class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
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
	
	/**
	 * <p>init.</p>
	 *
	 * @param parent a {@link de.mhus.lib.form.LayoutComposite} object.
	 * @param config a {@link de.mhus.lib.core.directory.ResourceNode} object.
	 */
	protected void init(LayoutComposite parent, ResourceNode config) {
		this.parent = parent;
		this.config = config;
	}
	
	/**
	 * <p>isFullWidth.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isFullWidth() {
		return isFullWidth;
	}
	/**
	 * <p>setFullWidth.</p>
	 *
	 * @param isFullWidth a boolean.
	 */
	public void setFullWidth(boolean isFullWidth) {
		this.isFullWidth = isFullWidth;
	}
	/**
	 * <p>isTitleInside.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isTitleInside() {
		return isTitleInside;
	}
	/**
	 * <p>setTitleInside.</p>
	 *
	 * @param isTitleInside a boolean.
	 */
	public void setTitleInside(boolean isTitleInside) {
		this.isTitleInside = isTitleInside;
	}

	/**
	 * <p>Getter for the field <code>nls</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.core.util.MNls} object.
	 */
	public MNls getNls() {
		if (nls == null && parent != null) return parent.getNls();
		return nls;
	}
	/**
	 * <p>Setter for the field <code>nls</code>.</p>
	 *
	 * @param nls a {@link de.mhus.lib.core.util.MNls} object.
	 */
	protected void setNls(MNls nls) {
		this.nls = nls;
	}
	/**
	 * <p>Getter for the field <code>parent</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.form.LayoutComposite} object.
	 */
	public LayoutComposite getParent() {
		return parent;
	}
	/**
	 * <p>Getter for the field <code>name</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getName() {
		return name;
	}
	/**
	 * <p>Getter for the field <code>dataSource</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.form.DataSource} object.
	 */
	public DataSource getDataSource() {
		if (dataSource == null && parent != null) return parent.getDataSource();
		return dataSource;
	}

	/**
	 * <p>Setter for the field <code>dataSource</code>.</p>
	 *
	 * @param dataSource a {@link de.mhus.lib.form.DataSource} object.
	 */
	protected void setDataSource(DataSource dataSource) {
		if (this.dataSource != null) this.dataSource.setConnected(false);
		this.dataSource = dataSource;
		if (this.dataSource != null) this.dataSource.setConnected(true);
	}

	/**
	 * <p>Getter for the field <code>maxCols</code>.</p>
	 *
	 * @return a int.
	 */
	public int getMaxCols() {
		return maxCols;
	}

	/**
	 * <p>Setter for the field <code>maxCols</code>.</p>
	 *
	 * @param maxCols a int.
	 */
	public void setMaxCols(int maxCols) {
		this.maxCols = maxCols;
	}
	
	/**
	 * <p>setLayoutFactory.</p>
	 *
	 * @param factory a {@link de.mhus.lib.form.LayoutFactory} object.
	 */
	protected void setLayoutFactory(LayoutFactory factory) {
		this.factory = factory;
	}
	
	/**
	 * <p>getLayoutFactory.</p>
	 *
	 * @return a {@link de.mhus.lib.form.LayoutFactory} object.
	 */
	public LayoutFactory getLayoutFactory() {
		if (factory == null && parent != null)
			return parent.getLayoutFactory();
		return factory;
	}
	
	/**
	 * <p>doInit.</p>
	 *
	 * @throws java.lang.Exception if any.
	 */
	protected void doInit() throws Exception {
		
		isFullWidth = config.getBoolean(FmElement.FULLWIDTH, false);
		isTitleInside = config.getBoolean(FmElement.TITLEINSIDE, false);
		
		try {
			ResourceNode cSource = config.getNode(FmElement.SOURCES);
			if (cSource != null) {
				for ( ResourceNode srcConf : cSource.getNodes()) {
					DataConnector con = getDataSource().createDataConnector(this,srcConf);
					if (con != null) {
						if (sources == null) sources = new HashMap<String,DataConnector>();
						sources.put(con.getTaskName(), con);
						con.addObserver(this);
						
						// element enabled
						if (con.getTaskName().equals(DataSource.CONNECTOR_TASK_ENABLED) && config.isProperty(FmElement.ENABLED)) {
							con.setBoolean(config.getBoolean(FmElement.ENABLED, true));
						}
						
					}
				}
			}
		} catch (Throwable t) {
			log().i(t);
		}
		
		nlsPrefix = config.getExtracted("nls");
		
		title = find(FmElement.TITLE);
		description = find(FmElement.DESCRIPTION);
		type = config.getExtracted(FmElement.TYPE);
		if (type != null) type = type.toLowerCase();
		name = config.getExtracted(FmElement.NAME);
		
		setUi(getLayoutFactory().doBuildUi(this));

		doFallback();
	}

	/**
	 * <p>doFallback.</p>
	 */
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
				title = find(FmElement.TITLE);
				description = find(FmElement.DESCRIPTION);
			}
			if (title == null) {
				title = "[" + name + "]";
			}
		}
	}

	/**
	 * <p>dump.</p>
	 *
	 * @param out a {@link java.io.PrintStream} object.
	 * @param level a int.
	 */
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

	/**
	 * <p>Getter for the field <code>nlsPrefix</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getNlsPrefix() {
		return nlsPrefix;
	}

	/** {@inheritDoc} */
	@Override
	public void update(Observable o, Object arg) {
		if (ui != null)
			try {
				ui.doUpdate((DataConnector)o);
			} catch (MException e) {
				log().w(e);
			}
	}
	
	/**
	 * <p>build.</p>
	 *
	 * @param builder a {@link de.mhus.lib.form.UiBuilder} object.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	public void build(UiBuilder builder) throws MException {
		builder.createSimpleElement(this);
	}

	/**
	 * <p>Getter for the field <code>ui</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.form.UiElement} object.
	 */
	public UiElement getUi() {
		return ui;
	}

	/**
	 * <p>Setter for the field <code>ui</code>.</p>
	 *
	 * @param ui a {@link de.mhus.lib.form.UiElement} object.
	 */
	public void setUi(UiElement ui) {
		if (ui != null) ui.setElement(null);
		this.ui = ui;
		if (ui != null) ui.setElement(this);
	}

	/**
	 * <p>Getter for the field <code>formControl</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.form.FormControl} object.
	 */
	public FormControl getFormControl() {
		if (parent != null && formControl == null) return parent.getFormControl();
		return formControl;
	}

	/**
	 * <p>Setter for the field <code>formControl</code>.</p>
	 *
	 * @param formControl a {@link de.mhus.lib.form.FormControl} object.
	 */
	protected void setFormControl(FormControl formControl) {
		this.formControl = formControl;
	}

	/**
	 * <p>Getter for the field <code>config</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.core.directory.ResourceNode} object.
	 */
	public ResourceNode getConfig() {
		return config;
	}

	/**
	 * <p>getDataConnector.</p>
	 *
	 * @param taskName a {@link java.lang.String} object.
	 * @return a {@link de.mhus.lib.form.DataConnector} object.
	 */
	public DataConnector getDataConnector(String taskName) {
		return sources.get(taskName);
	}

	/**
	 * <p>fireAllDataSources.</p>
	 */
	public void fireAllDataSources() {
		if (sources != null) {
			for (DataConnector con : sources.values())
				con.fireDataChanged(null);
		}
	}

	/**
	 * <p>Getter for the field <code>title</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * <p>Getter for the field <code>description</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * <p>Getter for the field <code>type</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getType() {
		return type;
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		try {
			return config.getName() + "=n:" + name + ",t:" + type + ",fw:" + isFullWidth + ",ti:" + isTitleInside;
		} catch (MException e) {
			throw new MRuntimeException(e);
		}
	}

	/**
	 * <p>Getter for the field <code>errorMessage</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * <p>Setter for the field <code>errorMessage</code>.</p>
	 *
	 * @param nls a {@link java.lang.String} object.
	 * @param params a {@link java.lang.Object} object.
	 */
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
	
	/**
	 * <p>setErrorMessageDirect.</p>
	 *
	 * @param errorMessage a {@link java.lang.String} object.
	 */
	public void setErrorMessageDirect(String errorMessage) {
		if (ui != null) ui.setErrorMessage(errorMessage);
		this.errorMessage = errorMessage;
	}

	/**
	 * <p>find.</p>
	 *
	 * @param suffix a {@link java.lang.String} object.
	 * @param attr a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public String find(String suffix, String ... attr) {
		return getNls().find(getNlsPrefix() + "_" + suffix + '=' + config.getString(suffix,""), attr);
	}
}

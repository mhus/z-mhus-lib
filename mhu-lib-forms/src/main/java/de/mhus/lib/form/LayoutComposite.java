package de.mhus.lib.form;

import java.io.PrintStream;
import java.util.LinkedList;

import de.mhus.lib.core.MString;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.errors.MException;

/**
 * <p>LayoutComposite class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class LayoutComposite extends LayoutElement {

	private int cols;
	private int labelCols;
	private int offset;
	protected LinkedList<LayoutElement> elements = new LinkedList<LayoutElement>();

	/** {@inheritDoc} */
	@Override
	protected void doInit() throws Exception {
		
		this.cols = doCalculateColums();
		this.labelCols = doCalculateLableColumns();
		this.offset = doCalculateOffset();
				
		super.doInit();
		
		doBuildChildren();
		
		for (LayoutElement sub : elements)
			sub.doInit();
	}

	/**
	 * <p>doCalculateOffset.</p>
	 *
	 * @return a int.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	protected int doCalculateOffset() throws MException {
		int out = config.getInt("columns.offset", -1);
		if (out < 0) {
			if (getParent() == null)
				out = 0;
			else {
				out = getParent().doCalculateChildOffset(this);
			}
		}
		return out;
	}

	/**
	 * <p>doCalculateChildOffset.</p>
	 *
	 * @param layoutComposite a {@link de.mhus.lib.form.LayoutComposite} object.
	 * @return a int.
	 */
	protected int doCalculateChildOffset(LayoutComposite layoutComposite) {
		return 0;
	}

	/**
	 * <p>doCalculateLableColumns.</p>
	 *
	 * @return a int.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	protected int doCalculateLableColumns() throws MException {
		int out = config.getInt("columns.label", -1);
		if (out < 0) {
			if (getParent() == null)
				out = 2;
			else {
				out = getParent().doCalculateChildLabelColumns(this);
			}
		}
		return out;
	}

	/**
	 * <p>doCalculateChildLabelColumns.</p>
	 *
	 * @param child a {@link de.mhus.lib.form.LayoutComposite} object.
	 * @return a int.
	 */
	protected int doCalculateChildLabelColumns(LayoutComposite child) {
		int out = child.getColumns() / 2;
		if (out < 1) return 1;
		if (out > 2) return 2;
		return out;
	}

	/**
	 * <p>doCalculateColums.</p>
	 *
	 * @return a int.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	protected int doCalculateColums() throws MException {
		int out = config.getInt("columns", -1);
		if (out < 0) {
			if (getParent() == null)
				out = 0;
			else {
				out = getParent().doCalculateChildColumns(this);
			}
		}
		return out;
	}

	/**
	 * <p>doCalculateChildColumns.</p>
	 *
	 * @param child a {@link de.mhus.lib.form.LayoutComposite} object.
	 * @return a int.
	 */
	protected int doCalculateChildColumns(LayoutComposite child) {
		return getColumns();
	}

	/**
	 * <p>doBuildChildren.</p>
	 *
	 * @throws java.lang.Exception if any.
	 */
	protected void doBuildChildren() throws Exception {
		ResourceNode cLayout = config.getNode("layout");
		if (cLayout == null) return;
		for (ResourceNode subConfig : cLayout.getNodes()) {
			LayoutElement sub = doBuildChild(this,subConfig);
			if (sub != null)
				elements.add(sub);
		}
	}
	
	/**
	 * <p>doBuildChild.</p>
	 *
	 * @param parent a {@link de.mhus.lib.form.LayoutComposite} object.
	 * @param subConfig a {@link de.mhus.lib.core.directory.ResourceNode} object.
	 * @return a {@link de.mhus.lib.form.LayoutElement} object.
	 * @throws java.lang.Exception if any.
	 */
	protected LayoutElement doBuildChild(LayoutComposite parent, ResourceNode subConfig) throws Exception {
		LayoutElement sub = getLayoutFactory().doBuildChild(this,subConfig);
		return sub;
	}

	/**
	 * <p>getColumns.</p>
	 *
	 * @return a int.
	 */
	public int getColumns() {
		return cols;
	}

	/**
	 * <p>getLabelColums.</p>
	 *
	 * @return a int.
	 */
	public int getLabelColums() {
		return labelCols;
	}

	/**
	 * <p>setColumns.</p>
	 *
	 * @param col a int.
	 */
	public void setColumns(int col) {
		cols = col;
	}
	
	/**
	 * <p>setLabelColumns.</p>
	 *
	 * @param col a int.
	 */
	public void setLabelColumns(int col) {
		labelCols = col;
	}

	/** {@inheritDoc} */
	@Override
	public void dump(PrintStream out, int level) {
		super.dump(out,level);
		out.println(MString.getRepeatig(level, ' ') + "(");
		for (LayoutElement c : elements)
			c.dump(out,level+1);
		out.println(MString.getRepeatig(level, ' ') + ")");
	}

	/** {@inheritDoc} */
	@Override
	public void build(UiBuilder builder) throws MException {
		builder.createCompositStart(this);
		for (LayoutElement c : elements)
			c.build(builder);
		builder.createCompositStop(this);
	}

	/**
	 * <p>Getter for the field <code>offset</code>.</p>
	 *
	 * @return a int.
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * <p>Setter for the field <code>offset</code>.</p>
	 *
	 * @param offset a int.
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return super.toString() + ",c:" + cols + ",lc:" + labelCols + ",o:" + offset;
	}

}

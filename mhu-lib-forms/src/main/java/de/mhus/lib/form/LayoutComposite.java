package de.mhus.lib.form;

import java.io.PrintStream;
import java.util.LinkedList;

import de.mhus.lib.core.MString;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.errors.MException;

public class LayoutComposite extends LayoutElement {

	private int cols;
	private int labelCols;
	private int offset;
	protected LinkedList<LayoutElement> elements = new LinkedList<LayoutElement>();

	protected void doInit() throws Exception {
		
		this.cols = doCalculateColums();
		this.labelCols = doCalculateLableColumns();
		this.offset = doCalculateOffset();
				
		super.doInit();
		
		doBuildChildren();
		
		for (LayoutElement sub : elements)
			sub.doInit();
	}

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

	protected int doCalculateChildOffset(LayoutComposite layoutComposite) {
		return 0;
	}

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

	protected int doCalculateChildLabelColumns(LayoutComposite child) {
		int out = child.getColumns() / 2;
		if (out < 1) return 1;
		if (out > 2) return 2;
		return out;
	}

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

	protected int doCalculateChildColumns(LayoutComposite child) {
		return getColumns();
	}

	protected void doBuildChildren() throws Exception {
		ResourceNode cLayout = config.getNode("layout");
		if (cLayout == null) return;
		for (ResourceNode subConfig : cLayout.getNodes()) {
			LayoutElement sub = doBuildChild(this,subConfig);
			if (sub != null)
				elements.add(sub);
		}
	}
	
	protected LayoutElement doBuildChild(LayoutComposite parent, ResourceNode subConfig) throws Exception {
		LayoutElement sub = getLayoutFactory().doBuildChild(this,subConfig);
		return sub;
	}

	public int getColumns() {
		return cols;
	}

	public int getLabelColums() {
		return labelCols;
	}

	public void setColumns(int col) {
		cols = col;
	}
	
	public void setLabelColumns(int col) {
		labelCols = col;
	}

	public void dump(PrintStream out, int level) {
		super.dump(out,level);
		out.println(MString.getRepeatig(level, ' ') + "(");
		for (LayoutElement c : elements)
			c.dump(out,level+1);
		out.println(MString.getRepeatig(level, ' ') + ")");
	}

	public void build(UiBuilder builder) throws MException {
		builder.createCompositStart(this);
		for (LayoutElement c : elements)
			c.build(builder);
		builder.createCompositStop(this);
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}
	
	public String toString() {
		return super.toString() + ",c:" + cols + ",lc:" + labelCols + ",o:" + offset;
	}

}

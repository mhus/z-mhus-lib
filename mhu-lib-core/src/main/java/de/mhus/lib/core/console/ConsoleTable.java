package de.mhus.lib.core.console;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.mhus.lib.core.MString;

/**
 * <p>ConsoleTable class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class ConsoleTable {
    
	/** Constant <code>SEPARATOR_LINE="---"</code> */
	public static final String SEPARATOR_LINE = "---";
    
	public List<String> header = new ArrayList<String>();
    public List<List<String>> content = new ArrayList<List<String>>();
    private int maxColSize = -1;
    private boolean lineSpacer = false;
    
    /**
     * <p>addRow.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<String> addRow() {
        List<String> row = new ArrayList<String>();
        content.add(row);
        return row;
    }
    
    /**
     * <p>addRowValues.</p>
     *
     * @param values a {@link java.lang.Object} object.
     */
    public void addRowValues(Object ... values) {
    	List<String> row = addRow();
    	for (Object v : values)
    		row.add(String.valueOf(v));
    }
    
    /**
     * <p>Getter for the field <code>header</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<String> getHeader() {
    	return header;
    }
    
    /**
     * <p>setHeaderValues.</p>
     *
     * @param values a {@link java.lang.String} object.
     */
    public void setHeaderValues(String ... values) {
    	List<String> row = getHeader();
    	row.clear();
    	for (String v : values)
    		row.add(v);
    }
    
    /**
     * <p>print.</p>
     *
     * @param console a {@link de.mhus.lib.core.console.Console} object.
     */
    public void print(Console console)  {
    	setMaxColSize(console.getWidth());
    	print((PrintStream)console);
    }
    
    /**
     * <p>print.</p>
     *
     * @param out a {@link java.io.PrintStream} object.
     */
    public void print(PrintStream out)  {
        int[] sizes = new int[header.size()];
        updateSizes(sizes, header);
        for (List<String> row : content) {
            updateSizes(sizes, row);
        }
        String headerLine = getRow(sizes, header, " | ");
        out.println(headerLine);
        out.println(underline(headerLine.length()));
        boolean first = true;
        for (List<String> row : content) {
        	if (!first && lineSpacer) out.println();
            out.println(getRow(sizes, row, " | "));
            first = false;
        }
    }

    private String underline(int length) {
        char[] exmarks = new char[length];
        Arrays.fill(exmarks, '-');
        return new String(exmarks);
    }

    private String getRow(int[] sizes, List<String> row, String separator) {
    	
    	if (row.size() == 1 && row.get(0).equals(SEPARATOR_LINE)) {
    		int s = 0;
    		for (int i : sizes) {
    			if (s != 0) s+=separator.length();
    			s+=i;
    		}
    		return MString.rep('-', s );
    	}
    	
        StringBuilder line = new StringBuilder();
        int c = 0;
        for (String cell : row) {
            if (cell == null) {
                cell = "";
            }
            if (maxColSize > 0 && cell.length() > maxColSize) {
                cell = cell.substring(0, maxColSize -1);
            }
            cell = cell.replaceAll("\n", "");
            line.append(String.format("%-" + sizes[c] + "s", cell));
            if (c + 1 < row.size()) {
                line.append(separator);
            }
            c++;
        }
        return line.toString();
    }

    private void updateSizes(int[] sizes, List<String> row) {
        int c = 0;
        for (String cellContent : row) {
            int cellSize = cellContent != null ? cellContent.length() : 0;
            cellSize = maxColSize > 0 ? Math.min(cellSize, maxColSize) : cellSize;
            if (cellSize > sizes[c]) {
                sizes[c] = cellSize;
            }
            c++;
        }
    }

	/**
	 * <p>Getter for the field <code>maxColSize</code>.</p>
	 *
	 * @return a int.
	 */
	public int getMaxColSize() {
		return maxColSize;
	}

	/**
	 * <p>Setter for the field <code>maxColSize</code>.</p>
	 *
	 * @param maxColSize a int.
	 */
	public void setMaxColSize(int maxColSize) {
		this.maxColSize = maxColSize;
	}

	/**
	 * <p>isLineSpacer.</p>
	 *
	 * @return a boolean.
	 * @since 3.2.9
	 */
	public boolean isLineSpacer() {
		return lineSpacer;
	}

	/**
	 * <p>Setter for the field <code>lineSpacer</code>.</p>
	 *
	 * @param lineSpacer a boolean.
	 * @since 3.2.9
	 */
	public void setLineSpacer(boolean lineSpacer) {
		this.lineSpacer = lineSpacer;
	}
}

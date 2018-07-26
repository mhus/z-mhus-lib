/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.console;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.mhus.lib.core.MDate;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.lang.Value;

public class ConsoleTable {
    
	public static final String SEPARATOR_LINE = "---";

	private static final int MIN_TABLE_WIDTH = 80;
    
	public List<Column> header = new ArrayList<>();
    public List<List<String[]>> content = new ArrayList<>();
    private int maxColSize = -1;
    private boolean lineSpacer = false;
    private boolean multiLine = true;
    private int maxTableWidth = 0;

	private String colSeparator = " | ";

	private boolean acceptHorizontalLine = false;

	private boolean cellSpacer = true;

	private int tableWidth;

	private int definedTableWidth = 0;
    
    public ConsoleTable() {
    }
    
    public ConsoleTable(String options) {
    	if (options != null) {
    		options = options.trim();
    		MProperties o = MProperties.explodeToMProperties(options);
    		setFull(o.getBoolean("full", false));
    		
    		//TODO more options ...
    	} else {
			fitToConsole();
    	}
    }
    
    public ConsoleTable(boolean full) {
    	setFull(full);
	}
    
    public void setFull(boolean full) {
		if (full)
			setMaxColSize(0);
		else
			fitToConsole();
    }

	public Row addRow() {
        return new Row(addIntRow());
    }
    
    private List<String[]> addIntRow() {
        List<String[]> row = new ArrayList<>();
        content.add(row);
        return row;
    }
    
    public void addRowValues(Object ... values) {
    	List<String[]> row = addIntRow();
    	for (Object v : values) {
			row.add(splitInLines(v));
    	}
    }
    
    @SuppressWarnings("rawtypes")
	protected String[] splitInLines(Object v) {
    	if (v == null) return new String[] {""};
    	if (multiLine) {
	    	if (v instanceof Map) {
	    		@SuppressWarnings({ "unchecked" })
				Map<Object,Object> m = (Map)v;
	    		LinkedList<String> out = new LinkedList<>();
	    		for (Map.Entry<Object,Object> entry : m.entrySet())
	    			out.add(entry.getKey() + "=" + entry.getValue());
	    		return out.toArray(new String[out.size()]);
	    	}
	    	if (v instanceof Collection) {
	    		LinkedList<String> out = new LinkedList<>();
	    		for (Object entry : ((Collection)v))
	    			out.add(String.valueOf(entry));
	    		return out.toArray(new String[out.size()]);
	    	}
	    	if (v instanceof Throwable) {
	    		LinkedList<String> out = new LinkedList<>();
	    		Throwable t = (Throwable)v;
	    		if (maxColSize > 0)
	    			out.addAll(MString.splitCollection(t.toString(), maxColSize));
	    		else
	    			out.add(t.toString());
	    		for ( StackTraceElement st : t.getStackTrace())
	    			out.add("  at " + st );
	    		return out.toArray(new String[out.size()]);
	    	}
	    	if (v.getClass().isArray()) {
	    		if (maxColSize > 0)
	    			return MString.split(Arrays.deepToString((Object[]) v), maxColSize);
	    	}
	    	if (v instanceof Date) {
	    		return new String[] { MDate.toIso8601((Date)v) };
	    	}
	    	return String.valueOf(v).split("\n");
    	}
    	return new String[] {String.valueOf(v)};
	}

	public List<Column> getHeader() {
    	return header;
    }
    
    public void setHeaderValues(String ... values) {
    	List<Column> row = getHeader();
    	row.clear();
    	for (String v : values)
    		row.add(new Column(v));
    }
    
    public void print(Console console)  {
    	setMaxColSize(console.getWidth());
    	print((PrintStream)console);
    }
    
    public void print(PrintStream out)  {
        updateHeaderSizes();

        String headerLine = getHeaderRow();
        out.println(headerLine);
    	if (cellSpacer)
    		out.println(underline());
        boolean first = true;
        for (List<String[]> row : content) {
        	if (!first && lineSpacer) out.println();
        	int rowHeight = getRowHeight(row);
        	for (int l = 0; l < rowHeight; l++)
        		out.println(getRow(row, l));
            first = false;
        }
    }

    public void print(PrintWriter out)  {
        updateHeaderSizes();
        String headerLine = getHeaderRow();
        out.println(headerLine);
        if (cellSpacer)
        	out.println(underline());
        boolean first = true;
        for (List<String[]> row : content) {
        	if (!first && lineSpacer) out.println();
        	int rowHeight = getRowHeight(row);
        	for (int l = 0; l < rowHeight; l++)
        		out.println(getRow(row, l));
            first = false;
        }
    }
    
    private int getRowHeight(List<String[]> row) {
    	if (!multiLine) return 1;
    	int height = 1;
    	for (String[] cell : row)
    		height = Math.max(height, cell.length);
		return height;
	}

	private String underline() {
		return MString.rep('-', tableWidth );
    }

    private String getRow(List<String[]> row, int cellLine) {
    	
    	if (acceptHorizontalLine) {
	    	if (row.size() == 1 ) {
	    		String[] row0 = row.get(0);
	    		if (row0 != null && row0.length == 1 && row0[0] != null && row0[0].equals(SEPARATOR_LINE))
	    			return underline();
	    	}
    	}
        StringBuilder line = new StringBuilder();
        int c = 0;
        for (String[] cells : row) {
        	if (c > header.size()) continue;
        	Column h = header.get(c);
        	if (h.width == 0) continue;
        	
        	String cell = getCellLine(cells,cellLine);
        	cell = cell.replaceAll("\n", "");
            if (cell.length() > h.width) {
				cell = MString.truncateNice(cell, h.width);
            }
            if (cellSpacer) {
            	if (h.width > 0)
            		line.append(String.format("%-" + h.width + "s", cell));
            } else
            	line.append(cell);
            
            if (c + 1 < row.size()) {
                line.append(colSeparator);
            }
            
            if (h.last) break;
            c++;
        }
        return line.toString();
    }

    private String getHeaderRow() {
    	
        StringBuilder line = new StringBuilder();
        for (Column h : header) {
        	String cell = h.title;
            if (cell.length() > h.width) {
				cell = MString.truncateNice(cell, h.width);
            }
            cell = cell.replaceAll("\n", "");
            if (cellSpacer) {
            	if (h.width > 0)
            		line.append(String.format("%-" + h.width + "s", cell));
            } else
            	line.append(cell);
            	
            if (!h.last) {
                line.append(colSeparator);
            }
        }
        return line.toString();
    }

    private String getCellLine(String[] cells, int line) {
    	if (!multiLine) {
    		if (line == 0) {
    			return MString.join(cells, ' ');
    		} else
    			return "";
    	}
    	if (cells == null || line < 0 || line >= cells.length) return "";
		return cells[line];
	}
    
    private void updateHeaderSizes() {
    	tableWidth = 0;
    	if (header.size() == 0) return;
    	
    	// check headers
		header.forEach(h -> {
			if (h.title == null) h.title = "";
			h.last = false;
		});
		header.get(header.size()-1).last = true;
		
		// initial header width
        for (Column col : header) {
            col.contentWidth = col.title.length();
        }
        
        // iterate rows
        for (List<String[]> row : content) {
	        int c = 0;
	        for (String[] cells : row) {
	        	if (c >= header.size()) continue;
	        	Column h = header.get(c);
	        	for (String cellContent : cells) {
		            int cellSize = cellContent != null ? cellContent.length() : 0;
		            h.contentWidth = Math.max(h.contentWidth, cellSize);
	        	}
	        	c++;
	        }
        }
        
        // recalculate using guidelines
        Value<Integer> weight = new Value<>(0);
        Value<Integer> weightWidth = new Value<>(0);
        header.forEach(h -> {
        	// find width
        	int width = h.contentWidth;
        	if (h.minWidth > 0) width = Math.max(width, h.minWidth);
        	
        	if (h.maxWidth > 0) width = Math.min(width, h.maxWidth);
        	else
        	if (maxColSize > 0) width = Math.min(width, maxColSize);
        	
        	if (maxTableWidth > 0 && h.weight > 0) {
        		weight.value+=h.weight;
        		weightWidth.value+=width;
        	}
    		tableWidth+=width;
    		if (!h.last && colSeparator != null) tableWidth+=colSeparator.length();
        	
        	h.width = width;
        });

        if (weight.value > 0) {
        	final int delta = maxTableWidth - (tableWidth - weightWidth.value);
        	if (delta > 0) {
	            header.forEach(h -> {
	            	if (h.weight > 0) {
		            	// find width
		            	int width = h.contentWidth;
		            	tableWidth-=width;
		            	width = delta * h.weight / weight.value;
		            	tableWidth+=width;
		            	h.width = width;
	            	}
	            });
        	}
        } 
        else 
        if (maxTableWidth > 0 && tableWidth > maxTableWidth) {
    		int max = maxTableWidth / header.size();
            header.forEach(h -> {
            	int min = Math.max(10, h.minWidth);
            	int d = Math.max(max, min);
            	if (h.width > d) {
            		tableWidth = tableWidth + d - h.width;
	            	h.width= d;
            	}
            });
        }
        
        if (maxTableWidth > 0 && tableWidth > maxTableWidth) {
        	for (int i = header.size()-1 ; i >= 0; i--) {
        		Column h = header.get(i);
        		int width = h.contentWidth;
        		tableWidth-=width;
        		h.width = 0;
        		h.last = true;
        		if (tableWidth < maxTableWidth) {
        			width = maxTableWidth - tableWidth;
        			h.width = width;
	            	tableWidth+=width;
	            	break;
        		}
        	}
        } else
        if (definedTableWidth > 0 && tableWidth < definedTableWidth) {
        	int delta = definedTableWidth - tableWidth;
        	int d = delta / header.size();
        	for (int i = 0; i < header.size(); i++) {
        		Column h = header.get(i);
        		h.width = h.width + d;
        		tableWidth+=d;
        	}
            if (tableWidth < definedTableWidth) {
            	delta = definedTableWidth - tableWidth;
        		Column h = header.get(header.size()-1);
            	h.width = h.width + delta;
        		tableWidth+=delta;
            }
        }
        
    }
    
	public int getMaxColSize() {
		return maxColSize;
	}

	public void setMaxColSize(int maxColSize) {
		this.maxColSize = maxColSize;
	}

	public boolean isLineSpacer() {
		return lineSpacer;
	}

	public void setLineSpacer(boolean lineSpacer) {
		this.lineSpacer = lineSpacer;
	}
	
	@Override
	public String toString() {
		
		StringWriter sw = new StringWriter();
		PrintWriter ps = new PrintWriter(sw);
		print(ps);
		
		return sw.toString();
	}
	
	public static ConsoleTable fromJdbcResult(ResultSet res) throws SQLException {
		ResultSetMetaData resMeta = res.getMetaData();
		ConsoleTable out = new ConsoleTable();
		String[] h = new String[resMeta.getColumnCount()];
		for (int i = 0; i < resMeta.getColumnCount(); i++)
			h[i] = resMeta.getColumnName(i+1);
		
		out.setHeaderValues(h);
		while (res.next()) {
			List<String[]> r = out.addIntRow();
			for (int i = 0; i < resMeta.getColumnCount(); i++)
				r.add(String.valueOf(res.getObject(i+1)).split("\n") );
		}
		return out;
	}

	public String[] toStringArray(boolean showHeader) {
		int i = showHeader ? 1 : 0;
		updateHeaderSizes();
		String[] out = new String[content.size() + i];

        if (showHeader)
        	out[0] = getHeaderRow();
        for (List<String[]> row : content) {
        	int rowHeight = getRowHeight(row);
        	for (int l = 0; l < rowHeight; l++)
        		out[i] = getRow(row, l);
            i++;
        }
		return out;
	}

	public String[][] toStringMatrix(boolean showHeader) {
		int i = showHeader ? 1 : 0;
		String[][] out = new String[content.size() + i][];
        if (showHeader)
        	out[0] = getHeaderRowArray();
        for (List<String[]> row : content) {
        	int rowHeight = getRowHeight(row);
        	for (int l = 0; l < rowHeight; l++)
        		out[i] = getRowArray(row, l);
            i++;
        }
		return out;
	}

	private String[] getRowArray(List<String[]> row, int line) {
		String[] out = new String[row.size()];
		for (int i = 0; i < row.size(); i++)
			out[i] = getCellLine(row.get(i), line);
		return out;
	}
	
	private String[] getHeaderRowArray() {
		String[] out = new String[header.size()];
		for (int i = 0; i < out.length; i++)
			out[i] = header.get(i).title;
		return out;
	}
	
	public boolean isMultiLine() {
		return multiLine;
	}

	public void setMultiLine(boolean multiLine) {
		this.multiLine = multiLine;
	}

	public String getColSeparator() {
		return colSeparator;
	}

	public void setColSeparator(String colSeparator) {
		this.colSeparator = colSeparator;
	}

	public boolean isAcceptHorizontalLine() {
		return acceptHorizontalLine;
	}

	public void setAcceptHorizontalLine(boolean acceptHorizontalLine) {
		this.acceptHorizontalLine = acceptHorizontalLine;
	}

	public boolean isCellSpacer() {
		return cellSpacer;
	}

	public void setCellSpacer(boolean cellSpacer) {
		this.cellSpacer = cellSpacer;
	}

	public class Row {

		private List<String[]> row;

		public Row(List<String[]> row) {
			this.row = row;
		}

		public void add(Object v) {
    			row.add(splitInLines(v));
		}
	}

	public class Column {
		private boolean last = false;
		private int width = 0;
		
		public Column(String title) {
			this.title = title;
		}
		
		public String title;
		public int minWidth = 0;
		public int maxWidth = 0;
		public int contentWidth = 0;
		public int weight = 0;
	}
	
	public int size() {
		return content.size();
	}

	public void removeFirstRow() {
		if (content.size() == 0) return;
		content.remove(0);
	}

	public int getMaxTableWidth() {
		return maxTableWidth;
	}

	public void setMaxTableWidth(int maxTableWidth) {
		this.maxTableWidth = maxTableWidth;
	}

	public void addHeader(String name) {
		header.add(new Column(name));
	}

	public void fitToConsole() {
		Console console = Console.get();
		if (!console.isSupportSize()) {
			setMaxColSize(0);
			return;
		}
		int w = console.getWidth();
		setTableWidth(Math.max(w, MIN_TABLE_WIDTH));
	}

	public void setTableWidth(int width) {
		definedTableWidth  = width;
		maxTableWidth = width;
	}

	public void sort(final int col, final Comparator<String> comparator) {
		content.sort(new Comparator<List<String[]>>() {

			@Override
			public int compare(List<String[]> o1, List<String[]> o2) {
				String[] a1 = o1.get(col);
				String[] a2 = o2.get(col);
				String l1 = MString.join(a1, '\n');
				String l2 = MString.join(a2, '\n');
				return comparator.compare(l1, l2);
			}
		});
	}

}
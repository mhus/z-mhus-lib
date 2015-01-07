package de.mhus.lib.vaadin.aqua;

public class Border {

	public int left;
	public int right;
	public int top;
	public int bottom;
	
	public String color = "black";
	
	public Border() {
	}
	
	public Border(int size) {
		this(size,size,size,size);
	}
	
	public Border(int top, int right, int bottom, int left) {
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		this.left = left;
	}

	public String toStyle() {
		if (left == right && left == top && left == bottom)
			return "" + top + (top > 0 ? "px" : "") + ";";
		return "" + top + (top > 0 ? "px" : "") + " " + right + (right > 0 ? "px" : "") + " " + bottom + (bottom > 0 ? "px" : "" ) + " " + left + (left > 0 ? "px" : "") + ";";
	}
	
	public String toBorderStyle() {
		
		if (left == right && left == top && left == bottom) {
			return 
					( top <= 0 ? "border:0;" : "border: " + top + "px solid " + color + ";");
		}
		return
				( top <= 0 ? "border-top:0;" : "border-top: " + top + "px solid " + color + ";")
				+
				( right <= 0 ? "border-right:0;" : "border-top: " + right + "px solid " + color + ";")
				+
				( bottom <= 0 ? "border-bottom:0;" : "border-bottom: " + bottom + "px solid " + color + ";")
				+
				( left <= 0 ? "border-left:0;" : "border-left: " + left + "px solid " + color + ";");

	}
	
}

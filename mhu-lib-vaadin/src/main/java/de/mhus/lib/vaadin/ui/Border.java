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
package de.mhus.lib.vaadin.ui;

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

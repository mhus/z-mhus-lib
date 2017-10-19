package de.mhus.lib.core.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.mhus.lib.basics.Versioned;

/*
 * From aQute.libg.version.VersionRange
 */
public class VersionRange {
	Version			high;
	Version			low;
	char			start	= '[';
	char			end		= ']';
    public final static String  VERSION_STRING = "(\\d+)(\\.(\\d+)(\\.(\\d+)(\\.([-_\\da-zA-Z]+))?)?)?";

	static Pattern	RANGE	= Pattern.compile("(\\(|\\[)\\s*(" +
									VERSION_STRING + ")\\s*,\\s*(" +
									VERSION_STRING + ")\\s*(\\)|\\])");

	public VersionRange(String string) {
		string = string.trim();
		if (string.indexOf(',') > 0) {
			if (!string.startsWith("[") && !string.startsWith("("))
				string = "[" + string;
			if (!string.endsWith("]") && !string.endsWith(")"))
				string = string + ")";
		}
		
		Matcher m = RANGE.matcher(string);
		if (m.matches()) {
			start = m.group(1).charAt(0);
			String v1 = m.group(2);
			String v2 = m.group(10);
			low = new Version(v1);
			high = new Version(v2);
			end = m.group(18).charAt(0);
			if (low.compareTo(high) > 0)
				throw new IllegalArgumentException(
						"Low Range is higher than High Range: " + low + "-" +
								high);

		} else
			high = low = new Version(string);
	}

	public boolean isRange() {
		return high != low;
	}

	public boolean includeLow() {
		return start == '[';
	}

	public boolean includeHigh() {
		return end == ']';
	}

	@Override
	public String toString() {
		if (high == low)
			return high.toString();

		StringBuffer sb = new StringBuffer();
		sb.append(start);
		sb.append(low);
		sb.append(',');
		sb.append(high);
		sb.append(end);
		return sb.toString();
	}

	public Version getLow() {
		return low;
	}

	public Version getHigh() {
		return high;
	}

	public boolean includes(Versioned v) {
		return includes(new Version(v.getVersionString()));
	}
	
	public boolean includes(Version v) {
		if ( !isRange() ) {
			return low.compareTo(v) <=0;
		}
		if (includeLow()) {
			if (v.compareTo(low) < 0)
				return false;
		} else if (v.compareTo(low) <= 0)
			return false;

		if (includeHigh()) {
			if (v.compareTo(high) > 0)
				return false;
		} else if (v.compareTo(high) >= 0)
			return false;
		
		return true;
	}
}

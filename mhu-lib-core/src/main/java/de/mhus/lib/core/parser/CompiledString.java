package de.mhus.lib.core.parser;

import java.util.LinkedList;
import java.util.Map;

import de.mhus.lib.errors.MException;

/**
 * Parsed and tree representated compiled variant of the original string. This is used to
 * output the changed representation of the string.
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class CompiledString {

	private StringPart[] compiled;

	/**
	 * <p>Constructor for CompiledString.</p>
	 *
	 * @param compiled an array of {@link de.mhus.lib.core.parser.StringPart} objects.
	 */
	public CompiledString(StringPart[] compiled) {
		this.compiled = compiled;
	}
	
	/**
	 * <p>Constructor for CompiledString.</p>
	 *
	 * @param compiled a {@link java.util.LinkedList} object.
	 */
	public CompiledString(LinkedList<StringPart> compiled) {
		this.compiled = compiled.toArray(new StringPart[compiled.size()]);
	}

	/**
	 * Return the new, compiled string.
	 *
	 * @param attributes a {@link java.util.Map} object.
	 * @throws de.mhus.lib.errors.MException if any.
	 * @return a {@link java.lang.String} object.
	 */
	public String execute(Map<String, Object> attributes) throws MException {
		StringBuffer out = new StringBuffer();
		execute(out,attributes);
		return out.toString();
	}
	
	/**
	 * <p>execute.</p>
	 *
	 * @param sb a {@link java.lang.StringBuffer} object.
	 * @param attributes a {@link java.util.Map} object.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	public void execute(StringBuffer sb, Map<String, Object> attributes) throws MException {
		for (StringPart part : compiled) {
			part.execute(sb,attributes);
		}		
	}
	
	/**
	 * Return a readable information about the tree structure.
	 *
	 * @param sb a {@link java.lang.StringBuffer} object.
	 */
	public void dump(StringBuffer sb) {
		for (StringPart part : compiled) {
			part.dump(0,sb);
		}		
	}
}

package de.mhus.lib.core.parser;

import java.util.LinkedList;
import java.util.Map;

import de.mhus.lib.errors.MException;

/**
 * Parsed and tree representated compiled variant of the original string. This is used to
 * output the changed representation of the string.
 * 
 * @author mikehummel
 *
 */
public class CompiledString {

	private StringPart[] compiled;

	public CompiledString(StringPart[] compiled) {
		this.compiled = compiled;
	}
	
	public CompiledString(LinkedList<StringPart> compiled) {
		this.compiled = compiled.toArray(new StringPart[compiled.size()]);
	}

	/**
	 * Return the new, compiled string.
	 * 
	 * @param attributes
	 * @return
	 * @throws MException 
	 */
	public String execute(Map<String, Object> attributes) throws MException {
		StringBuffer out = new StringBuffer();
		execute(out,attributes);
		return out.toString();
	}
	
	public void execute(StringBuffer sb, Map<String, Object> attributes) throws MException {
		for (StringPart part : compiled) {
			part.execute(sb,attributes);
		}		
	}
	
	/**
	 * Return a readable information about the tree structure.
	 * 
	 * @param sb
	 */
	public void dump(StringBuffer sb) {
		for (StringPart part : compiled) {
			part.dump(0,sb);
		}		
	}
}

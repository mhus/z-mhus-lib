package de.mhus.lib.core.parser;

import java.util.Map;

import de.mhus.lib.errors.MException;

public interface StringPart {

	public void execute(StringBuffer out, Map<String, Object> attributes) throws MException;

	public void dump(int level, StringBuffer out);
}

package de.mhus.lib.core.parser;

import java.util.Map;

import de.mhus.lib.core.MString;

public class ConstantPart implements ParsingPart {

	private String content;

	public ConstantPart(String content) {
		this.content = content;
	}
	
	@Override
	public void execute(StringBuffer out, Map<String, Object> attributes) {
		out.append(content);
	}

	@Override
	public void parse(ParseReader str) throws ParseException {
	}

	@Override
	public void dump(int level, StringBuffer out) {
		MString.appendRepeating(level, ' ', out);
		out.append(getClass().getCanonicalName()).append(content);
	}


}

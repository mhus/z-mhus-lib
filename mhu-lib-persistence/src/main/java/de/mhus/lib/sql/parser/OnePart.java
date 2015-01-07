package de.mhus.lib.sql.parser;

import java.io.IOException;

import de.mhus.lib.core.parser.ConstantParsingPart;
import de.mhus.lib.core.parser.ParseReader;

public class OnePart extends ConstantParsingPart {
	
	boolean first = true;
	
	public OnePart(ICompiler compiler) {
	}

	@Override
	public boolean parse(char c, ParseReader str) throws IOException {
		if (first) {
			buffer.append(c);
			str.consume();
			first = false;
			return true;
		}
		return false;
	}

	public void append(char c) {
		content = content + c;
	}
	
}

package de.mhus.lib.sql.parser;

import java.io.IOException;

import de.mhus.lib.core.parser.ConstantParsingPart;
import de.mhus.lib.core.parser.ParseException;
import de.mhus.lib.core.parser.ParseReader;

public class ConstWordPart extends ConstantParsingPart {

	public ConstWordPart(ICompiler compiler) {
	}

	@Override
	public boolean parse(char c, ParseReader str) throws ParseException,
			IOException {
		
		if (c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z' || c == '_') {
			buffer.append(c);
			str.consume();
			return true;
		}
		return false;
	}
	
}

package de.mhus.lib.sql.parser;

import java.io.IOException;

import de.mhus.lib.core.parser.ConstantParsingPart;
import de.mhus.lib.core.parser.ParseReader;

public class NumberPart extends ConstantParsingPart {

	public NumberPart(ICompiler compiler) {
	}

	@Override
	public boolean parse(char c, ParseReader str) throws IOException {
		if (c >= '0' && c <= '9') {
			buffer.append(c);
			str.consume();
			return true;
		}
		return false;
	}

}

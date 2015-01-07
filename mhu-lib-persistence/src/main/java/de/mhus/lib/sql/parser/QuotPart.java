package de.mhus.lib.sql.parser;

import java.io.IOException;

import de.mhus.lib.core.parser.ConstantParsingPart;
import de.mhus.lib.core.parser.ParseException;
import de.mhus.lib.core.parser.ParseReader;

public class QuotPart extends ConstantParsingPart {

	boolean first = true;
	private char marker;

	
	public QuotPart(ICompiler compiler) {
	}


	@Override
	public boolean parse(char c, ParseReader str) throws ParseException,
			IOException {
		
		if (first) {
			marker = c;
			str.consume();
			first = false;
			buffer.append(marker);
			return true;
		} else
		if (c == marker) {
			str.consume();
			buffer.append(marker);
			if (str.isClosed())
				return false;
			char c2 = str.character();
			if (c2 == marker) {
				str.consume();
				buffer.append(marker);
				return true;
			}
			return false;
//		} else
//		if (c == '$') {
//			
		} else {
			buffer.append(c);
			str.consume();
		}
		
		return true;
	}

}

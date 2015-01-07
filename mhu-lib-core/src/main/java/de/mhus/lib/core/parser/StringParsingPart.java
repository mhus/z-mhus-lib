package de.mhus.lib.core.parser;

import java.io.IOException;

import de.mhus.lib.core.lang.MObject;

public abstract class StringParsingPart extends MObject implements ParsingPart {
	
	@Override
	public void parse(ParseReader str) throws ParseException {
		doPreParse();
		
		try {
			while( !str.isClosed() ) {
				char c = str.character();
				boolean res = parse(c,str);
				if (!res) {
					doPostParse();
					return;
				}
			}
		} catch (IOException e) {
			throw new ParseException(e);
		}
		
		doPostParse();

	}

	public abstract void doPreParse();

	public abstract void doPostParse();

	public abstract boolean parse(char c,ParseReader str) throws ParseException, IOException;

}

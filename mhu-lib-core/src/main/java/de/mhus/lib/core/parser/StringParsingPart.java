package de.mhus.lib.core.parser;

import java.io.IOException;

import de.mhus.lib.core.lang.MObject;

/**
 * <p>Abstract StringParsingPart class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public abstract class StringParsingPart extends MObject implements ParsingPart {
	
	/** {@inheritDoc} */
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

	/**
	 * <p>doPreParse.</p>
	 */
	public abstract void doPreParse();

	/**
	 * <p>doPostParse.</p>
	 */
	public abstract void doPostParse();

	/**
	 * <p>parse.</p>
	 *
	 * @param c a char.
	 * @param str a {@link de.mhus.lib.core.parser.ParseReader} object.
	 * @return a boolean.
	 * @throws de.mhus.lib.core.parser.ParseException if any.
	 * @throws java.io.IOException if any.
	 */
	public abstract boolean parse(char c,ParseReader str) throws ParseException, IOException;

}

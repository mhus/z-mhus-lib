package de.mhus.lib.core.parser;


/**
 * <p>ParsingPart interface.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public interface ParsingPart extends StringPart {

	/**
	 * <p>parse.</p>
	 *
	 * @param str a {@link de.mhus.lib.core.parser.ParseReader} object.
	 * @throws de.mhus.lib.core.parser.ParseException if any.
	 */
	void parse(ParseReader str) throws ParseException;
	
}

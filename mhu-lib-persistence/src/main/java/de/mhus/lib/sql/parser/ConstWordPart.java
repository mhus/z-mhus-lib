package de.mhus.lib.sql.parser;

import java.io.IOException;

import de.mhus.lib.core.parser.ConstantParsingPart;
import de.mhus.lib.core.parser.ParseException;
import de.mhus.lib.core.parser.ParseReader;

/**
 * <p>ConstWordPart class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class ConstWordPart extends ConstantParsingPart {

	/**
	 * <p>Constructor for ConstWordPart.</p>
	 *
	 * @param compiler a {@link de.mhus.lib.sql.parser.ICompiler} object.
	 */
	public ConstWordPart(ICompiler compiler) {
	}

	/** {@inheritDoc} */
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

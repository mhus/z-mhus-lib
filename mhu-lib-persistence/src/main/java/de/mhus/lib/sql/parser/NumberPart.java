package de.mhus.lib.sql.parser;

import java.io.IOException;

import de.mhus.lib.core.parser.ConstantParsingPart;
import de.mhus.lib.core.parser.ParseReader;

/**
 * <p>NumberPart class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class NumberPart extends ConstantParsingPart {

	/**
	 * <p>Constructor for NumberPart.</p>
	 *
	 * @param compiler a {@link de.mhus.lib.sql.parser.ICompiler} object.
	 */
	public NumberPart(ICompiler compiler) {
	}

	/** {@inheritDoc} */
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

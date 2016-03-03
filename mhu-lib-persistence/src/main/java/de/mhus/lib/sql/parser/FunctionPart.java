package de.mhus.lib.sql.parser;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;

import de.mhus.lib.core.MString;
import de.mhus.lib.core.parser.ParseException;
import de.mhus.lib.core.parser.ParseReader;
import de.mhus.lib.core.parser.ParsingPart;
import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.MRuntimeException;

/**
 * <p>FunctionPart class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class FunctionPart implements ParsingPart {

	private String name;
	private LinkedList<ParsingPart> parts = new LinkedList<ParsingPart>();
	private ICompiler compiler;

	/**
	 * <p>Constructor for FunctionPart.</p>
	 *
	 * @param compiler a {@link de.mhus.lib.sql.parser.ICompiler} object.
	 * @param name a {@link java.lang.String} object.
	 */
	public FunctionPart(ICompiler compiler, String name) {
		this.compiler = compiler;
		this.name = name;
	}

	/** {@inheritDoc} */
	@Override
	public void execute(StringBuffer out, Map<String, Object> attributes) {
		try {
			out.append(name).append("(");
			boolean first = true;
			for (ParsingPart p : parts) {
				if (!first) out.append(",");
				p.execute(out, attributes);
				first = false;
			}
			out.append(")");
		} catch (MException e) {
			throw new MRuntimeException(e);
		}
	}

	/** {@inheritDoc} */
	@Override
	public void dump(int level, StringBuffer out) {
		MString.appendRepeating(level, ' ', out);
		out.append(getClass().getCanonicalName()).append(" ").append(name).append(" (").append("\n");
		for (ParsingPart p : parts) {
			p.dump(level+1, out);
		}
		MString.appendRepeating(level, ' ', out);
		out.append(")").append("\n");
	}

	/** {@inheritDoc} */
	@Override
	public void parse(ParseReader str) throws ParseException {
		try {
			while (true) {
				removeSpaces(str);
				MainPart pp = new MainPart(compiler);
				pp.setStopOnComma(true);
				pp.parse(str);
				add(pp);
				if ( str.character() == ')') {
					str.consume();
					return;
				}
				if ( str.character() == ',') {
					str.consume();
				}
			}
		} catch (IOException e) {
			throw new ParseException(e);
		}
	}

	private void removeSpaces(ParseReader str) throws IOException {
		do {
			if (str.isClosed()) return;
			char c = str.character();
			if (c != ' ' && c != '\n' && c != '\n' && c != '\r')
				return;
			str.consume();
		} while(true);
	}

	/**
	 * <p>add.</p>
	 *
	 * @param pp a {@link de.mhus.lib.core.parser.ParsingPart} object.
	 */
	public void add(ParsingPart pp) {
		parts.add(pp);
	}

}

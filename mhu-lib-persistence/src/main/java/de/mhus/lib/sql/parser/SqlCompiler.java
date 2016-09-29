package de.mhus.lib.sql.parser;

import java.io.IOException;
import java.io.StringReader;
import java.util.Date;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MDate;
import de.mhus.lib.core.MSql;
import de.mhus.lib.core.parser.CompiledString;
import de.mhus.lib.core.parser.ParseException;
import de.mhus.lib.core.parser.ParseReader;
import de.mhus.lib.core.parser.Parser;
import de.mhus.lib.core.parser.ParsingPart;
import de.mhus.lib.core.parser.StringPart;

/**
 * <p>SqlCompiler class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class SqlCompiler implements  Parser, ICompiler {

	//	private static Log log = Log.getLog(SqlCompiler.class);

	private ICompiler compiler = null;

	/**
	 * <p>Constructor for SqlCompiler.</p>
	 */
	public SqlCompiler() {
		compiler = this;
	}

	/**
	 * <p>Constructor for SqlCompiler.</p>
	 *
	 * @param compiler a {@link de.mhus.lib.sql.parser.ICompiler} object.
	 */
	public SqlCompiler(ICompiler compiler) {
		this.compiler = compiler;
	}

	/** {@inheritDoc} */
	@Override
	public CompiledString compileString(String in) throws ParseException {

		MainPart root = new MainPart(compiler);

		StringReader sr = new StringReader(in);

		ParseReader pr;
		try {
			pr = new ParseReader(sr);
		} catch (IOException e) {
			throw new ParseException(e);
		}
		root.parse(pr);

		return new CompiledString(new StringPart[] {root});
	}

	/** {@inheritDoc} */
	@Override
	public boolean isParseAttributes() {
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public ParsingPart compileFunction(FunctionPart function) {
		return function;
	}

	/**
	 * <p>Setter for the field <code>compiler</code>.</p>
	 *
	 * @param compiler a {@link de.mhus.lib.sql.parser.ICompiler} object.
	 */
	public void setCompiler(ICompiler compiler) {
		this.compiler = compiler;
	}

	/**
	 * <p>Getter for the field <code>compiler</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.sql.parser.ICompiler} object.
	 */
	public ICompiler getCompiler() {
		return compiler;
	}

	/** {@inheritDoc} */
	@Override
	public String toSqlDateValue(Date date) {
		return "'" + MDate.toIsoDate(date) + "'";
	}

	/** {@inheritDoc} */
	@Override
	public String valueToString(Object value) {
		return MCast.objectToString(value);
	}

	/** {@inheritDoc} */
	@Override
	public String escape(String text) {
		return MSql.escape(text);
	}

}

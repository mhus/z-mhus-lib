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

public class SqlCompiler implements  Parser, ICompiler {

	//	private static Log log = Log.getLog(SqlCompiler.class);

	private ICompiler compiler = null;

	public SqlCompiler() {
		compiler = this;
	}

	public SqlCompiler(ICompiler compiler) {
		this.compiler = compiler;
	}

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

	@Override
	public boolean isParseAttributes() {
		return true;
	}

	@Override
	public ParsingPart compileFunction(FunctionPart function) {
		return function;
	}

	public void setCompiler(ICompiler compiler) {
		this.compiler = compiler;
	}

	public ICompiler getCompiler() {
		return compiler;
	}

	@Override
	public String toSqlDateValue(Date date) {
		return "'" + MDate.toIsoDate(date) + "'";
	}

	@Override
	public String valueToString(Object value) {
		return MCast.objectToString(value);
	}

	@Override
	public String escape(String text) {
		return MSql.escape(text);
	}

}

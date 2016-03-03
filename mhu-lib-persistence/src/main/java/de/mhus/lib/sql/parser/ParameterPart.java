package de.mhus.lib.sql.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.lang.Raw;
import de.mhus.lib.core.parser.ParseException;
import de.mhus.lib.core.parser.ParseReader;
import de.mhus.lib.core.parser.StringParsingPart;
import de.mhus.lib.core.util.FallbackMap;
import de.mhus.lib.sql.DbStatement;

/**
 * <p>ParameterPart class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class ParameterPart extends StringParsingPart {


	private StringBuffer buffer;
	public String[] attribute;

	private ICompiler compiler;

	/**
	 * <p>Constructor for ParameterPart.</p>
	 *
	 * @param compiler a {@link de.mhus.lib.sql.parser.ICompiler} object.
	 */
	public ParameterPart(ICompiler compiler) {
		this.compiler = compiler;
	}

	/** {@inheritDoc} */
	@Override
	public void execute(StringBuffer out, Map<String, Object> attributes) {

		Object value = attributes.get(attribute[0]);
		if (value == null) {
			out.append("null");
			return;
		}
		if (value.getClass().isArray()) {
			HashMap<String, Object> valueMap = new HashMap<String, Object>();
			FallbackMap<String, Object> proxyMap = new FallbackMap<String, Object>(valueMap, attributes, false);
			for (int i = 0; i < ((Object[])value).length; i++ ) {
				if (i != 0) out.append(attribute.length > 2 ? attribute[2] : ",");
				valueMap.put(attribute[0], ((Object[])value)[i] );
				execute(out,proxyMap);
			}
			return;
		}
		if (value instanceof List) {
			HashMap<String, Object> valueMap = new HashMap<String, Object>();
			FallbackMap<String, Object> proxyMap = new FallbackMap<String, Object>(valueMap, attributes, false);
			boolean first = true;
			for (Object obj : (List<?>)value) {
				if (!first) out.append(attribute.length > 2 ? attribute[2] : ",");
				valueMap.put(attribute[0], obj );
				execute(out,proxyMap);
				first = false;
			}
			return;
		}
		if (value instanceof InputStream) {
			out.append("?");
			DbStatement.addBinary(attributes,value);
			return;
		}
		String type = null;
		if (attribute.length > 1 && !MString.isEmptyTrim(attribute[1])) {
			type = attribute[1];
		} else {
			if (value instanceof Number || value instanceof Raw)
				type = "raw";  // direct toString() operation (via compiler request)
			else
				if (value instanceof Date || value instanceof Calendar || value instanceof java.sql.Date )
					type = "date";
				else
					if (value instanceof Boolean)
						type = "bool";
					else
						if (value instanceof Enum)
							type = "int";
		}
		if (type == null)
			type = "text";

		log().t(type,value);

		if ("text".equals(type) || "string".equals(type))
			out.append("'").append(compiler.escape(String.valueOf(value))).append("'");
		else
			if ("int".equals(type)) {
				if (value instanceof Enum)
					out.append(compiler.valueToString( ((Enum<?>)value).ordinal() ) );
				else
					out.append(compiler.valueToString(MCast.toint(value.toString(),0)));
			} else
				if ("long".equals(type))
					out.append(compiler.valueToString(MCast.tolong(value.toString(),0)));
				else
					if ("float".equals(type))
						out.append(compiler.valueToString(MCast.tofloat(value.toString(),0)));
					else
						if ("double".equals(type))
							out.append(compiler.valueToString(MCast.todouble(value.toString(),0)));
						else
							if ("date".equals(type))
								out.append( compiler.toSqlDateValue( MCast.objectToDate(value) ) );
							else
								if ("raw".equals(type))
									out.append(compiler.valueToString(value));
								else
									if ("bool".equals(type))
										out.append( MCast.toboolean(value.toString(),false) ? "1" : "0" );
									else
										log().w("Unknown attribute type:",type);
	}

	/** {@inheritDoc} */
	@Override
	public void doPreParse() {
		buffer = new StringBuffer();
	}

	/** {@inheritDoc} */
	@Override
	public void doPostParse() {
		attribute = MString.split(buffer.toString(), ",");
		buffer = null;
	}

	/** {@inheritDoc} */
	@Override
	public boolean parse(char c, ParseReader str) throws ParseException,
	IOException {

		str.consume();
		if (c == '$') {
			return false;
		}

		buffer.append(c);

		return true;

	}

	/** {@inheritDoc} */
	@Override
	public void dump(int level, StringBuffer out) {
		MString.appendRepeating(level, ' ', out);
		out.append(getClass().getCanonicalName()).append(attribute).append("\n");
	}

}

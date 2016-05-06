package de.mhus.lib.adb.query;

import java.util.Map.Entry;
import java.util.function.Function;

import de.mhus.lib.core.MString;
import de.mhus.lib.core.parser.AttributeMap;

public class Db {

	public static <T> AQuery<T> query(Class<T> type) {
		return new AQuery<T>(type);
	}

	public static APart eq(AAttribute left, AAttribute right) {
		return new ACompare(ACompare.TYPE.EQ,left,right);
	}

	public static APart eq(String attr, Object value) {
		return new ACompare(ACompare.TYPE.EQ,attr(attr),value(value));
	}
		
	/**
	 * <p>like</p>
	 * 
	 * @param attr a {@link java.lang.String} object.
	 * @param value a {@link java.lang.Object} object.
	 * @return like
	 */
	public static APart like(String attr, Object value) {
		return new ACompare(ACompare.TYPE.LIKE,attr(attr),value(value));
	}

	public static APart ne(AAttribute left, AAttribute right) {
		return new ACompare(ACompare.TYPE.NE,left,right);
	}

	public static APart lt(AAttribute left, AAttribute right) {
		return new ACompare(ACompare.TYPE.LT,left,right);
	}

	public static APart le(AAttribute left, AAttribute right) {
		return new ACompare(ACompare.TYPE.LE,left,right);
	}
	
	public static APart gt(AAttribute left, AAttribute right) {
		return new ACompare(ACompare.TYPE.GT,left,right);
	}

	public static APart ge(AAttribute left, AAttribute right) {
		return new ACompare(ACompare.TYPE.GE,left,right);
	}
	
	public static APart el(AAttribute left, AAttribute right) {
		return new ACompare(ACompare.TYPE.EL,left,right);
	}

	public static APart eg(AAttribute left, AAttribute right) {
		return new ACompare(ACompare.TYPE.EG,left,right);
	}

	public static APart like(AAttribute left, AAttribute right) {
		return new ACompare(ACompare.TYPE.LIKE,left,right);
	}

	public static APart contains(AAttribute left, AAttribute right) {
		return new ACompare(ACompare.TYPE.LIKE,left, new AContainsWrap( right ) );
	}
	
	public static APart in(AAttribute left, AAttribute right) {
		return new ACompare(ACompare.TYPE.IN,left,right);
	}

	public static AOperation in(AAttribute left, AAttribute projection,
			AQuery<?> subQuery) {
		return new ASubQuery(left, projection, subQuery);
	}

	public static APart and(APart ... operations) {
		return new AAnd(operations);
	}

	public static APart or(APart ... operations) {
		return new AOr(operations);
	}

	public static APart not(APart operation) {
		return new ANot(operation);
	}

	public static AAttribute concat(AAttribute ... parts) {
		return new AConcat(parts);
	}

	/**
	 * A dynamic value.
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	public static AAttribute value(String name, Object value) {
		return new ADynValue(name,value);
	}

	/**
	 * A dynamic value
	 * 
	 * @param value
	 * @return
	 */
	public static AAttribute value(Object value) {
		return new ADynValue(value);
	}

	/**
	 * A database field.
	 * 
	 * @param attribute
	 * @return
	 */
	public static AAttribute attr(String attribute) {
		return new ADbAttribute(null, attribute);
	}

	/**
	 * A database field.
	 * 
	 * @param clazz
	 * @param attribute
	 * @return
	 */
	public static AAttribute attr(Class<?> clazz, String attribute) {
		return new ADbAttribute(clazz, attribute);
	}

	/**
	 * A fixed value
	 * 
	 * @param value
	 * @return
	 */
	public static AAttribute fix(String value) {
		return new AFix(value);
	}

	public static AAttribute fix(Enum<?> value) {
		return new AEnumFix(value);
	}

	public static APart literal(String literal) {
		return new ALiteral(literal);
	}

	public static APart literal(APart ... list) {
		return new ALiteralList(list);
	}

	public static AAttribute list(AAttribute ... list) {
		return new AList(list);
	}

	public static AOperation limit(int limit) {
		return new ALimit(limit);
	}
	
	public static AOperation limit(int offset, int limit) {
		return new ALimit(offset, limit);
	}
	
	private static class AContainsWrap extends AAttribute {

		private AAttribute attr;

		public AContainsWrap(AAttribute attr) {
			this.attr = attr;
		}

		@Override
		public void print(AQuery<?> query, StringBuffer buffer) {
			attr.print(query, buffer);
		}

		@Override
		public void getAttributes(AttributeMap map) {
			AttributeMap map2 = new AttributeMap();
			attr.getAttributes(map2);
			
			for (Entry<String, Object> entry : map2.entrySet())
				map.put(entry.getKey(), "%" + entry.getValue() + "%" );
			
		}
		
	}

	public static AOperation isNull(AAttribute attr) {
		return new ANull(attr, true);
	}

	public static AOperation isNotNull(AAttribute attr) {
		return new ANull(attr, false);
	}


	private static final SearchHelper DEFAULT_HELPER = new SearchHelper();

	/**
	 * Extend the query by operators from the search string. THe method analyze the search string and
	 * add all search parameters to the query concatenating with 'and'.
	 * The syntax of the search are <key>:<value>,<key>:<value>... It's also possible to set only a value.
	 * In this case the helper is asked for the correct key.
	 * 
	 * The helper plays a central role in analyzing the query. The helper can also deny the usage of keys or
	 * change the name.
	 * 
	 * The value can have place holders at the beginning and end to generate a like statement. The placeholder
	 * is a asterisk.
	 * 
	 * @param query The query to extend
	 * @param search The search string
	 * @param helper The helper or null for the default helper
	 * @return The query
	 */
	public static <T> AQuery<T> extendObjectQueryFromSearch(AQuery<T> query, String search, SearchHelper helper) {
		if (MString.isEmpty(search)) return query;
		if (helper == null) helper = DEFAULT_HELPER;
		String[] parts = search.split(",");
		for (String part : parts) {
			if (MString.isSet(part))
				extendObjectQueryFromParameter(query, part, helper);
		}
		return query;
	}

	public static void extendObjectQueryFromParameter(AQuery<?> query, String part, SearchHelper helper) {
		int p = part.indexOf(':');
		String key = null;
		String value = null;
		if (p < 0) {
			key = helper.findKeyForValue(query, part);
			value = part;
		} else {
			key = part.substring(0,p);
			value = part.substring(p+1);
		}
		key = helper.transformKey(query, key);
		if (key == null) return; // ignore
		value = helper.transformValue(query, key, value);
		
		boolean like = false;
		if (helper.isLikeAllowed(query,key)) {
			if (value.startsWith("*") || value.endsWith("*")) {
				value = transformToLikeLike(value);
				like = true;
			}
		}
		
		if (like)
			query.like(key, value);
		else
			helper.extendQuery(query, key, value);
		
	}

	public static String transformToLikeLike(String value) {
		if (value.indexOf('\\') > -1) value = value.replace("\\", "\\\\");
		if (value.indexOf('%') > -1) value = value.replace("%", "\\%");
		if (value.startsWith("*")) value = "%" + value.substring(1);
		if (value.endsWith("*")) value = value.substring(0, value.length()-1) + "%";
		return value;
	}
	
}

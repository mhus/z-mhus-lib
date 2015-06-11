package de.mhus.lib.adb.query;

import java.util.Map.Entry;

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
	
	public static APart ne(AAttribute left, AAttribute right) {
		return new ACompare(ACompare.TYPE.NE,left,right);
	}

	public static APart lt(AAttribute left, AAttribute right) {
		return new ACompare(ACompare.TYPE.LT,left,right);
	}

	public static APart gt(AAttribute left, AAttribute right) {
		return new ACompare(ACompare.TYPE.GT,left,right);
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

}

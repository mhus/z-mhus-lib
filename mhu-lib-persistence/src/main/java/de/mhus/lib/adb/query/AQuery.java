package de.mhus.lib.adb.query;

import java.util.LinkedList;
import java.util.Map;

import de.mhus.lib.adb.DbManager;
import de.mhus.lib.core.parser.AttributeMap;

public class AQuery<T> extends APrint {

	private LinkedList<AOperation> operations;
	private DbManager manager;
	private Class<?> type;

	public AQuery(Class<T> type, AOperation ... operations) {
		this.type = type;
		this.operations = new LinkedList<>();
		for (AOperation o : operations)
			this.operations.add(o);
	}

	public Class<?> getType() {
		return type;
	}

	public String toQualification(DbManager dbManager) {
		manager = dbManager;
		StringBuffer buffer = new StringBuffer();
		print(this, buffer);
		return buffer.toString();
	}

	public Map<String, Object> getAttributes(DbManager dbManager) {
		manager = dbManager;
		AttributeMap map = new AttributeMap();
		getAttributes(map);
		return map;
	}

	@Override
	public void print(AQuery<?> query, StringBuffer buffer) {
		//		buffer.append('(');
		{
			boolean first = true;
			for (AOperation operation : operations) {
				if (operation instanceof APart) {
					if (first)
						first = false;
					else
						buffer.append(" and ");
					operation.print(query, buffer);
				}
			}
		}
		//		buffer.append(')');

		{
			boolean first = true;
			AOperation limit = null;
			for (AOperation operation : operations) {
				if (operation instanceof AOrder) {
					if (first) {
						first = false;
						buffer.append(" ORDER BY ");
					} else
						buffer.append(" , ");
					operation.print(query, buffer);
				} else
					if (operation instanceof ALimit)
						limit = operation;
			}

			if (limit != null) {
				limit.print(query, buffer);
			}

		}


	}

	@Override
	public void getAttributes(AttributeMap map) {
		for (AOperation operation : operations)
			operation.getAttributes(map);
	}

	public DbManager getManager() {
		return manager;
	}

	public AQuery<T> eq(AAttribute left, AAttribute right) {
		operations.add(Db.eq(left, right));
		return this;
	}

	public AQuery<T> eq(String attr, Object value) {
		operations.add(Db.eq(attr, value));
		return this;
	}

	public AQuery<T> ne(AAttribute left, AAttribute right) {
		operations.add(Db.ne(left, right));
		return this;
	}

	public AQuery<T> lt(AAttribute left, AAttribute right) {
		operations.add(Db.lt(left, right));
		return this;
	}

	public AQuery<T> gt(AAttribute left, AAttribute right) {
		operations.add(Db.gt(left, right));
		return this;
	}

	public AQuery<T> el(AAttribute left, AAttribute right) {
		operations.add(Db.el(left, right));
		return this;
	}

	public AQuery<T> eg(AAttribute left, AAttribute right) {
		operations.add(Db.eg(left, right));
		return this;
	}

	public AQuery<T> like(AAttribute left, AAttribute right) {
		operations.add(Db.like(left, right));
		return this;
	}

	public AQuery<T> and(APart ... parts) {
		operations.add(Db.and(parts));
		return this;
	}

	public AQuery<T> or(APart ... parts) {
		operations.add(Db.or(parts));
		return this;
	}

	public AQuery<T> order(AOrder order) {
		operations.add(order);
		return this;
	}

	public AQuery<T> asc(String attr) {
		operations.add(new AOrder(type, attr, true));
		return this;
	}

	public AQuery<T> desc(String attr) {
		operations.add(new AOrder(type, attr, false));
		return this;
	}

	public AQuery<T> literal(APart ... parts) {
		operations.add(Db.literal(parts));
		return this;
	}

	public AQuery<T> literal(ALiteral literal) {
		operations.add(Db.literal(literal));
		return this;
	}

	public AQuery<T> not(APart part) {
		operations.add(Db.not(part));
		return this;
	}

	public AQuery<T> in(AAttribute left, AAttribute ... right) {
		operations.add(Db.in(left, new AList(right) ));
		return this;
	}

	public AQuery<T> limit(int limit) {
		operations.add(Db.limit(limit));
		return this;
	}
	
	public AQuery<T> isNull(AAttribute attr) {
		operations.add(Db.isNull(attr));
		return this;
	}
	
	public AQuery<T> isNotNull(AAttribute attr) {
		operations.add(Db.isNotNull(attr));
		return this;
	}
	
	public AQuery<T> isNull(String attr) {
		operations.add(Db.isNull(Db.attr(attr)));
		return this;
	}
	
	public AQuery<T> isNotNull(String attr) {
		operations.add(Db.isNotNull(Db.attr(attr)));
		return this;
	}

}

package de.mhus.lib.adb.model;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.core.pojo.PojoAttribute;
import de.mhus.lib.errors.MException;
import de.mhus.lib.sql.DbResult;

/**
 * <p>FieldVirtual class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class FieldVirtual extends Field {

	/**
	 * <p>Constructor for FieldVirtual.</p>
	 *
	 * @param table a {@link de.mhus.lib.adb.model.Table} object.
	 * @param isPrimary a boolean.
	 * @param attribute a {@link de.mhus.lib.core.pojo.PojoAttribute} object.
	 * @param attr2 a {@link de.mhus.lib.core.directory.ResourceNode} object.
	 * @param features an array of {@link java.lang.String} objects.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	@SuppressWarnings("unchecked")
	public FieldVirtual(Table table, boolean isPrimary, PojoAttribute<?> attribute, ResourceNode attr2, String[] features) throws MException {
		this.attribute = (PojoAttribute<Object>) attribute;
		this.nameOrg = attribute.getName();
		this.table = table;
		this.manager = table.manager;
		this.name = nameOrg.toLowerCase();
		this.createName = name.toLowerCase();
		this.methodName = name;
		this.isPrimary = isPrimary;
		this.attr = attr2;
		init(features);
	}

	//	public FieldVirtual(Table table, de.mhus.lib.adb.DbDynamic.Field f) {
	//		methodName = f.getName();
	//		this.table = table;
	//		this.nameOrg = methodName;
	//		this.name = methodName.toLowerCase();
	//		this.createName = methodName.toLowerCase();
	//		this.isPrimary = f.isPrimaryKey();
	//		this.ret = f.getReturnType();
	//		this.attr = f.getAttributes();
	//		this.dynamicField = f;
	//	}

	/** {@inheritDoc} */
	@Override
	public void prepareCreate(Object obj)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
	}

	/** {@inheritDoc} */
	@Override
	public Object getFromTarget(Object obj)
			throws Exception {
		return get(obj);
	}

	/** {@inheritDoc} */
	@Override
	public void setToTarget(DbResult res, Object obj) throws Exception {
	}

	/** {@inheritDoc} */
	@Override
	public boolean changed(DbResult res, Object obj) throws Exception {
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public void fillNameMapping(HashMap<String, Object> nameMapping) {
	}

	/** {@inheritDoc} */
	@Override
	public boolean isPersistent() {
		return false;
	}
}

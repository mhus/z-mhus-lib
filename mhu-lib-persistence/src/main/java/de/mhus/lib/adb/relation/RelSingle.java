package de.mhus.lib.adb.relation;

import java.util.List;

import de.mhus.lib.adb.IRelationObject;
import de.mhus.lib.adb.model.Field;
import de.mhus.lib.adb.model.FieldRelation;
import de.mhus.lib.core.parser.AttributeMap;
import de.mhus.lib.sql.DbConnection;

/**
 * <p>RelSingle class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class RelSingle <T> implements IRelationObject {

	private FieldRelation field;
	private Object obj;
	private T relation;
	private boolean changed = false;

	/**
	 * <p>Getter for the field <code>relation</code>.</p>
	 *
	 * @return a T object.
	 * @throws java.lang.Exception if any.
	 */
	@SuppressWarnings("unchecked")
	public T getRelation() throws Exception {
		synchronized (this) {
			if (relation == null) {

				String src = field.getConfig().sourceAttribute();
				if ("".equals(src)) src = field.getName() + "id";
				src = src.toLowerCase();
				String tar = field.getConfig().targetAttribute();
				if ("".equals(tar)) tar = "id";
				//tar = tar.toLowerCase();

				Field idField = field.getTable().getField(src);
				if (idField == null) return null;
				Object id = idField.getFromTarget(obj);
				if (id == null) return null;

				List<?> res = field.getManager().getByQualification(field.getConfig().target(),
						"$db." + field.getManager().getMappingName(field.getConfig().target()) + "." + tar + "$ = $id$",
						new AttributeMap("id", id) ).toCacheAndClose();

				if (res != null && res.size() > 0)
					relation = (T) res.get(0);

				// relation = (T) field.getManager().getObject(field.getConfig().target(), id);

			}
		}
		changed = false;
		return relation;
	}

	/**
	 * <p>Setter for the field <code>relation</code>.</p>
	 *
	 * @param relation a T object.
	 */
	public void setRelation(T relation) {
		changed = true;
		this.relation = relation;
	}

	/**
	 * <p>reset.</p>
	 */
	public void reset() {
		synchronized (this) {
			relation = null;
		}
	}

	/**
	 * <p>prepare.</p>
	 *
	 * @throws java.lang.Exception if any.
	 */
	protected void prepare() throws Exception {
		if (!field.getConfig().managed() || !isChanged()) return;
		synchronized (this) {

			String src = field.getConfig().sourceAttribute();
			if ("".equals(src)) src = field.getName() + "id";
			src = src.toLowerCase();

			Field idField = field.getTable().getField(src);
			if (idField == null) return;

			if (relation == null)
				idField.set(obj, null);
			else {
				String tar = field.getConfig().targetAttribute();
				if ("".equals(tar)) tar = "id";
				tar = tar.toLowerCase();

				Object id = field.getManager().getTable(field.getManager().getRegistryName(relation)).getField(tar).get(relation);
				idField.set(obj, id);
			}
			changed = false;
		}
	}

	/** {@inheritDoc} */
	@Override
	public void prepareCreate() throws Exception{
		prepare();
	}

	/** {@inheritDoc} */
	@Override
	public void created(DbConnection con) throws Exception{
	}

	/** {@inheritDoc} */
	@Override
	public void saved(DbConnection con) throws Exception {
	}

	/** {@inheritDoc} */
	@Override
	public void setManager(FieldRelation field, Object obj) {
		this.field = field;
		this.obj = obj;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isChanged() {
		return changed;
	}

	/** {@inheritDoc} */
	@Override
	public void loaded(DbConnection con) {
		synchronized (this) {
			relation = null;
			changed = false;
		}
	}

	/** {@inheritDoc} */
	@Override
	public void prepareSave(DbConnection con) throws Exception {
		prepare();
	}

}

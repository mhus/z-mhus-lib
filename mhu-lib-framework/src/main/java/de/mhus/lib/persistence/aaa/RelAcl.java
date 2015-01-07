package de.mhus.lib.persistence.aaa;

import java.util.UUID;

import de.mhus.lib.adb.IRelationObject;
import de.mhus.lib.adb.model.Field;
import de.mhus.lib.adb.model.FieldRelation;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.sql.DbConnection;

public class RelAcl implements IRelationObject {

	private UUID acl = null;
	private UUID parentId = null;
	private FieldRelation fieldRelation;
	private Object obj;
	private Field parentField;
	
	@Override
	public void prepareCreate() throws Exception {
	}

	@Override
	public void created(DbConnection con) throws Exception {
		loaded(con);
	}

	@Override
	public void saved(DbConnection con) throws Exception {
		loaded(con);
	}

	@Override
	public void setManager(FieldRelation fieldRelation, Object obj) {
		this.fieldRelation = fieldRelation;
		this.obj = obj;
	}

	@Override
	public boolean isChanged() {
		return false;
	}

	@Override
	public void loaded(DbConnection con) {
		try {
			String aclFieldName = "acl";
			String parentFieldName = "parentid";
			DbAccess anno = MSystem.findAnnotation(fieldRelation.getTable().getClazz(), DbAccess.class);
			if (anno != null) {
				aclFieldName = anno.attribute();
				parentFieldName = anno.parent();
			}
			
			acl = (UUID) fieldRelation.getTable().getField(aclFieldName).get(obj);
			if (MString.isSet(parentFieldName)) {
				parentField = fieldRelation.getTable().getField(parentFieldName);
				if (parentField != null)
					parentId = (UUID) parentField.get(obj);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void prepareSave(DbConnection con) throws Exception {
	}

	public UUID getValue() {
		return acl;
	}
	
	public boolean isParentChanged() {
		if (parentField == null) return false; // so not check rights
		UUID currentId;
		try {
			currentId = (UUID) parentField.get(obj);
		} catch (Exception e) {
			return false; // do nothing in case of an error, log the error ?
		}
		return !MSystem.equals(currentId, parentId);
	}
	
}

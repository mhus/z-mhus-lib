package de.mhus.lib.persistence.aaa;

import java.util.HashMap;
import java.util.UUID;

import de.mhus.lib.adb.DbAccessManager;
import de.mhus.lib.adb.DbCollection;
import de.mhus.lib.adb.DbManager;
import de.mhus.lib.adb.model.Field;
import de.mhus.lib.adb.model.FieldRelation;
import de.mhus.lib.adb.model.Table;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.errors.AccessDeniedException;
import de.mhus.lib.sql.DbConnection;
import de.mhus.lib.sql.DbResult;

public class AclAccessManager extends DbAccessManager {

	private ISubjectCheck check;

	public AclAccessManager(ISubjectCheck check) {
		this.check = check;
	}
	
	@Override
	public void hasAccess(DbManager manager, Table c, DbConnection con,
			Object object, int right) throws AccessDeniedException {
		
		if (check.isAdministrator(manager,con)) return;
		
		if (right == DbManager.R_CREATE) {
			// create right is checked completely different
			hasCreateRight(manager, c, con, object);
			return;
		}
				
		Object[] list = getAcl(manager,con, c,object);
		Acl acl = (Acl) list[0];
		boolean publicAccess = (Boolean) list[1];
		String defaultPolicy = (String)list[2];
		boolean isParentChanged = (Boolean) list[3];

		if (publicAccess) return;

		// if the location of the node is changed, check the move right and the parent create right
		if (right == DbManager.R_UPDATE && isParentChanged) {
			if (!hasRight(manager,con,acl,defaultPolicy,Acl.RIGHT_MOVE))
				throw new AccessDeniedException("access denied", check.getCurrentUserInfo(),acl, Acl.RIGHT_MOVE, object);
			hasCreateRight(manager, c, con, object);
		}
		
		if (right == DbManager.R_UPDATE && !hasRight(manager,con,acl,defaultPolicy,Acl.RIGHT_WRITE))
			throw new AccessDeniedException("access denied", check.getCurrentUserInfo(),acl, Acl.RIGHT_WRITE, object);

		if (right == DbManager.R_DELETE && !hasRight(manager, con, acl, defaultPolicy, Acl.RIGHT_REMOVE))
			throw new AccessDeniedException("access denied", check.getCurrentUserInfo(),acl, Acl.RIGHT_REMOVE, object);
		
	}

	private void hasCreateRight(DbManager manager, Table c, DbConnection con,
			Object object) throws AccessDeniedException {

		// creation out of an DbResult is not possible - paranoia
		if (object instanceof DbResult) throw new AccessDeniedException("access denied", check.getCurrentUserInfo(),object);

		try {
			// find config
			
			Class<?> targetClass = c.getClazz();
			DbAccess anno = MSystem.findAnnotation(targetClass, DbAccess.class);
			String parentFieldName = "parent";
			Class<?> parentType = Class.class;
			String aclFieldName = "acl";
			
			if (anno != null) {
				parentFieldName = anno.parent();
				parentType = anno.parentType();
				aclFieldName = anno.attribute();
			}
			if (parentType == Class.class)
				parentType = targetClass;
			
			
			// find the parent node
			if (MString.isEmpty(parentFieldName)) return; // no check allowed
			
			Table parentTable = null;
			
			Field parentField = c.getField(parentFieldName);
			Object parentValue = parentField.get(object);
			if (parentValue == null)
				throw new AccessDeniedException("access denied: parent is null", check.getCurrentUserInfo(),object);
			UUID parentId = null;
			Object parent = null;
			if (parentValue instanceof UUID)
				parentId = (UUID)parentValue;
			else {
				
				String parentIdStr = parentValue.toString();
				if (parentIdStr.startsWith("from ")) {
					
					// query ... e.g. from Subject where $db.a.b$ = $field$
					
					String query = MString.afterIndex(parentIdStr, ' ');
					String from = MString.beforeIndex(query, ' ').trim();
					query = MString.afterIndex(query, ' ').trim();
					if (!query.startsWith("where "))
						throw new AccessDeniedException("access denied: syntax error", check.getCurrentUserInfo(),object, parentIdStr);
					query = MString.afterIndex(query, ' ').trim();
					
					from = from.toLowerCase();
					parentTable = manager.getTable(from);
					parentType = parentTable.getClazz();
					
					HashMap<String, Object> attributes = new HashMap<String, Object>();
					// fill
					for (Field f : c.getFields()) {
						attributes.put(f.getMappedName(), f.get(object));
					}
					DbCollection<Object> res = manager.getByQualification(con, manager.createSchemaObject(from), from, query, attributes);
					if (res.hasNext()) {
						parent = res.next();
					}
					res.close();
				} else {
					parentId = UUID.fromString(parentIdStr);
				}
			}
			
			// if not loaded, load type
			if (parentTable == null) {
				parentTable = manager.getTable(manager.getRegistryName(parentType));
				if (parentTable == null)
					throw new AccessDeniedException("access denied: parent type not found", check.getCurrentUserInfo(), object, parentType);
			}
			
			// if not loaded, load from parentId
			if (parent == null) {
				parent = manager.getObject(con, parentType, parentId);
			}
		
			if (parent == null)
				throw new AccessDeniedException("access denied: parent not found", check.getCurrentUserInfo(),object, parentId);
			
			// check right "create"
			
			Object[] list = getAcl(manager,con, parentTable,parent);
			Acl acl = (Acl) list[0];
			boolean publicAccess = (Boolean) list[1];
			String defaultPolicy = (String)list[2];

			if (publicAccess) return;
			
			if (!hasRight(manager,con,acl,defaultPolicy,Acl.RIGHT_CREATE))
				throw new AccessDeniedException("access denied", check.getCurrentUserInfo(),acl,Acl.RIGHT_CREATE, object, parent);
			
			// and now set the default acl to the object
			String childAclFieldName = "childacl";
			DbAccess pAnno = MSystem.findAnnotation(parentType, DbAccess.class);
			if (pAnno != null) {
				childAclFieldName = pAnno.childAcl();
			}
			if (MString.isEmpty(childAclFieldName)) return; // ignore feature
			
			Field aclField = c.getField(aclFieldName);
			Field childAclField = parentTable.getField(childAclFieldName);
			
			// must have the same type ...
			
			Object childAcl = childAclField.get(parent);
			aclField.set(object, childAcl);
			
		} catch (AccessDeniedException ade) {
			throw ade;
		} catch (Exception e) {
			throw new AccessDeniedException("error",e);
		}
	}

	public boolean hasRight(DbManager manager, DbConnection con, Acl acl,
			String defaultPolicy, String right) {
		return  
				check.hasRight(manager, con, acl, right) 
				|| 
				AaaUtil.hasRight(defaultPolicy, right) 
				||
				check.hasRight(manager, con, acl, Acl.RIGHT_ALL)
				||
				AaaUtil.hasRight(defaultPolicy, Acl.RIGHT_ALL)
				||
				check.isAdministrator(manager,con) // should be checked before in the methods
				;
	}

	@Override
	public void hasReadAccess(DbManager manager, Table c,
			DbConnection con, DbResult ret) throws AccessDeniedException {

		if (check.isAdministrator(manager,con)) return;

		// TODO it's a hack ! create a official list of world read access classes.
		if (c.getClazz() == Subject.class || c.getClazz() == Acl.class || c.getClazz() == SubjectToSubject.class || c.getClazz() == AclToSubject.class)
			return;
		
		try {
			// load acl
			Object[] list = getAcl(manager,con, c,ret);
			Acl acl = (Acl) list[0];
			boolean publicAccess = (Boolean) list[1];
			String defaultPolicy = (String)list[2];
			
			// check rights
			
			if (publicAccess) return;
			
//			String conf = ret.getString( dbManager.getNameMapping().get("db.finances.confidential").toString() );
			if ( !hasRight(manager, con, acl, defaultPolicy, Acl.RIGHT_READ) ) {
				throw new AccessDeniedException("access denied",check.getCurrentUserInfo(),acl,Acl.RIGHT_READ);
			}
		} catch (Exception e) {
//			e.printStackTrace();
			throw new AccessDeniedException(e);
		}

	}

	public Object[] getAcl(DbManager manager, DbConnection con, Table c, Object object) {
		Class<?> targetClass = c.getClazz();
		
		String aclFieldName = "acl";
		String aclOriginalName = "acloriginal";
		String defaultPolicy = "";
		String ownerFieldName = "";
		Class<?> ownerType = null;
		Acl aclObj = null;
		boolean publicAccess = true;
		boolean isParentChanged = false;

		DbAccess anno = MSystem.findAnnotation(targetClass, DbAccess.class);
		if (anno != null) {
			publicAccess = false;
			aclFieldName = anno.attribute();
			aclOriginalName = anno.original();
			defaultPolicy = anno.worldAccess();
			ownerFieldName = anno.owner();
			ownerType = anno.ownerType();
		}
		
		try {
			
			// check for Owner ...
			if (MString.isSet(ownerFieldName)) {
				// try the owner
				Field ownerField = c.getField(ownerFieldName);
				Object ownerValueObj = null;
				if (object instanceof DbResult)
					ownerValueObj = ((DbResult)object).getString( ownerField.getMappedName() );
				else
					ownerValueObj = ownerField.get(object);
				
				UUID ownerId = null;
				Object ownerObj = null;
				
				if (ownerValueObj instanceof UUID)
					ownerId = (UUID)ownerValueObj;
				else
					ownerId = UUID.fromString(ownerValueObj.toString());
				
				if (ownerId != null) {
					ownerObj = manager.getObject(con, ownerType, ownerId);
				}
				
				if (ownerObj == null) return null;
				
				return getAcl(manager, con, manager.getTable(manager.getRegistryName(ownerObj) ), ownerObj);
				
			}
			
			// check for ACL ...
			Field aclField = c.getField(aclFieldName);
			FieldRelation originalField = c.getFieldRelation(aclOriginalName);
			
			
			if (aclField != null) {
				publicAccess = false;
				
				Object aclValueObj = null;
				if (object instanceof DbResult)	// comes direct from the database
					aclValueObj = ((DbResult)object).getString( aclField.getMappedName() );
				else
				if (originalField != null) { 		// load from an internal copy - the original acl not the locally changed acl
					aclValueObj 	= ((RelAcl)originalField.getRelationObject(object)).getValue(); 
					isParentChanged = ((RelAcl)originalField.getRelationObject(object)).isParentChanged();
				} else 							// TODO should deny this option ... !
					aclValueObj = aclField.get(object); 
				
				if (aclValueObj != null) {
					UUID aclId = null;
					if (aclValueObj instanceof UUID)
						aclId = (UUID)aclValueObj;
					else
						aclId = UUID.fromString(aclValueObj.toString());
					
					if (aclId != null) {
						aclObj = (Acl)manager.getObject(con, Acl.class, aclId);
					}
					
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new Object[] {aclObj, publicAccess, defaultPolicy, isParentChanged};
		
	}
}

package de.mhus.lib.adb.model;

import de.mhus.lib.annotations.adb.DbIndex;
import de.mhus.lib.annotations.adb.DbPersistent;
import de.mhus.lib.annotations.adb.DbPrimaryKey;
import de.mhus.lib.annotations.adb.DbRelation;
import de.mhus.lib.core.config.MConfigFactory;
import de.mhus.lib.core.directory.WritableResourceNode;
import de.mhus.lib.core.pojo.DefaultFilter;
import de.mhus.lib.core.pojo.PojoAttribute;
import de.mhus.lib.core.pojo.PojoModel;
import de.mhus.lib.core.pojo.PojoParser;

public class TableAnnotations extends Table {

	@SuppressWarnings("unchecked")
	protected PojoModel getPojoModel() {
		return new PojoParser().parse(clazz, "_", new Class[] { DbPersistent.class, DbPrimaryKey.class, DbRelation.class }).filter(new DefaultFilter(true,false,true,false,true)).getModel();
	}
	
	@Override
	protected void parseFields() throws Exception {
		
		PojoModel model = getPojoModel();

		for (PojoAttribute<?> attribute : model) {
			String mName = attribute.getName();
			DbPrimaryKey pk = attribute.getAnnotation(DbPrimaryKey.class);
			DbPersistent p  = attribute.getAnnotation(DbPersistent.class);
			DbIndex idx = attribute.getAnnotation(DbIndex.class);
			DbRelation r  = attribute.getAnnotation(DbRelation.class);
			
			if (pk != null || p != null || r != null ) {
				
				
				if (!attribute.canRead()) {
					log().d("getter not found",mName);
					continue;
				}
				if (!attribute.canWrite() && r == null) { // relations do not need setters
					log().d("setter not found",mName);
					continue;
				}
								
				if (r != null) {
					log().t("relation",mName);
					
					// check for doubled
					if (getFieldRelation(mName) != null || getField(mName) != null) {
						log().t("double field definition", mName);
						continue;
					}
					
					FieldRelation fr = new FieldRelation(manager, this, attribute, r);
					addField(fr);
					
				} else {
					log().t("field",mName);
					WritableResourceNode attr = base(MConfigFactory.class).toConfig(toAttributes(p,pk));
					boolean v = (p !=null && p.virtual());
					
					// check for doubled
					if (getField(mName) != null || getFieldRelation(mName) != null) {
						log().t("double field definition", mName);
						continue;
					}
						
					
					Field field = manager.getSchema().createField(manager, this, pk!=null, v, attribute, attr, null, p != null ? p.features() : null);
					if (field != null)
						addField( field );
					
					// indexes
					if (idx != null && field.isPersistent()) {
						addToIndex(idx.value(),field);
					}
				}
			}
		}
	}

}

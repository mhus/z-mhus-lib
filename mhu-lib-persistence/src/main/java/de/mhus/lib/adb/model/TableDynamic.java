package de.mhus.lib.adb.model;

import java.io.IOException;
import java.lang.annotation.Annotation;

import de.mhus.lib.adb.DbDynamic;
import de.mhus.lib.core.pojo.PojoAttribute;
import de.mhus.lib.errors.MException;

public class TableDynamic extends Table {

	@Override
	protected void parseFields() throws InstantiationException, IllegalAccessException, MException {
		DbDynamic.Field[] fa = ((DbDynamic)clazz.newInstance()).getFieldDefinitions();
		for (DbDynamic.Field f : fa) {
			
			PojoAttribute<?> attr = new DynamicAttribute(f);
			Field field = manager.getSchema().createField(manager, this, f.isPrimaryKey(), !f.isPersistent(), attr, f.getAttributes(), f, null);
			
			if (field != null) addField( field );
			
			// indexes
			String[] indexes = f.getIndexes();
			if (indexes != null) {
				addToIndex(indexes,field);
			}

		}		
	}

	private class DynamicAttribute implements PojoAttribute<Object> {

		private de.mhus.lib.adb.DbDynamic.Field f;

		public DynamicAttribute(de.mhus.lib.adb.DbDynamic.Field f) {
			this.f = f;
		}

		@Override
		public Object get(Object pojo) throws IOException {
			return null;
		}

		@Override
		public void set(Object pojo, Object value) throws IOException {
			
		}

		@Override
		public Class<?> getType() {
			return f.getReturnType();
		}

		@Override
		public boolean canRead() {
			return true;
		}

		@Override
		public boolean canWrite() {
			return true;
		}

		@Override
		public Class<Object> getManagedClass() {
			return null;
		}

		@Override
		public String getName() {
			return f.getName();
		}

		@Override
		public <A extends Annotation> A getAnnotation(
				Class<? extends A> annotationClass) {
			return null;
		}
		
	}
}

package de.mhus.lib.adb;

import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.errors.MException;

/**
 * Implement this interface for a persistent class to manage the columns
 * by you own.
 * 
 * @author mikehummel
 *
 */
public interface DbDynamic {

	/**
	 * This is only called at manager initialization time and it return the field definitions for this class.
	 * If you want to change the field definition recreate the DbManager (it's planed to create the possibility
	 * to recreate the definition of the manager in lifetime.
	 * 
	 * @return
	 */
	Field[] getFieldDefinitions();


	void setValue(Field dynamicField, Object value);

	Object getValue(Field dynamicField);

	public interface Field {

		String getName();

		boolean isPrimaryKey();

		Class<?> getReturnType();

		ResourceNode getAttributes();

		String[] getIndexes() throws MException;

		boolean isPersistent();

		boolean isReadOnly();

	}
}

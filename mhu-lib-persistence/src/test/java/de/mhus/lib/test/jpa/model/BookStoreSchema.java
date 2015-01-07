package de.mhus.lib.test.jpa.model;

import de.mhus.lib.jpa.JpaComfortable;
import de.mhus.lib.jpa.JpaSchema;

public class BookStoreSchema extends JpaSchema {

	@Override
	public Class<?>[] getObjectTypes() {
		return new Class[] {
				JpaComfortable.class,
				Book.class,
				Person.class
			};
	}

}

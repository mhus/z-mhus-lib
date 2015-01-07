package de.mhus.lib.annotations.adb;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * The same like DbPersistent but the column is used as primary key. You can
 * mark more then one getter/setter as primary key to generate combined keys.
 * The names will be ordered alphabetically if you search an object with this
 * key.
 * 
 * @author mikehummel
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DbPrimaryKey {
	boolean auto_id() default true;
}

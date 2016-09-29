package de.mhus.lib.annotations.adb;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * With this annotation you can overwrite the default behavior and define a 
 * class table name 'by hand'. Use this annotation for classes.
 * 
 * @author mikehummel
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DbTable {

	String tableName() default "";
	String[] features() default {};
	String attributes() default "";
	
}

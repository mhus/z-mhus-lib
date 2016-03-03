package de.mhus.lib.annotations.adb;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * Mark a getter or setter with this annotation to define it as a column in the database.
 * DbPrimaryKey will have the same effect. If you mark a getter or setter as persistent
 * both functions getter and setter must exist and be public. Fou boolenas the getter
 * can also have a 'is...' notation.
 *
 * @author mikehummel
 * @version $Id: $Id
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DbPersistent {
	int size() default 201;
	String more() default "";
	DbType.TYPE type() default DbType.TYPE.UNKNOWN;
	boolean nullable() default true;
	boolean auto_id() default false;
	boolean virtual() default false;
	String[] features() default {};
	String[] hints() default {};
	String description() default "";
	boolean ro() default false;
}

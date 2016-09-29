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
public @interface DbRelation {
	String sourceAttribute() default "";
	String targetAttribute() default "";
	Class<?> target() default Class.class;
	String orderBy() default "";
	boolean managed() default true;
	boolean remove() default false;
}

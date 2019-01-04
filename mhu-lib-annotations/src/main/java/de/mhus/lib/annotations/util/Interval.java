package de.mhus.lib.annotations.util;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Interval {

	String value();
	Class<?> cfgOwner() default Class.class;
	String cfgPath() default "";
	
}

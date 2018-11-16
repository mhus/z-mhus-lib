package de.mhus.lib.basics.consts;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface GenerateConst {
	Class<? extends Annotation>[] annotation() default {}; 
	String[] ignore() default {};
	boolean restricted() default false;
	
}

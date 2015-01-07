package de.mhus.lib.annotations.jmx;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface JmxManaged {

	String descrition() default "";
	
}

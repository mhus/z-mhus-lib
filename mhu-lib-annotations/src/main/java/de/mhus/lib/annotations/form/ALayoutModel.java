package de.mhus.lib.annotations.form;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ALayoutModel {

	public String value() default "";

}

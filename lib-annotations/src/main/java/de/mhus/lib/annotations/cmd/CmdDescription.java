package de.mhus.lib.annotations.cmd;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CmdDescription {

	String description() default "";
	String[] flags() default {};

}

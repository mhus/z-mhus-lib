package de.mhus.lib.annotations.cmd;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CmdOption {

	String description() default "";

	char shortcut() default 0;

	String name() default "";

	int valueCnt() default 0;

	boolean multi() default false;
	
	boolean mandatory() default false;

	boolean value() default false;

}

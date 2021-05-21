package de.mhus.lib.annotations.cmd;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CmdArgument {

	String description() default "";

	String name() default "";

	int index();

	boolean mandatory() default false;

	int valueCnt() default 0;

	boolean multi() default false;

}

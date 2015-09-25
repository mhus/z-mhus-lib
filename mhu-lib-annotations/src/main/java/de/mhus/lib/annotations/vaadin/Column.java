package de.mhus.lib.annotations.vaadin;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
	String nls() default "";
	String title() default "";
	boolean elapsed() default true;
	int order() default -1;
	String[] schema() default {};
	Align align() default Align.LEFT;
	boolean collapsible() default true;
	boolean editable() default true;
	Class<?> converter() default Object.class;
	String[] properties() default {};
}

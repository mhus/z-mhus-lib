package de.mhus.lib.annotations.jmx;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <p>JmxManaged class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface JmxManaged {

	String descrition() default "";
	
}

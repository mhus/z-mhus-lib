package de.mhus.lib.annotations.activator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <p>DefaultImplementation class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DefaultImplementation {

	Class<?> value();
	
}

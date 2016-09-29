package de.mhus.lib.annotations.activator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <p>DefaultFactory class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DefaultFactory {

	Class<? extends ObjectFactory> value();
	
}

package de.mhus.lib.annotations.activator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface DefaultFactory {

	Class<? extends ObjectFactory> value();
	
}

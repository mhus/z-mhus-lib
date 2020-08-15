package de.mhus.lib.annotations.service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Identify the annotated method as the {@code activate} method of a Service Component.
 *
 * <p>The annotated method is the activate method of the Component.
 *
 * <p>This annotation is not processed at runtime by Service Component Runtime. It must be processed
 * by tools and used to add a Component Description to the bundle.
 *
 * @see "The activate attribute of the component element of a Component Description."
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ServiceFactory {}

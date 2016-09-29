package de.mhus.lib.annotations.form;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <p>ALayoutModel class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ALayoutModel {

	/**
	 * <p>value.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String value() default "";

}

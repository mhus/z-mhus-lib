package de.mhus.lib.form;

/**
 * <p>DataValidationException class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class DataValidationException extends RuntimeException {

	/**
	 * <p>Constructor for DataValidationException.</p>
	 */
	public DataValidationException() {}
	
	/**
	 * <p>Constructor for DataValidationException.</p>
	 *
	 * @param msg a {@link java.lang.String} object.
	 * @since 3.2.9
	 */
	public DataValidationException(String msg) {
		super(msg);
	}
	
	/**
	 * <p>Constructor for DataValidationException.</p>
	 *
	 * @param element a {@link de.mhus.lib.form.LayoutElement} object.
	 * @param msg a {@link java.lang.String} object.
	 * @since 3.2.9
	 */
	public DataValidationException(LayoutElement element, String msg) {
		super(msg);
		setErroMessage(element);
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * <p>setErroMessage.</p>
	 *
	 * @param element a {@link de.mhus.lib.form.LayoutElement} object.
	 */
	public void setErroMessage(LayoutElement element) {
		element.setErrorMessageDirect(getMessage()); //TODO use nls ...
	}

}

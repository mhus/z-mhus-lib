package de.mhus.lib.form;

public class DataValidationException extends RuntimeException {

	public DataValidationException() {}
	
	public DataValidationException(String msg) {
		super(msg);
	}
	
	public DataValidationException(LayoutElement element, String msg) {
		super(msg);
		setErroMessage(element);
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void setErroMessage(LayoutElement element) {
		element.setErrorMessageDirect(getMessage()); //TODO use nls ...
	}

}

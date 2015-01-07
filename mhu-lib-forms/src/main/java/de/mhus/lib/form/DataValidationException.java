package de.mhus.lib.form;

public class DataValidationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void setErroMessage(LayoutElement element) {
		element.setErrorMessageDirect(getMessage()); //TODO use nls ...
	}

}

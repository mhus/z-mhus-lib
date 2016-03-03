package de.mhus.lib.core.parser;

import java.util.Iterator;

/**
 * Parse a string
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
public class StringTokenizerParser implements Iterable<String>, Iterator<String> {

	private String part;
	private String condition;
	protected String breakableCharacters = "";
	protected String enclosureCharacters = "\"'";
	protected String whiteSpace = " \r\t";
	protected String encapsulateCharacters = "\\";
	protected String lineSeparator = "\n";
	protected char enclosure;
	protected char encapsulated;
	protected StringBuffer buffer;
	private int line = 0;
	private char current;
	private String original;
	
	/**
	 * <p>Constructor for StringTokenizerParser.</p>
	 *
	 * @param condition a {@link java.lang.String} object.
	 */
	public StringTokenizerParser(String condition) {
		original = condition;
		condition = condition.trim();
		this.condition = condition;
	}

	/**
	 * <p>parseToken.</p>
	 */
	public void parseToken() {

		while (true) {
			findNextToken();
			if (part == null) return;
			
			part = part.trim();
			if (part.length() != 0) break;
		}
		
	}

	/**
	 * <p>findNextToken.</p>
	 */
	protected void findNextToken() {
		
		part = null;
		if (condition == null) return;
		
		enclosure = 0;
		encapsulated = 0;
		buffer = new StringBuffer();
		for (int i = 0; i < condition.length(); i++) {
			current = condition.charAt(i);
			
			if (encapsulated != 0) {
				foundEncapsulated();
			} else {
				if (enclosure != 0 && current == enclosure) {
					foundToken(i);
					return;
				} else
				if (isEnclosureCharacter()) {
					enclosure = current;
				} else
				if (isWhiteSpace()) {
					foundToken(i);
					return;
				} else
				if (isEndOfLine()) {
					line++;
					foundToken(i);
					return;
				}
				if (isEncapsulateStarting())
					encapsulated = current;
				else
				if (breakOnThisCharacter()) {
					foundBreak(i);
					return;
				} else
					foundCharacter();
			}
		}
		foundEnd();
	}

	/**
	 * <p>foundBreak.</p>
	 *
	 * @param i a int.
	 */
	protected void foundBreak(int i) {
		if (i == 0) {
			buffer.append(current);
			foundToken(i);
		} else {
			foundToken(i-1);
		}
	}

	/**
	 * <p>foundEnd.</p>
	 */
	protected void foundEnd() {
		part = buffer.toString();
		condition = null;
	}

	/**
	 * <p>foundToken.</p>
	 *
	 * @param i a int.
	 */
	protected void foundToken(int i) {
		part = buffer.toString();
		condition = condition.substring(i+1);
	}

	/**
	 * <p>isEncapsulateStarting.</p>
	 *
	 * @return a boolean.
	 */
	protected boolean isEncapsulateStarting() {
		return encapsulateCharacters.indexOf(current) >= 0;
	}

	/**
	 * <p>foundEncapsulated.</p>
	 */
	protected void foundEncapsulated() {
		buffer.append(current);
		encapsulated = 0;
	}

	/**
	 * <p>foundCharacter.</p>
	 */
	protected void foundCharacter() {
		buffer.append(current);
	}

	/**
	 * <p>isWhiteSpace.</p>
	 *
	 * @return a boolean.
	 */
	protected boolean isWhiteSpace() {
		return whiteSpace.indexOf(current) >= 0;
	}

	/**
	 * <p>isEndOfLine.</p>
	 *
	 * @return a boolean.
	 */
	protected boolean isEndOfLine() {
		return lineSeparator.indexOf(current) >= 0;
	}
	
	/**
	 * <p>isEnclosureCharacter.</p>
	 *
	 * @return a boolean.
	 */
	protected boolean isEnclosureCharacter() {
		return enclosureCharacters.indexOf(current) >= 0;
	}

	/**
	 * <p>breakOnThisCharacter.</p>
	 *
	 * @return a boolean.
	 */
	protected boolean breakOnThisCharacter() {
		return breakableCharacters .indexOf(current) >= 0;
	}

	/**
	 * <p>Getter for the field <code>breakableCharacters</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getBreakableCharacters() {
		return breakableCharacters;
	}

	/**
	 * <p>Setter for the field <code>breakableCharacters</code>.</p>
	 *
	 * @param breakableCharacters a {@link java.lang.String} object.
	 */
	public void setBreakableCharacters(String breakableCharacters) {
		this.breakableCharacters = breakableCharacters;
	}

	/**
	 * <p>Getter for the field <code>enclosureCharacters</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getEnclosureCharacters() {
		return enclosureCharacters;
	}

	/**
	 * <p>Setter for the field <code>enclosureCharacters</code>.</p>
	 *
	 * @param enclosureCharacters a {@link java.lang.String} object.
	 */
	public void setEnclosureCharacters(String enclosureCharacters) {
		this.enclosureCharacters = enclosureCharacters;
	}

	/**
	 * <p>Getter for the field <code>whiteSpace</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getWhiteSpace() {
		return whiteSpace;
	}

	/**
	 * <p>Setter for the field <code>whiteSpace</code>.</p>
	 *
	 * @param whiteSpace a {@link java.lang.String} object.
	 */
	public void setWhiteSpace(String whiteSpace) {
		this.whiteSpace = whiteSpace;
	}

	/**
	 * <p>Getter for the field <code>encapsulateCharacters</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getEncapsulateCharacters() {
		return encapsulateCharacters;
	}

	/**
	 * <p>Setter for the field <code>encapsulateCharacters</code>.</p>
	 *
	 * @param encapsulateCharacters a {@link java.lang.String} object.
	 */
	public void setEncapsulateCharacters(String encapsulateCharacters) {
		this.encapsulateCharacters = encapsulateCharacters;
	}

	/**
	 * <p>Getter for the field <code>line</code>.</p>
	 *
	 * @return a int.
	 */
	public int getLine() {
		return line;
	}

	/**
	 * <p>Setter for the field <code>line</code>.</p>
	 *
	 * @param line a int.
	 */
	public void setLine(int line) {
		this.line = line;
	}

	/** {@inheritDoc} */
	@Override
	public boolean hasNext() {
		return condition != null;
	}

	/** {@inheritDoc} */
	@Override
	public Iterator<String> iterator() {
		//reset();
		return this;
	}

	/**
	 * <p>reset.</p>
	 */
	protected void reset() {
		condition = original;
		part = null;
		enclosure = 0;
		encapsulated = 0;
		buffer = null;
		line = 0;
	}

	/** {@inheritDoc} */
	@Override
	public String next() {
		parseToken();
		return part;
	}

	/** {@inheritDoc} */
	@Override
	public void remove() {
		
	}
	
	/**
	 * <p>isTokenEncapsulated.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isTokenEncapsulated() {
		return encapsulated != 0;
	}
	
}

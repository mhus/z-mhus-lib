package de.mhus.lib.test;

import de.mhus.lib.annotations.pojo.Action;
import de.mhus.lib.annotations.pojo.Embedded;
import de.mhus.lib.annotations.pojo.Hidden;

public class PojoExample {

	private String myString;
	private int myInt;
	private double myDouble;
	private boolean myBoolean;
	private char myChar;
	private float myFloat;
	private short myShort;
	private byte myByte;
	private byte[] myByteArray;
	@Embedded
	private PojoEmbedded myEmbedded = new PojoEmbedded();
	@Hidden
	private String myHidden;
	protected String myAttributeOnly;
	private String myReadOnly = "ro";
	
	public String getMyString() {
		return myString;
	}
	public void setMyString(String myString) {
		this.myString = myString;
	}
	public int getMyInt() {
		return myInt;
	}
	public void setMyInt(int myInt) {
		this.myInt = myInt;
	}
	public double getMyDouble() {
		return myDouble;
	}
	public void setMyDouble(double myDouble) {
		this.myDouble = myDouble;
	}
	
	@Embedded
	public PojoEmbedded getMyEmbedded() {
		return myEmbedded;
	}
	
	@Hidden
	public String getMyHidden() {
		return myHidden;
	}
	public void setMyHidden(String myHidden) {
		this.myHidden = myHidden;
	}
	
	@Action
	public void doClean() {
		myString = null;
		myInt = 0;
		myDouble = 0;
		myEmbedded.setLine1(null);
		myEmbedded.setLine2(null);
		myHidden = null;
	}
	
	public String getMyReadOnly() {
		return myReadOnly;
	}
	public boolean isMyBoolean() {
		return myBoolean;
	}
	public void setMyBoolean(boolean myBoolean) {
		this.myBoolean = myBoolean;
	}
	public char getMyChar() {
		return myChar;
	}
	public void setMyChar(char myChar) {
		this.myChar = myChar;
	}
	public float getMyFloat() {
		return myFloat;
	}
	public void setMyFloat(float myFloat) {
		this.myFloat = myFloat;
	}
	public short getMyShort() {
		return myShort;
	}
	public void setMyShort(short myShort) {
		this.myShort = myShort;
	}
	public byte getMyByte() {
		return myByte;
	}
	public void setMyByte(byte myByte) {
		this.myByte = myByte;
	}
	public byte[] getMyByteArray() {
		return myByteArray;
	}
	public void setMyByteArray(byte[] myByteArray) {
		this.myByteArray = myByteArray;
	}
	
	
	
}

package com.mjr.mjrbot.util;

public class BoolStringPair {

	private String valueString;
	private Boolean valueBoolean;

	public BoolStringPair(String valueString, Boolean valueBoolean) {
		super();
		this.valueString = valueString;
		this.valueBoolean = valueBoolean;
	}

	public String getValueString() {
		return valueString;
	}

	public void setValueString(String valueString) {
		this.valueString = valueString;
	}

	public Boolean getValueBoolean() {
		return valueBoolean;
	}

	public void setValueBoolean(Boolean valueBoolean) {
		this.valueBoolean = valueBoolean;
	}

}

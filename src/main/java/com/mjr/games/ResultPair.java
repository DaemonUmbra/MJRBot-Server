package com.mjr.games;

public class ResultPair {
	private String result;
	private boolean hasWon;

	public ResultPair(String result, boolean hasWon) {
		super();
		this.result = result;
		this.hasWon = hasWon;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public boolean hasWon() {
		return hasWon;
	}

	public void setHasWon(boolean hasWon) {
		this.hasWon = hasWon;
	}
}

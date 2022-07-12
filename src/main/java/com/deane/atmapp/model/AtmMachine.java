package com.deane.atmapp.model;

import org.springframework.stereotype.Component;

@Component
public class AtmMachine {
	
	private int fifties;
	private int twenties;
	private int tens;
	private int fives;
	
	public AtmMachine() {
		fifties = 10;
		twenties = 30;
		tens = 30;
		fives = 20;
	}
	
	public int getTotalInMachine() {
		
		return (50 * fifties) + (20 * twenties) + (10 * tens) + (5 * fives);
	}

	public int getFifties() {
		return fifties;
	}

	public void setFifties(int fifties) {
		this.fifties = fifties;
	}

	public int getTwenties() {
		return twenties;
	}

	public void setTwenties(int twenties) {
		this.twenties = twenties;
	}

	public int getTens() {
		return tens;
	}

	public void setTens(int tens) {
		this.tens = tens;
	}

	public int getFives() {
		return fives;
	}

	public void setFives(int fives) {
		this.fives = fives;
	}
	
	public void resetToDefaultAmounts() {
		fifties = 10;
		twenties = 30;
		tens = 30;
		fives = 20;
	}

}

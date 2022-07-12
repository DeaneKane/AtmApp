package com.deane.atmapp.utils;

import com.deane.atmapp.model.AtmMachine;

public class AtmMachineUtilities {
	
	
	//Would have been better to use an object titled Money and loop over it. This is repetitive
	public static int [] calculateRequiredNotes(double dispenseAmount, AtmMachine atmMachine) {
		int [] money = new int[4];
		
		if(dispenseAmount > 50 && atmMachine.getFifties() > 0) {
			if(dispenseAmount >= (atmMachine.getFifties() * 50)) {
				money[0] = atmMachine.getFifties();
				dispenseAmount -= (atmMachine.getFifties() * 50);
				atmMachine.setFifties(0);
			} else {
				money[0] = (int) (dispenseAmount / 50);
				dispenseAmount = dispenseAmount % 50;
				atmMachine.setFifties(atmMachine.getFifties() - money[0]);
			}
		}
		
		if(dispenseAmount > 20 && atmMachine.getTwenties() > 0) {
			if(dispenseAmount >= (atmMachine.getTwenties() * 20)) {
				money[1] = atmMachine.getTwenties();
				dispenseAmount -= (atmMachine.getTwenties() * 20);
				atmMachine.setTwenties(0);
			} else {
				money[1] = (int) (dispenseAmount / 20);
				dispenseAmount = dispenseAmount % 20;
				atmMachine.setTwenties(atmMachine.getTwenties() - money[1]);
			}
		}
		
		if(dispenseAmount > 10 && atmMachine.getTens() > 0) {
			if(dispenseAmount >= (atmMachine.getTens() * 10)) {
				money[2] = atmMachine.getTens();
				dispenseAmount -= (atmMachine.getTens() * 10);
				atmMachine.setTens(0);
			} else {
				money[2] = (int) (dispenseAmount / 10);
				dispenseAmount = dispenseAmount % 10;
				atmMachine.setTens(atmMachine.getTens() - money[2]);
			}
		}
		
		if(dispenseAmount > 5 && atmMachine.getFives() > 0) {
			if(dispenseAmount >= (atmMachine.getFives() * 5)) {
				money[3] = atmMachine.getFives();
				dispenseAmount -= (atmMachine.getFives() * 5);
				atmMachine.setFives(0);
			} else {
				money[3] = (int) (dispenseAmount / 5);
				dispenseAmount = dispenseAmount % 5;
				atmMachine.setFives(atmMachine.getFives() - money[3]);
			}
		}
		return money;
	}

}

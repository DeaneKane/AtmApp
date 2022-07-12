package com.deane.atmapp.controller;

import java.time.LocalDateTime;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deane.atmapp.dao.BankAccountRepo;
import com.deane.atmapp.exception.DispenseIssueException;
import com.deane.atmapp.exception.ErrorResponseDetails;
import com.deane.atmapp.exception.InvalidBankDetailsException;
import com.deane.atmapp.model.AtmMachine;
import com.deane.atmapp.model.BankAccount;
import com.deane.atmapp.utils.AtmMachineUtilities;

@RestController
@RequestMapping("/atm")
public class AtmMachineController {

	@PostConstruct
	public void initialize() {
			BankAccount ba1 = new BankAccount(123456789, 1234, 800, 200);
			BankAccount ba2 = new BankAccount(987654321, 4321, 1230, 150);
			if(bankAccountRepo.findByAccountNumber(123456789) == null){
				bankAccountRepo.save(ba1);
				bankAccountRepo.save(ba2);
			}
	}
	
	@Autowired
	BankAccountRepo bankAccountRepo;

	@Autowired
	AtmMachine atmMachine;

	@GetMapping("/balance/{accountNumber}/{bankPin}")
	public String getBalance(@PathVariable("accountNumber") int accountNumber,
			@PathVariable("bankPin") int bankPin) {
		
		BankAccount bankAccount = bankAccountRepo.findByAccountNumber(accountNumber);
		String message = isValidAccountNumAndPin(bankAccount, bankPin);
		if(!message.equals("valid")) {
			throw new InvalidBankDetailsException(message);
		}

		return "The balance on the account is: " + bankAccount.getCurrentBalance()  + " and the overdraft remaining is: " + bankAccount.getOverdraft() ;
	}
	
	@GetMapping("/dispense/{accountNumber}/{bankPin}/{dispenseAmount}")
	public String dispenseCash(@PathVariable("accountNumber") int accountNumber,
			@PathVariable("bankPin") int bankPin, @PathVariable("dispenseAmount") double dispenseAmount) {
		double tempDispenseAm = dispenseAmount;
		BankAccount bankAccount = bankAccountRepo.findByAccountNumber(accountNumber);
		int money [];
		//Checking validity of account details
		String message = isValidAccountNumAndPin(bankAccount, bankPin);
		if(!message.equals("valid")) {
			throw new InvalidBankDetailsException(message);
		} else if(dispenseAmount % 5 != 0 || dispenseAmount < 0) {
			throw new DispenseIssueException("Dispense amount must be divisible by 5 and more than 0");
		}
		//Checking enough balance
		else if(dispenseAmount > bankAccount.getCurrentBalance() + bankAccount.getOverdraft()) {
			throw new DispenseIssueException("Insufficient balance in your account");
		} else if(atmMachine.getTotalInMachine() < dispenseAmount) {
			throw new DispenseIssueException("Balance in machine less than requested dispense amount");
		}else {			
			money = AtmMachineUtilities.calculateRequiredNotes(dispenseAmount, atmMachine);
		}
		
		if(bankAccount.getCurrentBalance() >= dispenseAmount) {
			bankAccount.setCurrentBalance(bankAccount.getCurrentBalance() - dispenseAmount);
		} else {
			dispenseAmount = dispenseAmount - bankAccount.getCurrentBalance();
			bankAccount.setCurrentBalance(0);
			bankAccount.setOverdraft(bankAccount.getOverdraft() - dispenseAmount);
		}
		bankAccountRepo.save(bankAccount);
		
		StringBuilder withdrawalString = new StringBuilder("Successful Withdrawal: \n" +
		"you have withdraw: " + tempDispenseAm + "\n");
		withdrawalString.append(money[0] + " fifty notes\n");
		withdrawalString.append(money[1] + " twenty notes\n");
		withdrawalString.append(money[2] + " ten notes\n");
		withdrawalString.append(money[3] + " five notes\n");
		withdrawalString.append("Your new balance is: " + bankAccount.getCurrentBalance()+"\n");
		withdrawalString.append("Your overdraft balance is: " + bankAccount.getOverdraft());
		

		return withdrawalString.toString() ;
	}
	
	public String isValidAccountNumAndPin(BankAccount bankAccount, int bankPin) {
		if(bankAccount == null) {
			return "The account number provided was invalid";
		}
		if(bankAccount.getBankPin() != bankPin) {
			return "Incorrect pin - please try again";
		}
		return "valid";
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponseDetails> handleInvalidBankDetailsException(InvalidBankDetailsException ibde) {

		ErrorResponseDetails erd = new ErrorResponseDetails(LocalDateTime.now(), ibde.getMessage(),
				HttpStatus.NOT_FOUND.value());

		return new ResponseEntity<>(erd, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorResponseDetails> handleDispenseIssueException(DispenseIssueException die) {
		
		ErrorResponseDetails erd = new ErrorResponseDetails(LocalDateTime.now(), die.getMessage(),
				HttpStatus.UNAUTHORIZED.value());
		
		return new ResponseEntity<>(erd, HttpStatus.UNAUTHORIZED);
	}
}
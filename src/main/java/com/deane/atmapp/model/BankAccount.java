package com.deane.atmapp.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "bank_account")
public class BankAccount {

	public BankAccount() {
	}

	public BankAccount(int accountNumber, int bankPin, double currentBalance, double overdraft) {
		super();
		this.accountNumber = accountNumber;
		this.bankPin = bankPin;
		this.currentBalance = currentBalance;
		this.overdraft = overdraft;
		this.createdDate = LocalDateTime.now();
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="account_number")
	private int accountNumber;
	
	@Column(name="bank_pin")
	private int bankPin;
	
	@Column(name="current_balance")
	private double currentBalance;
	
	@Column(name="overdraft")
	private double overdraft;

	public int getId() {
		return id;
	}
	
	public int getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(int accountNumber) {
		this.accountNumber = accountNumber;
	}

	public int getBankPin() {
		return bankPin;
	}

	public void setBankPin(int bankPin) {
		this.bankPin = bankPin;
	}

	public double getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(double currentBalance) {
		this.currentBalance = currentBalance;
	}

	public double getOverdraft() {
		return overdraft;
	}

	public void setOverdraft(double overdraft) {
		this.overdraft = overdraft;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	@Column(name="created_date")
	private LocalDateTime createdDate;
	
	
}

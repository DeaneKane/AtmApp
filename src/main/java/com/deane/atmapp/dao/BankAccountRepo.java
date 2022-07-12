package com.deane.atmapp.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deane.atmapp.model.BankAccount;

public interface BankAccountRepo extends JpaRepository<BankAccount, Integer>{
	
	public BankAccount findByAccountNumber(int accountNumber);
}

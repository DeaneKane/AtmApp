package com.deane.atmapp.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.deane.atmapp.dao.BankAccountRepo;
import com.deane.atmapp.model.AtmMachine;
import com.deane.atmapp.model.BankAccount;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TestAtmAppController {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private BankAccountRepo bankAccountRepo;

	@Autowired
	AtmMachine atmMachine;

	@Test
	public void testCheckBalanceSuccess() throws Exception {
		BankAccount ba1 = new BankAccount(999999999, 9999, 123456.99, 0);
		bankAccountRepo.save(ba1);
		MvcResult result = mockMvc.perform(
				MockMvcRequestBuilders.get("/atm/balance/999999999/9999").contentType(MediaType.APPLICATION_JSON))
				.andReturn();
		Assertions.assertTrue(result.getResponse().getContentAsString().contains("123456.99"));
		bankAccountRepo.delete(ba1);
	}

	@Test
	public void testCheckBalanceWrongAccountNum() throws Exception {

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.get("/atm/balance/0/1234").contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
		Assertions.assertTrue(
				result.getResponse().getContentAsString().contains("The account number provided was invalid"));
	}

	@Test
	public void testCheckBalanceWrongPin() throws Exception {

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.get("/atm/balance/123456789/9999")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
		Assertions.assertTrue(result.getResponse().getContentAsString().contains("Incorrect pin - please try again"));

	}
	
	@Test
	public void testDispenseSuccess() throws Exception {
		BankAccount ba1 = new BankAccount(888888888, 8888, 1435, 50);
		bankAccountRepo.save(ba1);
		MvcResult result = mockMvc.perform(
				MockMvcRequestBuilders.get("/atm/dispense/888888888/8888/1485").contentType(MediaType.APPLICATION_JSON))
				.andReturn();
		Assertions.assertTrue(result.getResponse().getContentAsString().contains("Successful Withdrawal: \n"
				+ "you have withdraw: 1485.0\n"
				+ "10 fifty notes\n"
				+ "30 twenty notes\n"
				+ "30 ten notes\n"
				+ "17 five notes\n"
				+ "Your new balance is: 0.0\n"
				+ "Your overdraft balance is: 0.0"));
		bankAccountRepo.delete(ba1);
	}

	@Test
	public void testDispenseWrongAccountNum() throws Exception {

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.get("/atm/dispense/0/1234/50").contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
		Assertions.assertTrue(
				result.getResponse().getContentAsString().contains("number provided"));

	}

	@Test
	public void testDispenseWrongPin() throws Exception {

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.get("/atm/dispense/123456789/9999/50")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
		Assertions.assertTrue(result.getResponse().getContentAsString().contains("Incorrect pin - please try again"));

	}

	@Test
	public void testDispenseNotEnoughBalance() throws Exception {

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.get("/atm/dispense/123456789/1234/9000000")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isUnauthorized()).andReturn();
		Assertions
				.assertTrue(result.getResponse().getContentAsString().contains("Insufficient balance in your account"));

	}

	@Test
	public void testDispenseAtmNotEnough() throws Exception {
		atmMachine.setFifties(0);
		atmMachine.setFives(2);
		atmMachine.setTwenties(0);
		atmMachine.setTens(0);

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.get("/atm/dispense/123456789/1234/15")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isUnauthorized()).andReturn();
		Assertions
		.assertTrue(result.getResponse().getContentAsString().contains("Balance in machine less than requested dispense amount"));
		atmMachine.resetToDefaultAmounts();
	}

}

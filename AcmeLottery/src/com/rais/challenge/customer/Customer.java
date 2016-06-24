package com.rais.challenge.customer;

import java.io.Serializable;
import java.util.Map;

import com.rais.challenge.lottery.util.LotteryType;

public class Customer implements Serializable{
	
	/**
	 * Auto generated Serial Version UID
	 */
	private static final long serialVersionUID = 1L;
	
	private String lastName;
	private String firstName;
	private String email;
	private String id;
	private Map<Long, LotteryType> lotteryCart;
	private Map<Long, LotteryType> winningLottery;
	
	/**
	 * Default Constructor
	 */
	public Customer(){
		super();
	}
	
	/**
	 * Parameterized Constructor
	 * 
	 * @param lastName
	 * @param firstName
	 * @param email
	 * @param id
	 */
	public Customer(String lastName, String firstName, String email, String id) {
		super();
		this.lastName = lastName;
		this.firstName = firstName;
		this.email = email;
		this.id = id;
	}



	public String getLastName() {
		return lastName;
	}



	public void setLastName(String lastName) {
		this.lastName = lastName;
	}



	public String getFirstName() {
		return firstName;
	}



	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public Map<Long, LotteryType> getLotteryCart() {
		return lotteryCart;
	}



	public void setLotteryCart(Map<Long, LotteryType> lotteryCart) {
		this.lotteryCart = lotteryCart;
	}

	public Map<Long, LotteryType> getWinningLottery() {
		return winningLottery;
	}

	public void setWinningLottery(Map<Long, LotteryType> winningLottery) {
		this.winningLottery = winningLottery;
	}



	@Override
	public String toString() {
		return "Customer [lastName=" + lastName + ", firstName=" + firstName + ", email=" + email + ", id=" + id + ", lotteryCart=" + lotteryCart + ", winningLottery=" + winningLottery + "]";
	}
	
	public String getFullName() {
		return firstName+" "+lastName;
	}
	
}

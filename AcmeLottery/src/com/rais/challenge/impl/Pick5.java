package com.rais.challenge.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rais.challenge.LotteryTicket;

public class Pick5 implements LotteryTicket{

	public Pick5() {
		super();
		getLotteryPool();
	}
	
	private Map<Long, Boolean> lotteryPool;

	public Map<Long, Boolean> getLotteryPool() {
		if(null == lotteryPool || lotteryPool.values().isEmpty()){
			lotteryPool = new HashMap<Long, Boolean>();
			System.out.println("Lottery Pool for Pick 5 is provided below: \n");
			for(int i=0; i< numberOfLotteries(); i++){
				Long lotteryNumber = generateLotteryNumber();
				System.out.println("Lottery Number : "+lotteryNumber);
				lotteryPool.put(lotteryNumber, false);
			}
			System.out.println("Lottery Pool : "+lotteryPool.toString()+"\n");
		}
		
		return lotteryPool;
	}

	public void setLotteryPool(Map<Long, Boolean> lotteryPool) {
		this.lotteryPool = lotteryPool;
	}

	@Override
	public Long generateLotteryNumber() {
		return Math.round(Math.random() * 89999) + 10000;
	}

	@Override
	public String lotteryCategory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer prizeValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int numberOfLotteries() {
		return 10;
	}

	@Override
	public Long drawWinner() {
		List<Long> tickets = new ArrayList<Long>();
		tickets.addAll(lotteryPool.keySet());
		Collections.shuffle(tickets);
		Long winner = tickets.get(0);
		System.out.println("Winner for Pick 3 is Lottery Number : "+winner);
		return winner;
	}
}

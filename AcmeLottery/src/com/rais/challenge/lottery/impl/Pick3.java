package com.rais.challenge.lottery.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.rais.challenge.lottery.LotteryTicket;
import com.rais.challenge.lottery.util.LotteryType;

public class Pick3 implements LotteryTicket{

	public Pick3() {
		super();
		getLotteryPool();
	}

	private Map<Long, Boolean> lotteryPool;

	public Map<Long, Boolean> getLotteryPool() {
		if(null == lotteryPool || lotteryPool.values().isEmpty()){
			lotteryPool = new HashMap<Long, Boolean>();
			System.out.println("Lottery Pool for Pick 3 is provided below: \n");
			for(int i=0; i< numberOfLotteries(); i++){
				Long lotteryNumber = generateLotteryNumber();
				System.out.println("Lottery Number : "+lotteryNumber);
				if(!lotteryPool.containsKey(lotteryNumber)) {
					lotteryPool.put(lotteryNumber, false);
				}
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
		return Math.round(Math.random() * 899) + 100;
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
		return 50;
	}

	@Override
	public Long drawWinner() {
		List<Long> tickets = new ArrayList<Long>();
		tickets.addAll(lotteryPool.keySet());
		Collections.shuffle(tickets);
		Long winner = tickets.get(0);
		return winner;
	}
	
	public static LotteryType getLotteryType() {
		return LotteryType.PICK3;
	}

	@Override
	public Long pickRandomLottery() {
		List<Long> lotteries = new ArrayList<Long>();
		for(Long number : lotteryPool.keySet()) {
			if(!lotteryPool.get(number)){
				// If the number is not picked, then add to the list of available lotteries
				lotteries.add(number);
			}
		}
		if(!lotteries.isEmpty()) {
			int index = new Random().nextInt(lotteries.size());
			Long randomPick = lotteries.get(index);
			// Once the lottery is picked, then set the boolean to true
			lotteryPool.put(randomPick, true);
			System.out.println("Lottery Number allocated is "+randomPick);
			return randomPick;
		}
		System.out.println("All tickets sold out for Pick 3");
		return 0L;
	}
}

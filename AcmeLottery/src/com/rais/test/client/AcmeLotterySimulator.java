package com.rais.test.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.rais.challenge.customer.Customer;
import com.rais.challenge.lottery.impl.Pick3;
import com.rais.challenge.lottery.impl.Pick4;
import com.rais.challenge.lottery.impl.Pick5;
import com.rais.challenge.lottery.util.LotteryType;

public class AcmeLotterySimulator {
	
	public static void main(String[] hh) {
		System.out.println("Lottery Initialized. \n");
		Pick3 pick3 = new Pick3();
		Pick4 pick4 = new Pick4();
		Pick5 pick5 = new Pick5();
		
		/** The simulation of lottery pool generation **/
		Map<Long, Boolean> pick3Pool = pick3.getLotteryPool();
		Map<Long, Boolean> pick4Pool = pick4.getLotteryPool();
		Map<Long, Boolean> pick5Pool = pick5.getLotteryPool();
		
		
		/** The simulation of customer buying lotteries **/
		//TODO
		List<Customer> customerPool = new ArrayList<Customer>();
		for(int i=0; i< 10; i++) {
			Customer customer = new Customer("lastName"+i, "firstName"+i, "customer_"+i+"@gmail.com", String.valueOf((i+1)*25+3-2*7));
			Integer picks = getRandomNumberOfTicketPicks(i);
			allocateTicketsToCustomer(customer, picks, pick3Pool, pick4Pool, pick5Pool);
		}
		
		Map<Long, LotteryType> winnerMap = drawWinnerLotteries(pick3, pick4, pick5);
		findWinningCustomerAndDisplayResults(customerPool, winnerMap);
	}

	/**
	 * This operation checks for the winning customer and displays the end results
	 * @param customerPool
	 * @param winnerMap
	 */
	private static void findWinningCustomerAndDisplayResults(List<Customer> customerPool, Map<Long, LotteryType> winnerMap) {
		/** The simulation of picking up the winner based on winner lotteries **/
		List<Customer> winners = new ArrayList<Customer>();
		for(Customer customer: customerPool) {
			Map<Long, LotteryType> customerLotteryCart = customer.getLotteryCart();
			Set<Long> customerLotteries = customerLotteryCart.keySet();
			Set<Long> winnerSet = winnerMap.keySet();
			winnerSet.retainAll(customerLotteries);
			if(winnerSet.size() > 0) {
				Map<Long, LotteryType> winningLottery = new HashMap<Long, LotteryType>();
				for(Long lotteryNumber: winnerSet) {
					winningLottery.put(lotteryNumber, winnerMap.get(lotteryNumber));
					System.out.println("Winner for "+winnerMap.get(lotteryNumber)+" Lottery is "+customer.getFullName()+".");
				}
				customer.setWinningLottery(winningLottery);
				winners.add(customer);
			}
		}
		
		if(!winners.isEmpty()) {
			System.out.println("The winners are listed below:");
			for(Customer customer: winners) {
				Map<Long, LotteryType> winningLottery = customer.getWinningLottery();
				for(Long lottery : winningLottery.keySet()){
					System.out.println("Winner Name : "+customer.getFullName()+" Lottery Number : "+lottery+ " Lottery Category : "+LotteryType.getLabel(winningLottery.get(lottery)));
				}
			}
		}
	}

	/**
	 * This operation allocates the tickets to the customers in a random manner from all the lottery pools
	 * 
	 * @param customer
	 * @param picks
	 * @param pick3Pool
	 * @param pick4Pool
	 * @param pick5Pool
	 */
	private static void allocateTicketsToCustomer(Customer customer, Integer picks, Map<Long, Boolean> pick3Pool, Map<Long, Boolean> pick4Pool, Map<Long, Boolean> pick5Pool) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * This operation selects a random winning lottery number from all 3 pools.
	 * 
	 * @param pick3
	 * @param pick4
	 * @param pick5
	 * @return
	 */
	private static Map<Long, LotteryType> drawWinnerLotteries(Pick3 pick3, Pick4 pick4, Pick5 pick5) {
		System.out.println("Lottery Drawing Initialized. \n");
		/** The Simulation of lottery draw **/
		Long pick3Winner = pick3.drawWinner();
		Long pick4Winner = pick4.drawWinner();
		Long pick5Winner = pick5.drawWinner();
		Map<Long, LotteryType> winnerMap = new HashMap<Long, LotteryType>();
		winnerMap.put(pick3Winner, LotteryType.PICK3);
		winnerMap.put(pick4Winner, LotteryType.PICK4);
		winnerMap.put(pick5Winner, LotteryType.PICK5);
		return winnerMap;
	}

	/**
	 * This operation returns the random number of picks for a customer.
	 * @param customerID
	 * @return
	 */
	private static Integer getRandomNumberOfTicketPicks(int customerID) {
		List<Integer> numberOfPicks = Arrays.asList(1,2,3,4,5);
		int index = new Random().nextInt(numberOfPicks.size());
		Integer randomPick = numberOfPicks.get(index);
		System.out.println("Customer # "+customerID+" gets to pick "+randomPick+" lotteries");
		return randomPick;
	}

}

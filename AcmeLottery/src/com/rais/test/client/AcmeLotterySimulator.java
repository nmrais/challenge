package com.rais.test.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
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
	private static boolean pick3SoldOut = false;
	private static boolean pick4SoldOut = false;
	private static boolean pick5SoldOut = false;
	
	public static void main(String[] hh) {
		System.out.println("Lottery Initialized. \n");
		Pick3 pick3 = new Pick3();
		Pick4 pick4 = new Pick4();
		Pick5 pick5 = new Pick5();
		
		/** The simulation of lottery pool generation **/
		pick3.getLotteryPool();
		pick4.getLotteryPool();
		pick5.getLotteryPool();
		
		
		/** The simulation of customer buying lotteries **/
		List<Customer> customerPool = new ArrayList<Customer>();
		boolean isSoldOut = false;
		for(int i=0; i< 25; i++) {
			Customer customer = new Customer("lastName"+(i+1), "firstName"+(i+1), "customer_"+(i+1)+"@gmail.com", String.valueOf((i+1)*25+3-2*7));
			Integer picks = getRandomNumberOfTicketPicks(i);
			if(!pick3SoldOut || !pick4SoldOut || !pick5SoldOut){
				allocateTicketsToCustomer(customer, picks, pick3, pick4, pick5);
			} else {
				isSoldOut = true;
			}
			if(!isSoldOut){
				System.out.println("Customer Name: "+ customer.getFullName()+" Customer Cart : "+customer.getLotteryCart());
				customerPool.add(customer);
			} 
			
		}
		
		Map<Long, LotteryType> winnerMap = drawWinnerLotteries(pick3, pick4, pick5);
		
		findWinningCustomerAndDisplayResults(customerPool, winnerMap);
	}

	/**
	 * This operation checks for the winning customer and displays the end results
	 * 
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
			Set<Long> customerWinnerSet = new HashSet<Long>();
			for(Long winner : winnerSet) {
				if(customerLotteries.contains(winner)){
					customerWinnerSet.add(winner);
				}
			}
			if(customerWinnerSet.size() > 0) {
				Map<Long, LotteryType> winningLottery = new HashMap<Long, LotteryType>();
				for(Long lotteryNumber: customerWinnerSet) {
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
	private static boolean allocateTicketsToCustomer(Customer customer, Integer picks, Pick3 pick3, Pick4 pick4, Pick5 pick5) {
		Map<Long, LotteryType> lotteryCart = new HashMap<Long, LotteryType>();
		
		for(int i=0; i<picks; i++){
			Integer randomPick = getRandomPickPool();
			Long lottery = 0L;
			if(randomPick == 3){
				lottery = pick3.pickRandomLottery();
				if(lottery != 0L && !pick3SoldOut){
					lotteryCart.put(lottery, LotteryType.PICK3);
				} else {
					pick3SoldOut = true;
				}
			} else if (randomPick == 4) {
				lottery = pick4.pickRandomLottery();
				if(lottery != 0L && !pick4SoldOut){
					lotteryCart.put(lottery, LotteryType.PICK4);
				} else {
					pick4SoldOut = true;
				}
			} else if (randomPick == 5){
				lottery = pick5.pickRandomLottery();
				if(lottery != 0L && !pick5SoldOut){
					lotteryCart.put(lottery, LotteryType.PICK5);
				} else {
					pick5SoldOut = true;
				}
			}
		}
		customer.setLotteryCart(lotteryCart);
		return true;
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
	
	/**
	 * This operation returns the random number of picks for a customer.
	 * @param customerID
	 * @return
	 */
	private static Integer getRandomPickPool() {
		List<Integer> numberOfPicks = new ArrayList<Integer>();
		if(!pick3SoldOut && !pick4SoldOut && !pick5SoldOut){
			numberOfPicks = Arrays.asList(3,4,5);
		} else if(!pick3SoldOut && !pick4SoldOut && pick5SoldOut) {
			numberOfPicks = Arrays.asList(3,4);
		} else if(!pick3SoldOut && pick4SoldOut && !pick5SoldOut){
			numberOfPicks = Arrays.asList(3,5);
		} else if(pick3SoldOut && !pick4SoldOut && !pick5SoldOut){
			numberOfPicks = Arrays.asList(4,5);
		} else if(pick3SoldOut && pick4SoldOut && !pick5SoldOut){
			System.out.println("Pick 5 is selected for next pick.");
			return 5;
		} else if(!pick3SoldOut && pick4SoldOut && pick5SoldOut){
			System.out.println("Pick 3 is selected for next pick.");
			return 3;
		} else if(pick3SoldOut && !pick4SoldOut && pick5SoldOut){
			System.out.println("Pick 4 is selected for next pick.");
			return 4;
		} 
		if(!numberOfPicks.isEmpty()) {
			int index = new Random().nextInt(numberOfPicks.size());
			Integer randomPick = numberOfPicks.get(index);
			System.out.println("Pick "+randomPick+ "is selected for next pick.");
			return randomPick;
		}
		return 0;
	}

}

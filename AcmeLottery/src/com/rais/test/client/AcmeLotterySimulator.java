package com.rais.test.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;
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
	private static final String CUSTOMER_PROP = "customer.properties";
	private static List<String> soldOutAttempts = new ArrayList<String>();
	private static Scanner scanner;

	
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
		List<Customer> customerPool = generateCustomerPool();
		List<Customer> buyersPool = new ArrayList<Customer>();
		boolean isSoldOut = false;
		for (Customer customer : customerPool) {
			Integer picks = getRandomNumberOfTicketPicks(customer);
			if (!pick3SoldOut || !pick4SoldOut || !pick5SoldOut) {
				allocateTicketsToCustomer(customer, picks, pick3, pick4, pick5);
			} else {
				isSoldOut = true;
			}
			if (!isSoldOut) {
				System.out.println("Customer Name: " + customer.getFullName() + " Customer Cart : " + customer.getLotteryCart());
				buyersPool.add(customer);
			}

		}

		Map<Long, LotteryType> winnerMap = drawWinnerLotteries(pick3, pick4, pick5);

		List<Customer> winners = new ArrayList<Customer>();
		findWinningCustomerAndDisplayResults(buyersPool, winnerMap, winners);
		System.out.println("Drawing completed and winners established. ");
		System.out.println("Please refer to the reports for more details. \n");
		String selection = "";
		loadReports(selection, buyersPool, winners, winnerMap);
		
	}

	private static void loadReports(String selection, List<Customer> buyersPool, List<Customer> winners, Map<Long, LotteryType> winnerMap) {
		selection = displayReportSection();
		if(selection.equalsIgnoreCase("1")) {
			System.out.println("The total number of customers that purchased the tickets today is "+buyersPool.size());
		} else if (selection.equalsIgnoreCase("2")){
			displayCustomerTicketTypeReport(buyersPool);
		} else if (selection.equalsIgnoreCase("3")){
			displayCustomerSoldOutTicketInfo();
		} else if (selection.equalsIgnoreCase("4")){
		   displayWinnerDetails(winners);
		} else if (selection.equalsIgnoreCase("5")){
		   displayWinningNumbers(winnerMap);
		} else if (selection.equalsIgnoreCase("6")){
			System.out.println("Exiting the ACME Lottery Simulation. Good Bye.");
			System.exit(0);
		} else {
			System.out.println("Wrong Selection. Please select a valid option.");
		}
		loadReports(selection, buyersPool, winners, winnerMap);
	}

	
	private static void displayWinningNumbers(Map<Long, LotteryType> winnerMap) {
		if (!winnerMap.isEmpty()) {
			System.out.println("------------------------------------");
			System.out.println("| Lottery Category | Winning Number |");
			System.out.println("------------------------------------");
			for (Long number : winnerMap.keySet()) {
				System.out.println("|"+padRight(winnerMap.get(number).toString(),18)+"|"+padRight(String.valueOf(number),16)+"|");
			}
			System.out.println("------------------------------------");
		}
		
	}

	private static void displayWinnerDetails(List<Customer> winners) {
		if (!winners.isEmpty()) {
			System.out.println("----------------------------------------------------------------------------------------");
			System.out.println("|Customer Name                                      | Lottery Category | Lottery Number |");
			System.out.println("----------------------------------------------------------------------------------------");
			for (Customer customer : winners) {
				Map<Long, LotteryType> winningLottery = customer.getWinningLottery();
				for (Long lottery : winningLottery.keySet()) {
					System.out.println("|"+padRight(customer.getFullName(),51)+"|"+padRight(winningLottery.get(lottery).toString(),18)+"|"+padRight(String.valueOf(lottery),16)+"|");
				}
			}
			System.out.println("----------------------------------------------------------------------------------------");
		}
	}

	private static void displayCustomerSoldOutTicketInfo() {
		if(!soldOutAttempts.isEmpty()) {
			System.out.println("-------------------------------------------------------------------------------------------");
			System.out.println("|Customer Name                                      | Pick 3   | Pick 4     | Pick 5       |");
			System.out.println("-------------------------------------------------------------------------------------------");
			for(String info : soldOutAttempts){
				String[] infoSplit = info.split("\\|");
				if(infoSplit[1].equalsIgnoreCase("PICK 3")) {
					System.out.println("|"+padRight(infoSplit[0],51)+"|"+padRight("*",10)+"|"+padRight("",12)+"|"+padRight("",14)+"|");
				} else if(infoSplit[1].equalsIgnoreCase("PICK 4")) {
					System.out.println("|"+padRight(infoSplit[0],51)+"|"+padRight("",10)+"|"+padRight("*",12)+"|"+padRight("",14)+"|");
				} else if(infoSplit[1].equalsIgnoreCase("PICK 5")) {
					System.out.println("|"+padRight(infoSplit[0],51)+"|"+padRight("",10)+"|"+padRight("",12)+"|"+padRight("*",14)+"|");
				}
				System.out.println("-------------------------------------------------------------------------------------------");
			}
		} else {
			System.out.println("No users attempted to buy a sold-out ticket.");
		}
	}

	private static void displayCustomerTicketTypeReport(List<Customer> buyersPool) {
		String blank10 = new String("          ");
		String blank12 = new String("            ");
		String blank14 = new String("              ");
		System.out.println("-------------------------------------------------------------------------------------------");
		System.out.println("|Customer Name                                      | Pick 3   | Pick 4     | Pick 5       |");
		System.out.println("-------------------------------------------------------------------------------------------");
		for(Customer customer: buyersPool){
			String buyerName = customer.getFullName();
			boolean isNamePrinted = false;
			for(Long ticketNumber : customer.getLotteryCart().keySet()){
				if(customer.getLotteryCart().get(ticketNumber) == LotteryType.PICK3){
					System.out.println("|"+padRight(isNamePrinted ? "" : buyerName,51)+"|"+padRight(String.valueOf(ticketNumber),10)+"|"+blank12+"|"+blank14+"|");
					if(!isNamePrinted) {isNamePrinted = true;}
				} else if(customer.getLotteryCart().get(ticketNumber) == LotteryType.PICK4) {
					System.out.println("|"+padRight(isNamePrinted ? "" : buyerName,51)+"|"+blank10+"|"+padRight(String.valueOf(ticketNumber),12)+"|"+blank14+"|");
					if(!isNamePrinted) {isNamePrinted = true;}
				} else if(customer.getLotteryCart().get(ticketNumber) == LotteryType.PICK5) {
					System.out.println("|"+padRight(isNamePrinted ? "" : buyerName,51)+"|"+blank10+"|"+blank12+"|"+padRight(String.valueOf(ticketNumber),14)+"|");
					if(!isNamePrinted) {isNamePrinted = true;}
				}
			}
			System.out.println("-------------------------------------------------------------------------------------------");
		}
	}

	private static String displayReportSection() {
		System.out.println("\n\n");
		System.out.println("*****************************************************************");
		System.out.println("************************ Reports Center *************************");
		System.out.println("*****************************************************************");
		System.out.println("\n\n");
		System.out.println("1. How many customers purchased tickets?");
		System.out.println("2. What type of tickets did each customer purchase?");
		System.out.println("3. Did customers attempt to purchase sold out ticket types?");
		System.out.println("4. Which customers won the drawing for each ticket type?");
		System.out.println("5. Which numbers were selected during the drawing?");
		System.out.println("6. Exit");
		System.out.print("Please enter your selection : ");
		scanner = new Scanner(System.in);
		String selection = scanner.nextLine();
		return selection;
	}

	/**
	 * This operation reads a properties file and generates a customer pool
	 * 
	 * @return
	 */
	private static List<Customer> generateCustomerPool() {
		List<Customer> customers = new ArrayList<Customer>();
		Properties prop = new Properties();
		InputStream input = null;
		try {
    		input = AcmeLotterySimulator.class.getClassLoader().getResourceAsStream(CUSTOMER_PROP);
    		if(input==null){
    	        System.out.println("Sorry, unable to find " + CUSTOMER_PROP);
    		    return new ArrayList<Customer>();
    		}
			// load a properties file
			prop.load(input);
			// get the property value and print it out
			for(int i=1; i<= prop.size(); i++){
				String customerString = prop.getProperty(String.valueOf(i));
				String[] customerData = customerString.split("\\|");
				Customer customer = new Customer(customerData[0], customerData[1], customerData[2], customerData[3]);
				customers.add(customer);
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return customers;
	}

	/**
	 * This operation checks for the winning customer and displays the end
	 * results
	 * 
	 * @param customerPool
	 * @param winnerMap
	 */
	private static void findWinningCustomerAndDisplayResults(List<Customer> customerPool, Map<Long, LotteryType> winnerMap, List<Customer> winners) {
		/** The simulation of picking up the winner based on winner lotteries **/
		for (Customer customer : customerPool) {
			Map<Long, LotteryType> customerLotteryCart = customer.getLotteryCart();
			Set<Long> customerLotteries = customerLotteryCart.keySet();
			Set<Long> winnerSet = winnerMap.keySet();
			Set<Long> customerWinnerSet = new HashSet<Long>();
			for (Long winner : winnerSet) {
				if (customerLotteries.contains(winner)) {
					customerWinnerSet.add(winner);
				}
			}
			if (customerWinnerSet.size() > 0) {
				Map<Long, LotteryType> winningLottery = new HashMap<Long, LotteryType>();
				for (Long lotteryNumber : customerWinnerSet) {
					winningLottery.put(lotteryNumber, winnerMap.get(lotteryNumber));
				}
				customer.setWinningLottery(winningLottery);
				winners.add(customer);
			}
		}
	}

	/**
	 * This operation allocates the tickets to the customers in a random manner
	 * from all the lottery pools
	 * 
	 * @param customer
	 * @param picks
	 * @param pick3Pool
	 * @param pick4Pool
	 * @param pick5Pool
	 */
	private static boolean allocateTicketsToCustomer(Customer customer, Integer picks, Pick3 pick3, Pick4 pick4, Pick5 pick5) {
		Map<Long, LotteryType> lotteryCart = new HashMap<Long, LotteryType>();

		for (int i = 0; i < picks; i++) {
			Integer randomPick = getRandomPickPool();
			Long lottery = 0L;
			if (randomPick == 3) {
				lottery = pick3.pickRandomLottery();
				if (lottery != 0L && !pick3SoldOut) {
					lotteryCart.put(lottery, LotteryType.PICK3);
				} else {
					pick3SoldOut = true;
					soldOutAttempts.add(customer.getFullName()+"|PICK 3");
				}
			} else if (randomPick == 4) {
				lottery = pick4.pickRandomLottery();
				if (lottery != 0L && !pick4SoldOut) {
					lotteryCart.put(lottery, LotteryType.PICK4);
				} else {
					pick4SoldOut = true;
					soldOutAttempts.add(customer.getFullName()+"|PICK 4");
				}
			} else if (randomPick == 5) {
				lottery = pick5.pickRandomLottery();
				if (lottery != 0L && !pick5SoldOut) {
					lotteryCart.put(lottery, LotteryType.PICK5);
				} else {
					pick5SoldOut = true;
					soldOutAttempts.add(customer.getFullName()+"|PICK 5");
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
	 * 
	 * @param customer
	 * @return
	 */
	private static Integer getRandomNumberOfTicketPicks(Customer customer) {
		List<Integer> numberOfPicks = Arrays.asList(1, 2, 3, 4, 5);
		int index = new Random().nextInt(numberOfPicks.size());
		Integer randomPick = numberOfPicks.get(index);
		System.out.println("Customer  " + customer.getFullName() + " gets to pick " + randomPick + " lotteries");
		return randomPick;
	}

	/**
	 * This operation returns the random number of picks for a customer.
	 * 
	 * @param customerID
	 * @return
	 */
	private static Integer getRandomPickPool() {
		List<Integer> numberOfPicks = new ArrayList<Integer>();
		if (!pick3SoldOut && !pick4SoldOut && !pick5SoldOut) {
			numberOfPicks = Arrays.asList(3, 4, 5);
		} else if (!pick3SoldOut && !pick4SoldOut && pick5SoldOut) {
			numberOfPicks = Arrays.asList(3, 4);
		} else if (!pick3SoldOut && pick4SoldOut && !pick5SoldOut) {
			numberOfPicks = Arrays.asList(3, 5);
		} else if (pick3SoldOut && !pick4SoldOut && !pick5SoldOut) {
			numberOfPicks = Arrays.asList(4, 5);
		} else if (pick3SoldOut && pick4SoldOut && !pick5SoldOut) {
			System.out.println("Pick 5 is selected for next pick.");
			return 5;
		} else if (!pick3SoldOut && pick4SoldOut && pick5SoldOut) {
			System.out.println("Pick 3 is selected for next pick.");
			return 3;
		} else if (pick3SoldOut && !pick4SoldOut && pick5SoldOut) {
			System.out.println("Pick 4 is selected for next pick.");
			return 4;
		}
		if (!numberOfPicks.isEmpty()) {
			int index = new Random().nextInt(numberOfPicks.size());
			Integer randomPick = numberOfPicks.get(index);
			System.out.println("Pick " + randomPick + "is selected for next pick.");
			return randomPick;
		}
		return 0;
	}

	public static String padRight(String s, int n) {
	     return String.format("%1$-" + n + "s", s);  
	}

	public static String padLeft(String s, int n) {
	    return String.format("%1$" + n + "s", s);  
	}
	
}

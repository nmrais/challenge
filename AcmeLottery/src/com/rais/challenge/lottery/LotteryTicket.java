package com.rais.challenge.lottery;

public interface LotteryTicket {

	/**
	 * This operation generates the Lottery Number
	 * @return
	 */
	public Long generateLotteryNumber();
	/**
	 * This operation determines the category of the lottery ticket
	 * e.g. Pick 3, Pick 4 or Pick 5
	 * @return
	 */
	public String lotteryCategory();
	/**
	 * This operation returns the Prize Amount on the lottery
	 * @return
	 */
	public Integer prizeValue();
	/**
	 * This operation returns the total number of lotteries left in the pool
	 * for the given category
	 * @return
	 */
	public int numberOfLotteries();
	/**
	 * This operation returns the winning Lottery Number
	 * @return
	 */
	public Long drawWinner();
}

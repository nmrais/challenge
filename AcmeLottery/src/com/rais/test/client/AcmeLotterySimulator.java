package com.rais.test.client;

import com.rais.challenge.impl.Pick3;
import com.rais.challenge.impl.Pick4;
import com.rais.challenge.impl.Pick5;

public class AcmeLotterySimulator {
	
	public static void main(String[] hh) {
		System.out.println("Lottery Initialized. \n");
		Pick3 pick3 = new Pick3();
		Pick4 pick4 = new Pick4();
		Pick5 pick5 = new Pick5();
		System.out.println("Lottery Drawing Initialized. \n");
		pick3.drawWinner();
		pick4.drawWinner();
		pick5.drawWinner();
	}

}

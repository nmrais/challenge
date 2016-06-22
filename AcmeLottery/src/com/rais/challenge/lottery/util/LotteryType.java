package com.rais.challenge.lottery.util;

public enum LotteryType {
PICK3, PICK4, PICK5;

public static String getLabel(LotteryType lotteryType) {
	String label=null;
	if(lotteryType.equals(LotteryType.PICK3)){
		label = "PICK 3";
	} else if (lotteryType.equals(LotteryType.PICK4)) {
		label = "PICK 4";
	} else if (lotteryType.equals(LotteryType.PICK5)) {
		label = "PICK 5";
	}
	return label;
}

}



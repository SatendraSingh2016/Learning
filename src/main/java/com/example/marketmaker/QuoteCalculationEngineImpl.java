package com.example.marketmaker;

public class QuoteCalculationEngineImpl implements QuoteCalculationEngine{

	@Override
	public double calculateQuotePrice(int securityId, double referencePrice,
			boolean buy, int quantity) {
		// TODO As of now not sure how to calculate it so returning the 
		//TODO: ref price only. 
		
		if(buy){
			//get ask/offer price
		}else{
			//get bid price
		}
		return referencePrice;
	}

}

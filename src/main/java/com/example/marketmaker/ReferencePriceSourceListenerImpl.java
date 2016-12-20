package com.example.marketmaker;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * This class is implemented to keep the updated reference prices for
 * security id.
 * 
 * It is assume that when ever the any prices change received from market
 * for respective security id then it will be cached and that will be used
 * to calculate quote price.
 * 
 * Also assume that there will be only one listener which will be subscribe.
 *  
 * @author Satendra
 *
 */
public class ReferencePriceSourceListenerImpl implements 
											ReferencePriceSourceListener{
	
	// whenever the price is being changed, updating the latest reference
	// price
	private final ConcurrentMap<Integer, Double> 
		secIdRefPriceMap = new ConcurrentHashMap<>();
	

	public ReferencePriceSourceListenerImpl(
			ReferencePriceSource refPriceSource){
		refPriceSource.subscribe(this);
	}
	
	@Override
	public void referencePriceChanged(int securityId, double price) {
		secIdRefPriceMap.put(securityId, price);	
	}

	/**
	 * getting the reference price for security id
	 * 
	 * @param securityId
	 * @return reference price for given security id
	 */
	public double getUpdatedRefPriceForSecurityId(int securityId){
		return secIdRefPriceMap.get(securityId)==null ?
				0D : secIdRefPriceMap.get(securityId);
	}
}

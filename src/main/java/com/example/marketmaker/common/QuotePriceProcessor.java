package com.example.marketmaker.common;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import com.example.marketmaker.QuoteCalculationEngine;
import com.example.marketmaker.QuoteCalculationEngineImpl;
import com.example.marketmaker.ReferencePriceSource;
import com.example.marketmaker.ReferencePriceSourceImpl;
import com.example.marketmaker.ReferencePriceSourceListenerImpl;

public class QuotePriceProcessor implements Runnable{
	
	private final BlockingQueue<QuoteRequest> quoteRequestQueue;
	private final ServerStatus serverstatus;
	
	private QuoteCalculationEngine quoteCalculationEngine;
	private ReferencePriceSource referencePriceSource;
	
	public QuotePriceProcessor(BlockingQueue<QuoteRequest> quoteRequestQueue,
			ServerStatus serverstatus){
		init();
		this.quoteRequestQueue = quoteRequestQueue;
		this.serverstatus = serverstatus;
	}
	
	/**
	 * initialize the quote pricing calculation utility
	 */
	private void init(){
		quoteCalculationEngine = new QuoteCalculationEngineImpl();
		referencePriceSource = new ReferencePriceSourceImpl();
		new ReferencePriceSourceListenerImpl(referencePriceSource);
	}

	@Override
	public void run() {

		while(serverstatus == ServerStatus.RUNNING){
			try {

				QuoteRequest request = quoteRequestQueue.take();
				if(request.getSocket().isConnected()){
					Double quotePrice = 0D;
					/**
					 * Here exactly not sure to which API use to get the
					 * quote price,
					 * 
					 * Assuming, I will get market data and if the side is 
					 *  buy then return the quote price as ask/offer 
					 *  
					 *if the side is sell then return quote price as bid
					 */
					double refPrice = referencePriceSource.get(request.getSecurityId());
					
					/**
					 * __________________________________________
					 * 
					 * ___Below is calculation for QuotePrice____
					 * 
					 * Not much sure about business calculation
					 * I need to learn the business in more depth
					 * 
					 */
					quotePrice = quoteCalculationEngine.calculateQuotePrice(
							request.getSecurityId(), 
							refPrice, 
							BuySell.BUY.equals(request.getBuy_sell())?true:false, 
							request.getquantity());
					try {
						request.getSocket().getOutputStream()
						.write(quotePrice.toString().getBytes());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
}

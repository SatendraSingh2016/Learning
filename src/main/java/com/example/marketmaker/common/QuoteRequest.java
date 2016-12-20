package com.example.marketmaker.common;

import java.net.Socket;


public final class QuoteRequest {
		
	private final int securityId;
	private final int quantity;
	private final BuySell buySell;
	private final Socket socket;
	
	public QuoteRequest(int securityId, int quantity, BuySell buySell,
			Socket socket){
		this.securityId = securityId;
		this.quantity = quantity;
		this.buySell = buySell;
		this.socket = socket;
	}

	public int getSecurityId() {
		return securityId;
	}

	public int getquantity() {
		return quantity;
	}

	public BuySell getBuy_sell() {
		return buySell;
	}

	public Socket getSocket() {
		return socket;
	}
	
	@Override
	public String toString(){
		StringBuilder sb =  new StringBuilder();
		sb.append(securityId).append('_');
		sb.append(buySell.name()).append('_');
		sb.append(quantity);
		return sb.toString();
	}
}

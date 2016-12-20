package com.example.marketmaker.common;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;


/**
 * This class is used to receive socket data from socket and 
 * parse it into quote request object
 * 
 * @author satendra
 *
 */
public final class SocketReader {
	
	private byte []rawData;
	private StringBuilder sb;
	private int readByte;
	private final int capacity;
	private final BlockingQueue<QuoteRequest> quoteReqQ;
	
	public SocketReader(int capacity, BlockingQueue<QuoteRequest> quoteReqQ){
		this.rawData = new byte[capacity];
		sb = new StringBuilder();
		this.capacity = capacity;
		this.quoteReqQ = quoteReqQ;
	}
	
	/**
	 * method is receiving the byte block data from socket and parsing it into the 
	 * quote request object.
	 *
	 * @param is
	 * @param beginStr
	 * @param msgList
	 * @throws Exception  
	 */
	
	public void readMultipleMessagesFromSocket(Socket client) throws IOException{
		if(client == null || !client.isConnected()) {
			throw new IOException("socket closed.");
		}
		readByte = client.getInputStream().read(rawData, 0, capacity);
		if(readByte == -1){
			throw new IOException("socket closed.");
		}
		sb.append(new String(rawData, 0, readByte));
		
		int sbLen = sb.length();
		int readDone = 0;
		int readIndex = sb.indexOf("\u0001",readDone);
		
		while(readIndex > -1){
			if(sbLen >= readIndex+1){
				String temp = sb.substring(readDone, readIndex);
				try {
					/**
					 * the quote request string format is changed as below format
					 * <securityId>_<B|S>_<quantity>SOH
					 * i.e. 123_B_1000SOH
					 */
					int idx = temp.indexOf("_");
					int securityId = Integer.parseInt(temp.substring(0, idx));
					BuySell buySell = temp.charAt(idx+1)=='B' ?
							BuySell.BUY : BuySell.SELL;
					int quantity = Integer.parseInt(temp.substring(idx+3));
					QuoteRequest quoteRequest = new QuoteRequest(securityId, 
							quantity, buySell, client);
					quoteReqQ.put(quoteRequest);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				readDone=readIndex+1;
			}else{
				sb.delete(0, readDone);
				break;
			}
			readIndex = sb.indexOf("\u0001",readDone);
			if(readIndex == -1){
				sb.delete(0, readDone);
			}
		}
	}
}

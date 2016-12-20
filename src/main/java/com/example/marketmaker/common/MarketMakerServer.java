package com.example.marketmaker.common;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * this class is used to create the server connection and able to handle
 * the multiple client connect at the moment. 
 * 
 * @author Satendra
 *
 */
public class MarketMakerServer{
	
	private ServerSocket serverSocket;
	private volatile ServerStatus serverStatus;
	private final BlockingQueue<QuoteRequest> quoteReqQ;
	private final Thread quotePriceProcessor;
	
	public MarketMakerServer(int port, int maxConn) {
		this.quoteReqQ = new LinkedBlockingQueue<>();
		this.serverStatus = ServerStatus.RUNNING;
		this.quotePriceProcessor = 
				new Thread(new QuotePriceProcessor(quoteReqQ, serverStatus));
		try {
			serverSocket = new ServerSocket(port, maxConn);
			serverSocket.setReuseAddress(true);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1); //exit if server is not able to bind service
		}
	}
	
	/**
	 * this method is used to accept the client connection and handling the 
	 * each client with different thread
	 */
	public void startServer(){
		quotePriceProcessor.start();
		while(serverStatus == ServerStatus.RUNNING){
			System.out.println("waiting for client connection request ...");
			try {
				final Socket client = serverSocket.accept();
				new Thread(new QuoteRequestHandler(serverStatus, client, quoteReqQ)).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void stopServer(){
		serverStatus = ServerStatus.STOPPED;
		try {
			quotePriceProcessor.interrupt();
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

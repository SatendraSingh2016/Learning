package com.example.marketmaker.common;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.example.marketmaker.ReferencePriceSource;

/**
 * this class is used to create the server connection and able to handle
 * the multiple client connect at the moment. 
 * 
 * @author Satendra
 *
 */
public class MarketMakerServer{
	
	private ServerSocket serverSocket;
	private final BlockingQueue<QuoteRequest> quoteReqQ;
	private volatile ServerStatus serverStatus;
	private final Thread quotePriceProcessor;
	
	public MarketMakerServer(int port, int maxConn, 
			ReferencePriceSource referencePriceSource) {
		
		this.quoteReqQ = new LinkedBlockingQueue<>();
		serverStatus = ServerStatus.RUNNING;
		this.quotePriceProcessor = 
				new Thread(new QuotePriceProcessor(quoteReqQ, serverStatus, 
						  referencePriceSource));
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
		new Thread(){
			@Override
			public void run(){
				quotePriceProcessor.start();
				while(serverStatus.isRunning()){
					try {
						System.out.println("Waiting for client...");
						final Socket client = serverSocket.accept();
						if(client!= null && client.isConnected())
						new Thread(new QuoteRequestHandler(serverStatus, client, quoteReqQ)).start();
					} catch (IOException e) {
						e.printStackTrace();						
					}
				}
			}
		}.start();
	}
	
	public void stopServer(){
		serverStatus.setRunning(false);
		try {
			quotePriceProcessor.interrupt();
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

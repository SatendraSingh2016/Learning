package com.example.marketmaker.common;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;

public class QuoteRequestHandler implements Runnable {

	private final ServerStatus serverStatus;
	private final Socket client;
	private final SocketReader socketReader;
	
	private static final int READ_BUFFER = 1024;
	private static final int RECV_BUFFER_SOCK = 102400;
	private static final int SEND_BUFFER_SOCK = 102400;
	
	public QuoteRequestHandler(ServerStatus serverStatus, Socket client, 
			BlockingQueue<QuoteRequest> quotReqQ){
		this.serverStatus = serverStatus;
		this.client = client;
		try {
			client.setKeepAlive(true);
			client.setTcpNoDelay(true);
			client.setReceiveBufferSize(RECV_BUFFER_SOCK);
			client.setSendBufferSize(SEND_BUFFER_SOCK);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		this.socketReader = new SocketReader(READ_BUFFER, quotReqQ);		
	}
	
	@Override
	public void run() {
		while(serverStatus.isRunning()){
			try {
				socketReader.readMultipleMessagesFromSocket(client);				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	


}

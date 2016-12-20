package com.example.marketmaker;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.example.marketmaker.common.MarketMakerServer;

public class MarketMakerServerTest {
	
	private MarketMakerServer server;
	
	private final int MAX_CONN = 500;
	private final int PORT = 1234;
	private final String HOST = "localhost";
	
	private ReferencePriceSource referencePriceSource = 
			new ReferencePriceSourceImpl();
	
	@Before
	public void setup(){
		new Thread(){
			@Override
			public void run(){
				new ReferencePriceSourceListenerImpl(referencePriceSource);
				server = new MarketMakerServer(PORT, MAX_CONN, referencePriceSource);
				server.startServer();
			}
		}.start();
	}
	
	@After
	public void cleanup(){
		server.stopServer();
		server=null;
	}
	
	@Test
	public void canSendQuoteRequestTest(){
		
		try {
			try {
				Thread.sleep(2000); // ensuring to server is uptodate
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("client sending connection request");
			@SuppressWarnings("resource")
			Socket client = new Socket(HOST, PORT);
			client.setKeepAlive(true);
			client.setTcpNoDelay(true);
			for(int i=0; i<1000; i++){
				StringBuilder sb = new StringBuilder();
				sb.append(1000+i);
				sb.append("_B_");
				sb.append(1000);
				sb.append('\u0001');
				client.getOutputStream().write(sb.toString().getBytes());
			}
			try {
				Thread.sleep(1000); // ensuring to server read done
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//client.close();
			try {
				Thread.sleep(10000); // ensuring to server read done
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void canMultipleClientSendQuoteRequestTest(){
			Runnable run = new Runnable(){

				@Override
				public void run() {
					try {
						try {
							Thread.sleep(2000); // ensuring to server is uptodate
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						System.out.println("client sending connection request");

						@SuppressWarnings("resource")
						Socket client = new Socket(HOST, PORT);
						client.setKeepAlive(true);
						client.setTcpNoDelay(true);
						for(int i=0; i<1000; i++){
							StringBuilder sb = new StringBuilder();
							sb.append(1000+i);
							sb.append("_B_");
							sb.append(1000);
							sb.append('\u0001');
							client.getOutputStream().write(sb.toString().getBytes());
						}
						try {
							Thread.sleep(1000); // ensuring to server read done
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						//client.close();
						try {
							Thread.sleep(10000); // ensuring to server read done
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
				
					} catch (UnknownHostException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}
			};
			
			new Thread(run).start();
			new Thread(run).start();
			new Thread(run).start();
	}

}

package com.example.marketmaker;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.example.marketmaker.common.MarketMakerServer;

public class MarketMakerServerTest {
	
	private static MarketMakerServer server;
	
	private static final int MAX_CONN = 100;
	private static final int PORT = 1234;
	private static final String HOST = "localhost";
	
	private static ReferencePriceSource referencePriceSource = 
			new ReferencePriceSourceImpl();
	
	@BeforeClass
	public static void setup(){
		new ReferencePriceSourceListenerImpl(referencePriceSource);
		server = new MarketMakerServer(PORT, MAX_CONN, referencePriceSource);
		server.startServer();
		System.out.println("Calling setup....");
	}
	
	@AfterClass
	public static void cleanup(){
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

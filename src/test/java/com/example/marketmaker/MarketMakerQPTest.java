package com.example.marketmaker;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.example.marketmaker.common.MarketMakerServer;
import com.example.marketmaker.ReferencePriceSourceImpl;
import com.example.marketmaker.ReferencePriceSource;

public class MarketMakerQPTest {
	
	private MarketMakerServer server;
				
	private final int MAX_CONN = 500;
	private final int PORT = 1234;
	private final String HOST = "localhost";
	private final ReferencePriceSource referencePriceSource =
			new ReferencePriceSourceImpl();
	
	@Before
	public void setup(){
		new ReferencePriceSourceListenerImpl(referencePriceSource);
		server = new MarketMakerServer(PORT, MAX_CONN, referencePriceSource);
		server.startServer();
	}
	
	@After
	public void cleanup(){
		server.stopServer();
		server=null;
	}
	
	@Test
	public void canSendQuoteRequestAndGetQuotePriceTest(){	
		try {
			try {
				Thread.sleep(2000); // ensuring to server is uptodate
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("client sending connection request");
			@SuppressWarnings("resource")
			final Socket client = new Socket(HOST, PORT);
			new Thread(new Runnable() {
				
				@Override
				public void run() {
						byte b[] = new byte[20];
						try {
							client.getInputStream().read(b);
						} catch (IOException e) {
							e.printStackTrace();
						}
						System.out.println("GOT from Server: "+ new String(b));
						Double hardCodedQP =123.50D;
						Assert.assertEquals(hardCodedQP, new Double(new String(b)));
				}
			}).start();
			client.setKeepAlive(true);
			client.setTcpNoDelay(true);
			
			//Setting Price
			((ReferencePriceSourceImpl)referencePriceSource)
			.updateRefPriceForSecurityId(1234, 123.50D);
			
			StringBuilder sb = new StringBuilder();
				sb.append(1234);
				sb.append("_B_");
				sb.append(1000);
				sb.append('\u0001');
				client.getOutputStream().write(sb.toString().getBytes());
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
}

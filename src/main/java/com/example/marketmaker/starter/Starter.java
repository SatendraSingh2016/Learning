package com.example.marketmaker.starter;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.example.marketmaker.common.MarketMakerServer;

public class Starter {
	
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        
        AnnotationConfigApplicationContext context = 
        		new AnnotationConfigApplicationContext("com.example.marketmaker.starter");
        
        MarketMakerServer server = (MarketMakerServer) context.getBean("marketMakerServer");
        
       server.startServer();
        //TODO need proper handling to start/stop the application via JMX control
        //TODO will implement JMX/Mbean exporter once get some time 
        try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        server.stopServer();
    }
}

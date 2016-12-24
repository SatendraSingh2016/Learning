package com.example.marketmaker.starter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import com.example.marketmaker.ReferencePriceSource;
import com.example.marketmaker.ReferencePriceSourceListener;
import com.example.marketmaker.common.MarketMakerServer;

@Component
@Import(MarketDataConfig.class)
public class Configuration {

	@Autowired
	@Qualifier("referencePriceSource")
	private ReferencePriceSource referencePriceSource;
	
	@Autowired
	@Qualifier("referencePriceSourceListener")
	private ReferencePriceSourceListener referencePriceSourceListener;
		
	@Bean(name="marketMakerServer")
	public MarketMakerServer marketMakerServer(){
		int port = 1234;
		int maxLiveConnection = 1000;
		return new MarketMakerServer(port, maxLiveConnection, referencePriceSource);
	}
	
	
}

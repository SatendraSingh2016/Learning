package com.example.marketmaker.starter;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.example.marketmaker.ReferencePriceSource;
import com.example.marketmaker.ReferencePriceSourceImpl;
import com.example.marketmaker.ReferencePriceSourceListener;
import com.example.marketmaker.ReferencePriceSourceListenerImpl;

@Component
public class MarketDataConfig {

	@Bean(name="referencePriceSource")
	public ReferencePriceSource referencePriceSource(){
		return new ReferencePriceSourceImpl();
	}
	
	@Bean(name="referencePriceSourceListener")
	public ReferencePriceSourceListener referencePriceSourceListener(){
		return new ReferencePriceSourceListenerImpl(referencePriceSource());
	}
	
}

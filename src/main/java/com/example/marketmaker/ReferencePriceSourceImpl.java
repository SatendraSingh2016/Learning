package com.example.marketmaker;

public class ReferencePriceSourceImpl implements ReferencePriceSource{

	ReferencePriceSourceListener listener;

	public ReferencePriceSourceImpl(){ }
	
	@Override
	public void subscribe(ReferencePriceSourceListener listener) {
		if(listener== null){
			throw new NullPointerException();
		}
		this.listener = listener;
	}

	/**
	 * returning the refPrice for security id
	 */
	@Override
	public double get(int securityId) {
		return ((ReferencePriceSourceListenerImpl)listener)
				.getUpdatedRefPriceForSecurityId(securityId);
	}
	
	
	/**
	 * Assume that, this method is reading the price from market data service
	 * 
	 * @param securityId
	 * @param refPrice
	 */
	public void updateRefPriceForSecurityId(int securityId, double refPrice){
		listener.referencePriceChanged(securityId, refPrice);
	}

}

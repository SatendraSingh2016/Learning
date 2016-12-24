package com.example.marketmaker;

/**
 * Source for reference prices.
 */
public interface ReferencePriceSource {
    /**
     * Subscribe to changes to reference prices.
     *
     * @param listener callback interface for changes
     */
    void subscribe(ReferencePriceSourceListener listener);

    double get(int securityId);
}

package com.progressoft.fxdeals.services;

import com.progressoft.fxdeals.dto.FxDealRequest;
import com.progressoft.fxdeals.dto.FxDealResponse;
import com.progressoft.fxdeals.exceptions.DuplicateDataException;
import com.progressoft.fxdeals.exceptions.ValidationException;

import java.time.LocalDateTime;
import java.util.List;

public interface FxDealService {

    /**
     * Creates a new FX deal
     * @param request the FX deal request
     * @return the created FX deal response
     * @throws ValidationException if validation fails
     * @throws DuplicateDataException if deal already exists
     */
    FxDealResponse createFxDeal(FxDealRequest request);

    /**
     * Retrieves all FX deals
     * @return list of all FX deals
     */
    List<FxDealResponse> getAllFxDeals();

    /**
     * Retrieves FX deal by unique ID
     * @param dealUniqueId the unique deal ID
     * @return the FX deal response if found
     * @throws ValidationException if deal not found
     */
    FxDealResponse getFxDealByUniqueId(Long dealUniqueId);

    /**
     * Retrieves FX deals by currency pair
     * @param fromCurrency source currency
     * @param toCurrency target currency
     * @return list of FX deals for the currency pair
     */
    List<FxDealResponse> getFxDealsByCurrencyPair(String fromCurrency, String toCurrency);

    /**
     * Retrieves FX deals within a date range
     * @param startDate start of date range
     * @param endDate end of date range
     * @return list of FX deals within the date range
     */
    List<FxDealResponse> getFxDealsByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Retrieves recent FX deals
     * @param limit number of recent deals to retrieve
     * @return list of recent FX deals
     */
    List<FxDealResponse> getRecentFxDeals(int limit);

    /**
     * Counts total number of FX deals
     * @return total count of FX deals
     */
    long getTotalFxDealsCount();

    /**
     * Checks if a deal exists by unique ID
     * @param dealUniqueId the unique deal ID
     * @return true if deal exists, false otherwise
     */
    boolean existsByDealUniqueId(String dealUniqueId);
}
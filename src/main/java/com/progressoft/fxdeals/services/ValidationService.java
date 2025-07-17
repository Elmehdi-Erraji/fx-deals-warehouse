package com.progressoft.fxdeals.services;

import com.progressoft.fxdeals.dto.FxDealRequest;
import com.progressoft.fxdeals.exceptions.ValidationException;
import java.math.BigDecimal;

public interface ValidationService {

    /**
     * Validates FX deal request
     * @param request the FX deal request to validate
     * @throws ValidationException if validation fails
     */
    void validateFxDealRequest(FxDealRequest request);

    /**
     * Validates if the deal amount is reasonable for the currency pair
     * @param amount the deal amount
     * @param fromCurrency source currency
     * @param toCurrency target currency
     * @return true if amount is reasonable, false otherwise
     */
    boolean isReasonableAmount(BigDecimal amount, String fromCurrency, String toCurrency);
}
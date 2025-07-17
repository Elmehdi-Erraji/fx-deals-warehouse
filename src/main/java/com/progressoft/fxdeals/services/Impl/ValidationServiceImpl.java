package com.progressoft.fxdeals.services.Impl;

import com.progressoft.fxdeals.dto.FxDealRequest;
import com.progressoft.fxdeals.exceptions.ValidationException;
import com.progressoft.fxdeals.services.ValidationService;
import com.progressoft.fxdeals.utils.CurrencyValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ValidationServiceImpl implements ValidationService {

    private static final Logger logger = LoggerFactory.getLogger(ValidationServiceImpl.class);

    @Override
    public void validateFxDealRequest(FxDealRequest request) {
        logger.debug("Validating FX deal request: {}", request);

        List<String> errors = new ArrayList<>();

        validateDealUniqueId(request.getDealUniqueId(), errors);

        validateCurrencies(request.getFromCurrency(), request.getToCurrency(), errors);


        validateTimestamp(request.getDealTimestamp(), errors);

        validateAmount(request.getDealAmount(), errors);

        if (!errors.isEmpty()) {
            String errorMessage = "Validation failed: " + String.join(", ", errors);
            logger.error("Validation errors: {}", errorMessage);
            throw new ValidationException(errorMessage);
        }

        logger.debug("FX deal request validation successful");
    }

    @Override
    public boolean isReasonableAmount(BigDecimal amount, String fromCurrency, String toCurrency) {

        if (amount.compareTo(new BigDecimal("0.01")) < 0) {
            return false;
        }

        if (amount.compareTo(new BigDecimal("100000000")) > 0) {
            return false;
        }

        return true;
    }

    /**
     * Validates deal unique ID
     */
    private void validateDealUniqueId(String dealUniqueId, List<String> errors) {
        if (dealUniqueId == null || dealUniqueId.trim().isEmpty()) {
            errors.add("Deal unique ID is required");
            return;
        }

        if (dealUniqueId.length() > 255) {
            errors.add("Deal unique ID cannot exceed 255 characters");
        }

        if (!dealUniqueId.matches("^[a-zA-Z0-9_-]+$")) {
            errors.add("Deal unique ID can only contain alphanumeric characters, hyphens, and underscores");
        }
    }

    /**
     * Validates currency codes
     */
    private void validateCurrencies(String fromCurrency, String toCurrency, List<String> errors) {
        if (fromCurrency == null || fromCurrency.trim().isEmpty()) {
            errors.add("From currency is required");
        } else if (!CurrencyValidator.isValidCurrency(fromCurrency)) {
            errors.add("From currency must be a valid ISO 4217 currency code");
        }

        if (toCurrency == null || toCurrency.trim().isEmpty()) {
            errors.add("To currency is required");
        } else if (!CurrencyValidator.isValidCurrency(toCurrency)) {
            errors.add("To currency must be a valid ISO 4217 currency code");
        }

        if (fromCurrency != null && toCurrency != null &&
                fromCurrency.equalsIgnoreCase(toCurrency)) {
            errors.add("From currency and to currency must be different");
        }
    }

    /**
     * Validates deal timestamp
     */
    private void validateTimestamp(LocalDateTime dealTimestamp, List<String> errors) {
        if (dealTimestamp == null) {
            errors.add("Deal timestamp is required");
            return;
        }

        if (dealTimestamp.isAfter(LocalDateTime.now())) {
            errors.add("Deal timestamp cannot be in the future");
        }

        LocalDateTime oneYearAgo = LocalDateTime.now().minusYears(1);
        if (dealTimestamp.isBefore(oneYearAgo)) {
            errors.add("Deal timestamp cannot be older than one year");
        }
    }

    /**
     * Validates deal amount
     */
    private void validateAmount(BigDecimal dealAmount, List<String> errors) {
        if (dealAmount == null) {
            errors.add("Deal amount is required");
            return;
        }

        if (dealAmount.compareTo(BigDecimal.ZERO) <= 0) {
            errors.add("Deal amount must be greater than zero");
        }

        BigDecimal maxAmount = new BigDecimal("999999999999999.9999");
        if (dealAmount.compareTo(maxAmount) > 0) {
            errors.add("Deal amount exceeds maximum allowed value");
        }

        if (dealAmount.scale() > 4) {
            errors.add("Deal amount cannot have more than 4 decimal places");
        }
    }
}
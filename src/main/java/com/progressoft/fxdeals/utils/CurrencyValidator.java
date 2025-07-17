package com.progressoft.fxdeals.utils;

import java.util.Set;

public class CurrencyValidator {

    private static final Set<String> VALID_CURRENCIES = Set.of(
            "USD", "EUR", "GBP", "JPY", "CHF", "AUD", "CAD", "NZD", "SEK", "NOK", "DKK",
            "PLN", "CZK", "HUF", "BGN", "RON", "HRK", "RUB", "CNY", "HKD", "SGD", "KRW",
            "INR", "BRL", "MXN", "ZAR", "TRY", "THB", "MYR", "IDR", "PHP", "VND", "EGP",
            "SAR", "AED", "QAR", "KWD", "BHD", "OMR", "JOD", "LBP", "ILS", "DZD", "MAD",
            "TND", "LYD", "NGN", "GHS", "KES", "UGX", "TZS", "RWF", "ETB", "XOF", "XAF"
    );

    /**
     * Validates if a currency code is valid ISO 4217 format
     * @param currencyCode the currency code to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidCurrency(String currencyCode) {
        if (currencyCode == null || currencyCode.trim().isEmpty()) {
            return false;
        }

        String upperCaseCode = currencyCode.toUpperCase().trim();

        // Check format: exactly 3 alphabetic characters
        if (!upperCaseCode.matches("^[A-Z]{3}$")) {
            return false;
        }

        return VALID_CURRENCIES.contains(upperCaseCode);
    }

    /**
     * Validates if both currencies are valid and different
     * @param fromCurrency source currency
     * @param toCurrency target currency
     * @return true if both are valid and different, false otherwise
     */
    public static boolean isValidCurrencyPair(String fromCurrency, String toCurrency) {
        if (!isValidCurrency(fromCurrency) || !isValidCurrency(toCurrency)) {
            return false;
        }

        return !fromCurrency.equalsIgnoreCase(toCurrency);
    }

    /**
     * Normalizes currency code to uppercase
     * @param currencyCode the currency code to normalize
     * @return normalized currency code or null if invalid
     */
    public static String normalizeCurrency(String currencyCode) {
        if (currencyCode == null) {
            return null;
        }

        String normalized = currencyCode.toUpperCase().trim();
        return isValidCurrency(normalized) ? normalized : null;
    }

    /**
     * Gets all supported currency codes
     * @return Set of supported currency codes
     */
    public static Set<String> getSupportedCurrencies() {
        return VALID_CURRENCIES;
    }
}
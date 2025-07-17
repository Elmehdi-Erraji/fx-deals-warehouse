package com.progressoft.fxdeals.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Setter
@Getter
public class FxDealRequest {


    @NotBlank(message = "Deal unique ID is required")
    @Size(max = 255, message = "Deal unique ID cannot exceed 255 characters")
    @JsonProperty("dealUniqueId")
    private String dealUniqueId;

    @NotBlank(message = "From currency is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "From currency must be a valid 3-letter ISO currency code")
    @JsonProperty("fromCurrency")
    private String fromCurrency;

    @NotBlank(message = "To currency is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "To currency must be a valid 3-letter ISO currency code")
    @JsonProperty("toCurrency")
    private String toCurrency;


    @NotNull(message = "Deal timestamp is required")
    @PastOrPresent(message = "Deal timestamp cannot be in the future")
    @JsonProperty("dealTimestamp")
    private LocalDateTime dealTimestamp;


    @NotNull(message = "Deal amount is required")
    @DecimalMin(value = "0.0001", inclusive = true, message = "Deal amount must be greater than 0")
    @Digits(integer = 15, fraction = 4, message = "Deal amount must have at most 15 integer digits and 4 decimal places")
    @JsonProperty("dealAmount")
    private BigDecimal dealAmount;

    public FxDealRequest() {}

    public FxDealRequest(String dealUniqueId, String fromCurrency, String toCurrency,
                         LocalDateTime dealTimestamp, BigDecimal dealAmount) {
        this.dealUniqueId = dealUniqueId;
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.dealTimestamp = dealTimestamp;
        this.dealAmount = dealAmount;
    }


    @Override
    public String toString() {
        return "FxDealRequest{" +
                "dealUniqueId='" + dealUniqueId + '\'' +
                ", fromCurrency='" + fromCurrency + '\'' +
                ", toCurrency='" + toCurrency + '\'' +
                ", dealTimestamp=" + dealTimestamp +
                ", dealAmount=" + dealAmount +
                '}';
    }
}
package com.progressoft.fxdeals.model.dtos;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString

public class FXDealDto {

    @NotNull(message = "Deal id must not be null")
    @NotEmpty(message = "Deal id must not be empty")
    @Size(max = 255, min = 3, message = "Deal id should be between 3 and 255 character.")
    private String id;

    @NotNull(message = "Ordering currency iso code must not be null")
    @NotEmpty(message = "Ordering currency iso code must not be empty")
    @Size(max = 255, min = 3, message = "Ordering currency iso code should be between 3 and 255 character.")
    private String orderingCurrencyIsoCode;

    @NotNull(message = "To currency iso code must not be null")
    @NotEmpty(message = "To currency iso code must not be empty")
    @Size(max = 255, min = 3, message = "To currency iso code should be between 3 and 255 character.")
    private String toCurrencyIsoCode;

    private LocalDateTime dealTimestamp;

    @NotNull(message = "Deal amount must not be null")
    @Max(value = (long) Double.MAX_VALUE, message = "to long value")
    @Min(value = 0, message = "deal amount should be a positive value.")
    private Double dealAmount;
}


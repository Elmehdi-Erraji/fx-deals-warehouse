package com.progressoft.fxdeals.model.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "foreign_exchange_deals")
public class FXDeal {

    @Id
    @Column(name = "id")
    private String id;

    @NotNull(message = "Ordering currency iso code must not be null")
    @NotEmpty(message = "Ordering currency iso code must not be empty")
    @Size(max = 255, min = 3, message = "Deal id should be between 3 and 255 character.")
    @Column(name = "orderingCurrencyIsoCode", nullable = false)
    private String orderingCurrencyIsoCode;

    @NotNull(message = "To currency iso code must not be null")
    @NotEmpty(message = "To currency iso code must not be empty")
    @Size(max = 255, min = 3, message = "Ordering currency iso code should be between 3 and 255 character.")
    @Column(name = "toCurrencyIsoCode", nullable = false)
    private String toCurrencyIsoCode;

    @CreationTimestamp
    @Column(name = "dealTimestamp")
    private LocalDateTime dealTimestamp;

    @NotNull(message = "Deal amount must not be null")
    @Max(value = (long) Double.MAX_VALUE, message = "to long value")
    @Min(value = 0, message = "deal amount should be a positive value.")
    @Column(name = "dealAmount", nullable = false)
    private Double dealAmount;
}
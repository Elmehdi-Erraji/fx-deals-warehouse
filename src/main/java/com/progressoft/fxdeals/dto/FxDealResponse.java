package com.progressoft.fxdeals.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.progressoft.fxdeals.domain.FxDeal;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
public class FxDealResponse {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("dealUniqueId")
    private String dealUniqueId;

    @JsonProperty("fromCurrency")
    private String fromCurrency;

    @JsonProperty("toCurrency")
    private String toCurrency;

    @JsonProperty("dealTimestamp")
    private LocalDateTime dealTimestamp;

    @JsonProperty("dealAmount")
    private BigDecimal dealAmount;

    @JsonProperty("createdAt")
    private LocalDateTime createdAt;

    @JsonProperty("updatedAt")
    private LocalDateTime updatedAt;

    public FxDealResponse() {}

    public FxDealResponse(Long id, String dealUniqueId, String fromCurrency, String toCurrency,
                          LocalDateTime dealTimestamp, BigDecimal dealAmount,
                          LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.dealUniqueId = dealUniqueId;
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.dealTimestamp = dealTimestamp;
        this.dealAmount = dealAmount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static FxDealResponse fromEntity(FxDeal entity) {
        return new FxDealResponse(
                entity.getId(),
                entity.getDealUniqueId(),
                entity.getFromCurrency(),
                entity.getToCurrency(),
                entity.getDealTimestamp(),
                entity.getDealAmount(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    @Override
    public String toString() {
        return "FxDealResponse{" +
                "id=" + id +
                ", dealUniqueId='" + dealUniqueId + '\'' +
                ", fromCurrency='" + fromCurrency + '\'' +
                ", toCurrency='" + toCurrency + '\'' +
                ", dealTimestamp=" + dealTimestamp +
                ", dealAmount=" + dealAmount +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
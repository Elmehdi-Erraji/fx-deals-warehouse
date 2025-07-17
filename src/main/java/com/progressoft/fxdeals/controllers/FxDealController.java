package com.progressoft.fxdeals.controllers;

import com.progressoft.fxdeals.dto.FxDealRequest;
import com.progressoft.fxdeals.dto.FxDealResponse;
import com.progressoft.fxdeals.services.FxDealService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/fx-deals")
@Validated
@CrossOrigin(origins = "*", maxAge = 3600)
public class FxDealController {

    private static final Logger logger = LoggerFactory.getLogger(FxDealController.class);

    private final FxDealService fxDealService;

    @Autowired
    public FxDealController(FxDealService fxDealService) {
        this.fxDealService = fxDealService;
    }

    /**
     * Creates a new FX deal
     * POST /api/v1/fx-deals
     */
    @PostMapping
    public ResponseEntity<ApiResponse<FxDealResponse>> createFxDeal(@Valid @RequestBody FxDealRequest request) {
        logger.info("Received request to create FX deal with unique ID: {}", request.getDealUniqueId());

        FxDealResponse response = fxDealService.createFxDeal(request);

        logger.info("FX deal created successfully with ID: {}", response.getId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                        true,
                        "FX deal created successfully",
                        response,
                        null
                ));
    }

    /**
     * Retrieves all FX deals
     * GET /api/v1/fx-deals
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<FxDealResponse>>> getAllFxDeals() {
        logger.debug("Received request to retrieve all FX deals");

        List<FxDealResponse> deals = fxDealService.getAllFxDeals();

        logger.debug("Retrieved {} FX deals", deals.size());

        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "FX deals retrieved successfully",
                deals,
                Map.of("totalCount", deals.size())
        ));
    }

    /**
     * Retrieves FX deal by unique ID
     * GET /api/v1/fx-deals/{dealUniqueId}
     */
    @GetMapping("/{dealUniqueId}")
    public ResponseEntity<ApiResponse<FxDealResponse>> getFxDealByUniqueId(
            @PathVariable @NotBlank Long dealUniqueId) {
        logger.debug("Received request to retrieve FX deal with unique ID: {}", dealUniqueId);

        FxDealResponse deal = fxDealService.getFxDealByUniqueId(dealUniqueId);

        logger.debug("Retrieved FX deal with unique ID: {}", dealUniqueId);

        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "FX deal retrieved successfully",
                deal,
                null
        ));
    }

    /**
     * Retrieves FX deals by currency pair
     * GET /api/v1/fx-deals/currency-pair/{fromCurrency}/{toCurrency}
     */
    @GetMapping("/currency-pair/{fromCurrency}/{toCurrency}")
    public ResponseEntity<ApiResponse<List<FxDealResponse>>> getFxDealsByCurrencyPair(
            @PathVariable @NotBlank String fromCurrency,
            @PathVariable @NotBlank String toCurrency) {
        logger.debug("Received request to retrieve FX deals for currency pair: {} -> {}", fromCurrency, toCurrency);

        List<FxDealResponse> deals = fxDealService.getFxDealsByCurrencyPair(fromCurrency, toCurrency);

        logger.debug("Retrieved {} FX deals for currency pair: {} -> {}", deals.size(), fromCurrency, toCurrency);

        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "FX deals retrieved successfully",
                deals,
                Map.of("currencyPair", fromCurrency + "/" + toCurrency, "totalCount", deals.size())
        ));
    }

    /**
     * Retrieves FX deals within a date range
     * GET /api/v1/fx-deals/date-range?startDate={startDate}&endDate={endDate}
     */
    @GetMapping("/date-range")
    public ResponseEntity<ApiResponse<List<FxDealResponse>>> getFxDealsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        logger.debug("Received request to retrieve FX deals between {} and {}", startDate, endDate);

        List<FxDealResponse> deals = fxDealService.getFxDealsByDateRange(startDate, endDate);

        logger.debug("Retrieved {} FX deals between {} and {}", deals.size(), startDate, endDate);

        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "FX deals retrieved successfully",
                deals,
                Map.of("startDate", startDate, "endDate", endDate, "totalCount", deals.size())
        ));
    }

    /**
     * Retrieves recent FX deals
     * GET /api/v1/fx-deals/recent?limit={limit}
     */
    @GetMapping("/recent")
    public ResponseEntity<ApiResponse<List<FxDealResponse>>> getRecentFxDeals(
            @RequestParam(defaultValue = "10") @Min(1) @Max(1000) int limit) {
        logger.debug("Received request to retrieve {} recent FX deals", limit);

        List<FxDealResponse> deals = fxDealService.getRecentFxDeals(limit);

        logger.debug("Retrieved {} recent FX deals", deals.size());

        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Recent FX deals retrieved successfully",
                deals,
                Map.of("limit", limit, "totalCount", deals.size())
        ));
    }

    /**
     * Gets total count of FX deals
     * GET /api/v1/fx-deals/count
     */
    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> getTotalFxDealsCount() {
        logger.debug("Received request to get total FX deals count");

        long count = fxDealService.getTotalFxDealsCount();

        logger.debug("Total FX deals count: {}", count);

        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Total FX deals count retrieved successfully",
                count,
                null
        ));
    }

    /**
     * Checks if FX deal exists by unique ID
     * GET /api/v1/fx-deals/{dealUniqueId}/exists
     */
    @GetMapping("/{dealUniqueId}/exists")
    public ResponseEntity<ApiResponse<Boolean>> existsByDealUniqueId(
            @PathVariable @NotBlank String dealUniqueId) {
        logger.debug("Received request to check if FX deal exists with unique ID: {}", dealUniqueId);

        boolean exists = fxDealService.existsByDealUniqueId(dealUniqueId);

        logger.debug("FX deal with unique ID {} exists: {}", dealUniqueId, exists);

        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "FX deal existence check completed",
                exists,
                Map.of("dealUniqueId", dealUniqueId)
        ));
    }

    /**
     * Health check endpoint
     * GET /api/v1/fx-deals/health
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> healthCheck() {
        logger.debug("Health check requested");

        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "FX Deals service is healthy",
                "OK",
                Map.of("timestamp", LocalDateTime.now())
        ));
    }

    /**
     * Generic API response wrapper
     */
    public static class ApiResponse<T> {
        private boolean success;
        private String message;
        private T data;
        private Map<String, Object> metadata;

        public ApiResponse(boolean success, String message, T data, Map<String, Object> metadata) {
            this.success = success;
            this.message = message;
            this.data = data;
            this.metadata = metadata;
        }

        // Getters and Setters
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }

        public T getData() { return data; }
        public void setData(T data) { this.data = data; }

        public Map<String, Object> getMetadata() { return metadata; }
        public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
    }
}
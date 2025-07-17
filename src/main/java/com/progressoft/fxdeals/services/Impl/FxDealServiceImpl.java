package com.progressoft.fxdeals.services.Impl;

import com.progressoft.fxdeals.dto.FxDealRequest;
import com.progressoft.fxdeals.dto.FxDealResponse;
import com.progressoft.fxdeals.domain.FxDeal;
import com.progressoft.fxdeals.exceptions.DuplicateDataException;
import com.progressoft.fxdeals.exceptions.ValidationException;
import com.progressoft.fxdeals.repositories.FxDealRepository;
import com.progressoft.fxdeals.services.FxDealService;
import com.progressoft.fxdeals.services.ValidationService;
import com.progressoft.fxdeals.utils.CurrencyValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class FxDealServiceImpl implements FxDealService {

    private static final Logger logger = LoggerFactory.getLogger(FxDealServiceImpl.class);

    private final FxDealRepository fxDealRepository;
    private final ValidationService validationService;

    @Autowired
    public FxDealServiceImpl(FxDealRepository fxDealRepository, ValidationService validationService) {
        this.fxDealRepository = fxDealRepository;
        this.validationService = validationService;
    }

    @Override
    public FxDealResponse createFxDeal(FxDealRequest request) {
        logger.info("Creating FX deal with unique ID: {}", request.getDealUniqueId());

        validationService.validateFxDealRequest(request);

        if (fxDealRepository.existsByDealUniqueId(request.getDealUniqueId())) {
            logger.error("Duplicate deal unique ID: {}", request.getDealUniqueId());
            throw new DuplicateDataException("Deal with unique ID '" + request.getDealUniqueId() + "' already exists");
        }

        String fromCurrency = CurrencyValidator.normalizeCurrency(request.getFromCurrency());
        String toCurrency = CurrencyValidator.normalizeCurrency(request.getToCurrency());

        FxDeal fxDeal = new FxDeal(
                request.getDealUniqueId(),
                fromCurrency,
                toCurrency,
                request.getDealTimestamp(),
                request.getDealAmount()
        );

        try {
            // Save to database
            FxDeal savedDeal = fxDealRepository.save(fxDeal);
            logger.info("FX deal created successfully with ID: {}", savedDeal.getId());

            return FxDealResponse.fromEntity(savedDeal);
        } catch (DataIntegrityViolationException e) {
            logger.error("Data integrity violation while saving FX deal: {}", e.getMessage());
            throw new DuplicateDataException("Deal with unique ID '" + request.getDealUniqueId() + "' already exists");
        } catch (Exception e) {
            logger.error("Unexpected error while saving FX deal: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to save FX deal", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<FxDealResponse> getAllFxDeals() {
        logger.debug("Retrieving all FX deals");

        List<FxDeal> deals = fxDealRepository.findAllByOrderByDealTimestampDesc();

        return deals.stream()
                .map(FxDealResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public FxDealResponse getFxDealByUniqueId(Long dealUniqueId) {
        logger.debug("Retrieving FX deal with unique ID: {}", dealUniqueId);

        Optional<FxDeal> deal = fxDealRepository.findById(dealUniqueId);

        if (deal.isEmpty()) {
            logger.error("FX deal not found with unique ID: {}", dealUniqueId);
            throw new ValidationException("Deal with unique ID '" + dealUniqueId + "' not found");
        }

        return FxDealResponse.fromEntity(deal.get());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FxDealResponse> getFxDealsByCurrencyPair(String fromCurrency, String toCurrency) {
        logger.debug("Retrieving FX deals for currency pair: {} -> {}", fromCurrency, toCurrency);

        if (!CurrencyValidator.isValidCurrency(fromCurrency) || !CurrencyValidator.isValidCurrency(toCurrency)) {
            throw new ValidationException("Invalid currency code provided");
        }

        String normalizedFromCurrency = CurrencyValidator.normalizeCurrency(fromCurrency);
        String normalizedToCurrency = CurrencyValidator.normalizeCurrency(toCurrency);

        List<FxDeal> deals = fxDealRepository.findByCurrencyPair(normalizedFromCurrency, normalizedToCurrency);

        return deals.stream()
                .map(FxDealResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FxDealResponse> getFxDealsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        logger.debug("Retrieving FX deals between {} and {}", startDate, endDate);

        if (startDate == null || endDate == null) {
            throw new ValidationException("Start date and end date are required");
        }

        if (startDate.isAfter(endDate)) {
            throw new ValidationException("Start date cannot be after end date");
        }

        List<FxDeal> deals = fxDealRepository.findByDealTimestampBetween(startDate, endDate);

        return deals.stream()
                .map(FxDealResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FxDealResponse> getRecentFxDeals(int limit) {
        logger.debug("Retrieving {} recent FX deals", limit);

        if (limit <= 0 || limit > 1000) {
            throw new ValidationException("Limit must be between 1 and 1000");
        }

        List<FxDeal> deals = fxDealRepository.findRecentDeals(limit);

        return deals.stream()
                .map(FxDealResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalFxDealsCount() {
        logger.debug("Counting total FX deals");
        return fxDealRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByDealUniqueId(String dealUniqueId) {
        return fxDealRepository.existsByDealUniqueId(dealUniqueId);
    }
}
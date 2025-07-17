package com.progressoft.fxdeals.repositories;

import com.progressoft.fxdeals.domain.FxDeal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FxDealRepository extends JpaRepository<FxDeal, Long> {

    /**
     * Find FX deal by unique deal ID
     * @param dealUniqueId the unique deal identifier
     * @return Optional containing the FX deal if found
     */
    Optional<FxDeal> findById(Long dealUniqueId);

    /**
     * Check if deal exists by unique ID
     * @param dealUniqueId the unique deal identifier
     * @return true if deal exists, false otherwise
     */
    boolean existsByDealUniqueId(String dealUniqueId);

    /**
     * Find deals by currency pair
     * @param fromCurrency source currency
     * @param toCurrency target currency
     * @return List of deals for the currency pair
     */
    @Query("SELECT d FROM FxDeal d WHERE d.fromCurrency = :fromCurrency AND d.toCurrency = :toCurrency")
    List<FxDeal> findByCurrencyPair(@Param("fromCurrency") String fromCurrency,
                                    @Param("toCurrency") String toCurrency);

    /**
     * Find deals within a date range
     * @param startDate start of date range
     * @param endDate end of date range
     * @return List of deals within the date range
     */
    @Query("SELECT d FROM FxDeal d WHERE d.dealTimestamp BETWEEN :startDate AND :endDate ORDER BY d.dealTimestamp DESC")
    List<FxDeal> findByDealTimestampBetween(@Param("startDate") LocalDateTime startDate,
                                            @Param("endDate") LocalDateTime endDate);

    /**
     * Find deals by source currency
     * @param fromCurrency source currency
     * @return List of deals with the specified source currency
     */
    List<FxDeal> findByFromCurrencyOrderByDealTimestampDesc(String fromCurrency);

    /**
     * Find deals by target currency
     * @param toCurrency target currency
     * @return List of deals with the specified target currency
     */
    List<FxDeal> findByToCurrencyOrderByDealTimestampDesc(String toCurrency);

    /**
     * Find recent deals (last N records)
     * @param limit number of records to return
     * @return List of recent deals
     */
    @Query("SELECT d FROM FxDeal d ORDER BY d.createdAt DESC LIMIT :limit")
    List<FxDeal> findRecentDeals(@Param("limit") int limit);

    /**
     * Count deals by currency pair
     * @param fromCurrency source currency
     * @param toCurrency target currency
     * @return number of deals for the currency pair
     */
    @Query("SELECT COUNT(d) FROM FxDeal d WHERE d.fromCurrency = :fromCurrency AND d.toCurrency = :toCurrency")
    long countByCurrencyPair(@Param("fromCurrency") String fromCurrency,
                             @Param("toCurrency") String toCurrency);

    /**
     * Find all deals ordered by timestamp descending
     * @return List of all deals ordered by timestamp
     */
    List<FxDeal> findAllByOrderByDealTimestampDesc();
}
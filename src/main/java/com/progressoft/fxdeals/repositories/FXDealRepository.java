package com.progressoft.fxdeals.repositories;

import com.progressoft.fxdeals.model.entities.FXDeal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FXDealRepository extends JpaRepository<FXDeal, String> {}

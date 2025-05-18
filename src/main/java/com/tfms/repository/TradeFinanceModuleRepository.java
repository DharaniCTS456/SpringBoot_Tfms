package com.tfms.repository;

import com.tfms.model.TradeFinanceModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradeFinanceModuleRepository extends JpaRepository<TradeFinanceModule, Long> {
    
    List<TradeFinanceModule> findByActiveTrue();
    
}
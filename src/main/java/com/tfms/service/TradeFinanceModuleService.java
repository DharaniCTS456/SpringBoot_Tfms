package com.tfms.service;

import com.tfms.model.TradeFinanceModule;
import java.util.List;

public interface TradeFinanceModuleService {
    
    List<TradeFinanceModule> getAllModules();
    
    List<TradeFinanceModule> getActiveModules();
    
    TradeFinanceModule getModuleById(Long id);
    
    void initializeModules();
}

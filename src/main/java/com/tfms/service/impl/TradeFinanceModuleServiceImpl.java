package com.tfms.service.impl;

import com.tfms.model.TradeFinanceModule;
import com.tfms.repository.TradeFinanceModuleRepository;
import com.tfms.service.TradeFinanceModuleService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class TradeFinanceModuleServiceImpl implements TradeFinanceModuleService {

    private final TradeFinanceModuleRepository moduleRepository;

    public TradeFinanceModuleServiceImpl(TradeFinanceModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    @Override
    public List<TradeFinanceModule> getAllModules() {
        return moduleRepository.findAll();
    }

    @Override
    public List<TradeFinanceModule> getActiveModules() {
        return moduleRepository.findByActiveTrue();
    }

    @Override
    public TradeFinanceModule getModuleById(Long id) {
        return moduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Trade Finance Module not found with ID: " + id));
    }

    @Override
    public void initializeModules() {
        if (moduleRepository.count() == 0) {
            List<TradeFinanceModule> modules = List.of(
                new TradeFinanceModule("Letter of Credit Management", 
                    "Create, amend, and manage Letters of Credit for international trade transactions.", true, "Ready for use"),
                new TradeFinanceModule("Bank Guarantee Management", 
                    "Request, issue, and track Bank Guarantees for various business needs.", true, "Ready for use"),
                new TradeFinanceModule("Trade Documentation", 
                    "Upload, view, and manage various trade-related documents.", true, "Ready for use"),
                new TradeFinanceModule("Risk Assessment", 
                    "Analyze and evaluate the risk of trade finance transactions.", true, "Ready for use"),
                new TradeFinanceModule("Compliance and Regulatory Reporting", 
                    "Generate and submit regulatory reports for compliance purposes.", true, "Ready for use")
            );
            moduleRepository.saveAll(modules);
        }
    }
}

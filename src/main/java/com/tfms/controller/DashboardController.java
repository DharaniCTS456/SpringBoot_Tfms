package com.tfms.controller;

import com.tfms.service.BankGuaranteeService;
import com.tfms.service.ComplianceService;
import com.tfms.service.LetterOfCreditService;
import com.tfms.service.RiskAssessmentService;
import com.tfms.service.TradeDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for the main dashboard and index page
 */
@Controller
public class DashboardController {

    @Autowired
    private LetterOfCreditService letterOfCreditService;

    @Autowired
    private BankGuaranteeService bankGuaranteeService;

    @Autowired
    private TradeDocumentService tradeDocumentService;

    @Autowired
    private RiskAssessmentService riskAssessmentService;

    @Autowired
    private ComplianceService complianceService;

    /**
     * Home page redirects to dashboard
     */
    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard";
    }

    /**
     * Login page
     */
    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    /**
     * Dashboard page displaying summary of all modules
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Add counts for each module
        model.addAttribute("lcCount", letterOfCreditService.getAllLettersOfCredit().size());
        model.addAttribute("bgCount", bankGuaranteeService.getAllBankGuarantees().size());
        model.addAttribute("documentCount", tradeDocumentService.getAllTradeDocuments().size());
        model.addAttribute("riskCount", riskAssessmentService.getAllRiskAssessments().size());
        model.addAttribute("complianceCount", complianceService.getAllComplianceReports().size());
        
        // Add active counts for each module
        model.addAttribute("activeLcCount", letterOfCreditService.getLetterOfCreditsByStatus("ACTIVE").size());
        model.addAttribute("activeBgCount", bankGuaranteeService.getBankGuaranteesByStatus("ISSUED").size());
        
        return "dashboard/index";
    }
}
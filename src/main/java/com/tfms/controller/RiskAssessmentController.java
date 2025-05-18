package com.tfms.controller;

import com.tfms.model.RiskAssessment;
import com.tfms.service.RiskAssessmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;

/**
 * Controller for Risk Assessment operations
 */
@Controller
@RequestMapping("/risk")
public class RiskAssessmentController {

    @Autowired
    private RiskAssessmentService riskAssessmentService;

    /**
     * Display all Risk Assessments
     */
    @GetMapping
    public String getAllRiskAssessments(Model model) {
        model.addAttribute("riskAssessments", riskAssessmentService.getAllRiskAssessments());
        return "risk/list";
    }

    /**
     * Display a specific Risk Assessment
     */
    @GetMapping("/{id}")
    public String getRiskAssessment(@PathVariable Long id, Model model) {
        model.addAttribute("riskAssessment", riskAssessmentService.getRiskAssessmentById(id));
        return "risk/view";
    }

    /**
     * Show form to create a new Risk Assessment
     */
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("riskAssessment", new RiskAssessment());
        return "risk/create";
    }

    /**
     * Process form submission to create a new Risk Assessment
     */
    @PostMapping("/create")
    public String createRiskAssessment(
            @Valid @ModelAttribute("riskAssessment") RiskAssessment riskAssessment,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Principal principal) {
        
        if (result.hasErrors()) {
            return "risk/create";
        }
        
        // Set the current user as assessor and creator
        riskAssessment.setAssessorName(principal.getName());
        riskAssessment.setCreatedBy(principal.getName());
        riskAssessment.setLastModifiedBy(principal.getName());
        
        // Set assessment date if not provided
        if (riskAssessment.getAssessmentDate() == null) {
            riskAssessment.setAssessmentDate(LocalDate.now());
        }
        
        RiskAssessment savedRisk = riskAssessmentService.createRiskAssessment(riskAssessment);
        redirectAttributes.addFlashAttribute("success", "Risk Assessment created successfully with ID: " + savedRisk.getRiskId());
        
        return "redirect:/risk";
    }

    /**
     * Show form to edit a Risk Assessment
     */
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("riskAssessment", riskAssessmentService.getRiskAssessmentById(id));
        return "risk/edit";
    }

    /**
     * Process form submission to update a Risk Assessment
     */
    @PostMapping("/{id}/edit")
    public String updateRiskAssessment(
            @PathVariable Long id,
            @Valid @ModelAttribute("riskAssessment") RiskAssessment riskAssessment,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Principal principal) {
        
        if (result.hasErrors()) {
            return "risk/edit";
        }
        
        // Set the current user as last modifier
        riskAssessment.setLastModifiedBy(principal.getName());
        
        riskAssessmentService.updateRiskAssessment(id, riskAssessment);
        redirectAttributes.addFlashAttribute("success", "Risk Assessment updated successfully");
        
        return "redirect:/risk/" + id;
    }

    /**
     * Show form to analyze risk for a transaction
     */
    @GetMapping("/analyze")
    public String showAnalyzeForm(Model model) {
        return "risk/analyze";
    }

    /**
     * Process risk analysis for a transaction
     */
    @PostMapping("/analyze")
    public String analyzeRisk(
            @RequestParam String transactionReference,
            @RequestParam String riskFactors,
            RedirectAttributes redirectAttributes,
            Principal principal) {
        
        RiskAssessment analyzedRisk = riskAssessmentService.analyzeRisk(transactionReference, riskFactors);
        redirectAttributes.addFlashAttribute("success", "Risk Analysis completed successfully. Risk Score: " + analyzedRisk.getRiskScore());
        
        return "redirect:/risk/" + analyzedRisk.getId();
    }

    /**
     * Get risk score for a specific risk ID
     */
    @GetMapping("/{riskId}/score")
    @ResponseBody
    public Integer getRiskScore(@PathVariable String riskId) {
        return riskAssessmentService.getRiskScore(riskId);
    }

    /**
     * Delete a Risk Assessment
     */
    @PostMapping("/{id}/delete")
    public String deleteRiskAssessment(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        
        riskAssessmentService.deleteRiskAssessment(id);
        redirectAttributes.addFlashAttribute("success", "Risk Assessment deleted successfully");
        
        return "redirect:/risk";
    }

    /**
     * Filter Risk Assessments
     */
    @GetMapping("/filter")
    public String filterRiskAssessments(
            @RequestParam(required = false) String transactionReference,
            @RequestParam(required = false) Integer minScore,
            @RequestParam(required = false) Integer maxScore,
            @RequestParam(required = false) String riskLevel,
            Model model) {
        
        if (transactionReference != null && !transactionReference.isEmpty()) {
            model.addAttribute("riskAssessments", riskAssessmentService.getRiskAssessmentsByTransactionReference(transactionReference));
            model.addAttribute("filterType", "transactionReference");
            model.addAttribute("filterValue", transactionReference);
        } else if (minScore != null) {
            model.addAttribute("riskAssessments", riskAssessmentService.getRiskAssessmentsByRiskScoreGreaterThanEqual(minScore));
            model.addAttribute("filterType", "minScore");
            model.addAttribute("filterValue", minScore);
        } else if (maxScore != null) {
            model.addAttribute("riskAssessments", riskAssessmentService.getRiskAssessmentsByRiskScoreLessThanEqual(maxScore));
            model.addAttribute("filterType", "maxScore");
            model.addAttribute("filterValue", maxScore);
        } else if (riskLevel != null && !riskLevel.isEmpty()) {
            model.addAttribute("riskAssessments", riskAssessmentService.getRiskAssessmentsByRiskLevel(riskLevel));
            model.addAttribute("filterType", "riskLevel");
            model.addAttribute("filterValue", riskLevel);
        } else {
            model.addAttribute("riskAssessments", riskAssessmentService.getAllRiskAssessments());
        }
        
        return "risk/list";
    }
}
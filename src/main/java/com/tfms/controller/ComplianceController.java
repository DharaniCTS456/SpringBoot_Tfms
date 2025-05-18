package com.tfms.controller;

import com.tfms.model.Compliance;
import com.tfms.service.ComplianceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;

/**
 * Controller for Compliance operations
 */
@Controller
@RequestMapping("/compliance")
public class ComplianceController {

    @Autowired
    private ComplianceService complianceService;

    /**
     * Display all Compliance Reports
     */
    @GetMapping
    public String getAllComplianceReports(Model model) {
        model.addAttribute("complianceReports", complianceService.getAllComplianceReports());
        return "compliance/list";
    }

    /**
     * Display a specific Compliance Report
     */
    @GetMapping("/{id}")
    public String getComplianceReport(@PathVariable Long id, Model model) {
        model.addAttribute("complianceReport", complianceService.getComplianceReportById(id));
        return "compliance/view";
    }

    /**
     * Show form to create a new Compliance Report
     */
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("complianceReport", new Compliance());
        return "compliance/create";
    }

    /**
     * Process form submission to create a new Compliance Report
     */
    @PostMapping("/create")
    public String createComplianceReport(
            @Valid @ModelAttribute("complianceReport") Compliance complianceReport,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Principal principal) {
        
        if (result.hasErrors()) {
            return "compliance/create";
        }
        
        // Set the current user as submitter and creator
        complianceReport.setSubmittedBy(principal.getName());
        complianceReport.setCreatedBy(principal.getName());
        complianceReport.setLastModifiedBy(principal.getName());
        
        // Set report date if not provided
        if (complianceReport.getReportDate() == null) {
            complianceReport.setReportDate(LocalDate.now());
        }
        
        Compliance savedReport = complianceService.createComplianceReport(complianceReport);
        redirectAttributes.addFlashAttribute("success", "Compliance Report created successfully with ID: " + savedReport.getComplianceId());
        
        return "redirect:/compliance";
    }

    /**
     * Show form to edit a Compliance Report
     */
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("complianceReport", complianceService.getComplianceReportById(id));
        return "compliance/edit";
    }

    /**
     * Process form submission to update a Compliance Report
     */
    @PostMapping("/{id}/edit")
    public String updateComplianceReport(
            @PathVariable Long id,
            @Valid @ModelAttribute("complianceReport") Compliance complianceReport,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Principal principal) {
        
        if (result.hasErrors()) {
            return "compliance/edit";
        }
        
        // Set the current user as last modifier
        complianceReport.setLastModifiedBy(principal.getName());
        
        complianceService.updateComplianceReport(id, complianceReport);
        redirectAttributes.addFlashAttribute("success", "Compliance Report updated successfully");
        
        return "redirect:/compliance/" + id;
    }

    /**
     * Show form to generate a Compliance Report
     */
    @GetMapping("/generate")
    public String showGenerateForm() {
        return "compliance/generate";
    }

    /**
     * Process request to generate a Compliance Report
     */
    @PostMapping("/generate")
    public String generateComplianceReport(
            @RequestParam String transactionReference,
            @RequestParam String regulationType,
            RedirectAttributes redirectAttributes) {
        
        Compliance generatedReport = complianceService.generateComplianceReport(transactionReference, regulationType);
        redirectAttributes.addFlashAttribute("success", "Compliance Report generated successfully");
        
        return "redirect:/compliance/" + generatedReport.getId();
    }

    /**
     * Show form to submit a Regulatory Report
     */
    @GetMapping("/{complianceId}/submit")
    public String showSubmitForm(@PathVariable String complianceId, Model model) {
        model.addAttribute("complianceId", complianceId);
        model.addAttribute("complianceReport", complianceService.getComplianceReportByComplianceId(complianceId));
        return "compliance/submit";
    }

    /**
     * Process submission of a Regulatory Report
     */
    @PostMapping("/{complianceId}/submit")
    public String submitRegulatoryReport(
            @PathVariable String complianceId,
            @RequestParam("reportFile") MultipartFile reportFile,
            RedirectAttributes redirectAttributes,
            Principal principal) {
        
        if (reportFile.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Please select a report file to upload");
            return "redirect:/compliance/" + complianceId + "/submit";
        }
        
        Compliance submittedReport = complianceService.submitRegulatoryReport(complianceId, reportFile);
        submittedReport.setSubmittedBy(principal.getName());
        submittedReport.setSubmissionDate(LocalDate.now());
        
        redirectAttributes.addFlashAttribute("success", "Regulatory Report submitted successfully");
        
        return "redirect:/compliance/" + submittedReport.getId();
    }

    /**
     * Delete a Compliance Report
     */
    @PostMapping("/{id}/delete")
    public String deleteComplianceReport(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        
        complianceService.deleteComplianceReport(id);
        redirectAttributes.addFlashAttribute("success", "Compliance Report deleted successfully");
        
        return "redirect:/compliance";
    }

    /**
     * Filter Compliance Reports
     */
    @GetMapping("/filter")
    public String filterComplianceReports(
            @RequestParam(required = false) String transactionReference,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String regulationType,
            @RequestParam(required = false) String reportingAuthority,
            Model model) {
        
        if (transactionReference != null && !transactionReference.isEmpty()) {
            model.addAttribute("complianceReports", complianceService.getComplianceReportsByTransactionReference(transactionReference));
            model.addAttribute("filterType", "transactionReference");
            model.addAttribute("filterValue", transactionReference);
        } else if (status != null && !status.isEmpty()) {
            model.addAttribute("complianceReports", complianceService.getComplianceReportsByStatus(status));
            model.addAttribute("filterType", "status");
            model.addAttribute("filterValue", status);
        } else if (regulationType != null && !regulationType.isEmpty()) {
            model.addAttribute("complianceReports", complianceService.getComplianceReportsByRegulationType(regulationType));
            model.addAttribute("filterType", "regulationType");
            model.addAttribute("filterValue", regulationType);
        } else if (reportingAuthority != null && !reportingAuthority.isEmpty()) {
            model.addAttribute("complianceReports", complianceService.getComplianceReportsByReportingAuthority(reportingAuthority));
            model.addAttribute("filterType", "reportingAuthority");
            model.addAttribute("filterValue", reportingAuthority);
        } else {
            model.addAttribute("complianceReports", complianceService.getAllComplianceReports());
        }
        
        return "compliance/list";
    }
}
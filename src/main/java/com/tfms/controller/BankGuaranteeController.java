package com.tfms.controller;

import com.tfms.model.BankGuarantee;
import com.tfms.service.BankGuaranteeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;

/**
 * Controller for Bank Guarantee operations
 */
@Controller
@RequestMapping("/guarantee")
public class BankGuaranteeController {

    @Autowired
    private BankGuaranteeService bankGuaranteeService;

    /**
     * Display all Bank Guarantees
     */
    @GetMapping
    public String getAllBankGuarantees(Model model) {
        model.addAttribute("bankGuarantees", bankGuaranteeService.getAllBankGuarantees());
        return "guarantee/list";
    }

    /**
     * Display a specific Bank Guarantee
     */
    @GetMapping("/{id}")
    public String getBankGuarantee(@PathVariable Long id, Model model) {
        model.addAttribute("guarantee", bankGuaranteeService.getBankGuaranteeById(id));
        return "guarantee/view";
    }

    /**
     * Show form to request a new Bank Guarantee
     */
    @GetMapping("/request")
    public String showRequestForm(Model model) {
        model.addAttribute("bankGuarantee", new BankGuarantee());
        return "guarantee/request";
    }

    /**
     * Process form submission to request a new Bank Guarantee
     */
    @PostMapping("/request")
    public String requestGuarantee(
            @Valid @ModelAttribute("bankGuarantee") BankGuarantee bankGuarantee,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Principal principal) {
        
        if (result.hasErrors()) {
            return "guarantee/request";
        }
        
        // Set the current user as creator
        bankGuarantee.setCreatedBy(principal.getName());
        bankGuarantee.setLastModifiedBy(principal.getName());
        
        BankGuarantee savedBg = bankGuaranteeService.requestGuarantee(bankGuarantee);
        redirectAttributes.addFlashAttribute("success", "Bank Guarantee requested successfully with ID: " + savedBg.getGuaranteeId());
        
        return "redirect:/guarantee";
    }

    /**
     * Show form to edit a Bank Guarantee
     */
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("bankGuarantee", bankGuaranteeService.getBankGuaranteeById(id));
        return "guarantee/edit";
    }

    /**
     * Process form submission to update a Bank Guarantee
     */
    @PostMapping("/{id}/edit")
    public String updateBankGuarantee(
            @PathVariable Long id,
            @Valid @ModelAttribute("bankGuarantee") BankGuarantee bankGuarantee,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Principal principal) {
        
        if (result.hasErrors()) {
            return "guarantee/edit";
        }
        
        // Set the current user as last modifier
        bankGuarantee.setLastModifiedBy(principal.getName());
        
        bankGuaranteeService.updateBankGuarantee(id, bankGuarantee);
        redirectAttributes.addFlashAttribute("success", "Bank Guarantee updated successfully");
        
        return "redirect:/guarantee/" + id;
    }

    /**
     * Issue a Bank Guarantee
     */
    @PostMapping("/{guaranteeId}/issue")
    public String issueGuarantee(
            @PathVariable String guaranteeId,
            RedirectAttributes redirectAttributes) {
        
        BankGuarantee issuedBg = bankGuaranteeService.issueGuarantee(guaranteeId);
        redirectAttributes.addFlashAttribute("success", "Bank Guarantee issued successfully");
        
        return "redirect:/guarantee/" + issuedBg.getId();
    }

    /**
     * Update Bank Guarantee status
     */
    @PostMapping("/{guaranteeId}/status")
    public String updateStatus(
            @PathVariable String guaranteeId,
            @RequestParam String status,
            RedirectAttributes redirectAttributes) {
        
        BankGuarantee updatedBg = bankGuaranteeService.updateBankGuaranteeStatus(guaranteeId, status);
        redirectAttributes.addFlashAttribute("success", "Bank Guarantee status updated to: " + status);
        
        return "redirect:/guarantee/" + updatedBg.getId();
    }

    /**
     * Delete a Bank Guarantee
     */
    @PostMapping("/{id}/delete")
    public String deleteBankGuarantee(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        
        bankGuaranteeService.deleteBankGuarantee(id);
        redirectAttributes.addFlashAttribute("success", "Bank Guarantee deleted successfully");
        
        return "redirect:/guarantee";
    }

    /**
     * Filter Bank Guarantees
     */
    @GetMapping("/filter")
    public String filterBankGuarantees(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String applicant,
            @RequestParam(required = false) String beneficiary,
            @RequestParam(required = false) String type,
            Model model) {
        
        if (status != null && !status.isEmpty()) {
            model.addAttribute("bankGuarantees", bankGuaranteeService.getBankGuaranteesByStatus(status));
            model.addAttribute("filterType", "status");
            model.addAttribute("filterValue", status);
        } else if (applicant != null && !applicant.isEmpty()) {
            model.addAttribute("bankGuarantees", bankGuaranteeService.getBankGuaranteesByApplicant(applicant));
            model.addAttribute("filterType", "applicant");
            model.addAttribute("filterValue", applicant);
        } else if (beneficiary != null && !beneficiary.isEmpty()) {
            model.addAttribute("bankGuarantees", bankGuaranteeService.getBankGuaranteesByBeneficiary(beneficiary));
            model.addAttribute("filterType", "beneficiary");
            model.addAttribute("filterValue", beneficiary);
        } else if (type != null && !type.isEmpty()) {
            model.addAttribute("bankGuarantees", bankGuaranteeService.getBankGuaranteesByType(type));
            model.addAttribute("filterType", "type");
            model.addAttribute("filterValue", type);
        } else {
            model.addAttribute("bankGuarantees", bankGuaranteeService.getAllBankGuarantees());
        }
        
        return "guarantee/list";
    }
}
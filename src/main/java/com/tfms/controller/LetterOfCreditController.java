package com.tfms.controller;

import com.tfms.model.LetterOfCredit;
import com.tfms.service.LetterOfCreditService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

/**
 * Controller for Letter of Credit operations.
 */
@Controller
@RequestMapping("/lc")
public class LetterOfCreditController {

    private final LetterOfCreditService letterOfCreditService;

    public LetterOfCreditController(LetterOfCreditService letterOfCreditService) {
        this.letterOfCreditService = letterOfCreditService;
    }

    /**
     * Display all Letters of Credit.
     */
    @GetMapping
    public String getAllLettersOfCredit(Model model) {
        model.addAttribute("lettersOfCredit", letterOfCreditService.getAllLettersOfCredit());
        return "lc/list";
    }

    /**
     * Display a specific Letter of Credit.
     */
    @GetMapping("/{id}")
    public String getLetterOfCredit(@PathVariable Long id, Model model) {
        model.addAttribute("lc", letterOfCreditService.getLetterOfCreditById(id));
        return "lc/view";
    }

    /**
     * Show form to create a new Letter of Credit.
     */
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("letterOfCredit", new LetterOfCredit());
        return "lc/create";
    }

    /**
     * Process form submission to create a new Letter of Credit.
     */
    @PostMapping("/create")
    public String createLetterOfCredit(
            @Valid @ModelAttribute("letterOfCredit") LetterOfCredit letterOfCredit,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Principal principal) {
        
        if (result.hasErrors()) {
            return "lc/create";
        }

        // Validate principal to avoid null pointer exception
        String username = Optional.ofNullable(principal).map(Principal::getName).orElse("UnknownUser");

        letterOfCredit.setCreatedBy(username);
        letterOfCredit.setLastModifiedBy(username);

        LetterOfCredit savedLc = letterOfCreditService.createLetterOfCredit(letterOfCredit);
        redirectAttributes.addFlashAttribute("success", "Letter of Credit created successfully with ID: " + savedLc.getLcId());

        return "redirect:/lc";
    }

    /**
     * Show form to edit a Letter of Credit.
     */
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("letterOfCredit", letterOfCreditService.getLetterOfCreditById(id));
        return "lc/edit";
    }

    /**
     * Process form submission to update a Letter of Credit.
     */
    @PostMapping("/{id}/edit")
    public String updateLetterOfCredit(
            @PathVariable Long id,
            @Valid @ModelAttribute("letterOfCredit") LetterOfCredit letterOfCredit,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Principal principal) {
        
        if (result.hasErrors()) {
            return "lc/edit";
        }

        // Validate principal to avoid null pointer exception
        String username = Optional.ofNullable(principal).map(Principal::getName).orElse("UnknownUser");
        letterOfCredit.setLastModifiedBy(username);

        letterOfCreditService.updateLetterOfCredit(id, letterOfCredit);
        redirectAttributes.addFlashAttribute("success", "Letter of Credit updated successfully");

        return "redirect:/lc/" + id;
    }

    /**
     * Show form to amend a Letter of Credit.
     */
    @GetMapping("/{lcId}/amend")
    public String showAmendForm(@PathVariable String lcId, Model model) {
        model.addAttribute("letterOfCredit", letterOfCreditService.getLetterOfCreditByLcId(lcId));
        return "lc/amend";
    }

    /**
     * Process form submission to amend a Letter of Credit.
     */
    @PostMapping("/{lcId}/amend")
    public String amendLetterOfCredit(
            @PathVariable String lcId,
            @Valid @ModelAttribute("letterOfCredit") LetterOfCredit letterOfCredit,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Principal principal) {
        
        if (result.hasErrors()) {
            return "lc/amend";
        }

        // Validate principal to avoid null pointer exception
        String username = Optional.ofNullable(principal).map(Principal::getName).orElse("UnknownUser");
        letterOfCredit.setLastModifiedBy(username);

        LetterOfCredit amendedLc = letterOfCreditService.amendLetterOfCredit(lcId, letterOfCredit);
        redirectAttributes.addFlashAttribute("success", "Letter of Credit amended successfully");

        return "redirect:/lc/" + amendedLc.getId();
    }

    /**
     * Close a Letter of Credit.
     */
    @PostMapping("/{lcId}/close")
    public String closeLetterOfCredit(
            @PathVariable String lcId,
            RedirectAttributes redirectAttributes) {

        LetterOfCredit closedLc = letterOfCreditService.closeLetterOfCredit(lcId);
        redirectAttributes.addFlashAttribute("success", "Letter of Credit closed successfully");

        return "redirect:/lc/" + closedLc.getId();
    }

    /**
     * Delete a Letter of Credit.
     */
    @PostMapping("/{id}/delete")
    public String deleteLetterOfCredit(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        letterOfCreditService.deleteLetterOfCredit(id);
        redirectAttributes.addFlashAttribute("success", "Letter of Credit deleted successfully");

        return "redirect:/lc";
    }

    /**
     * Filter Letters of Credit by status, applicant, or beneficiary.
     */
    @GetMapping("/filter")
    public String filterLetterOfCredit(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String applicant,
            @RequestParam(required = false) String beneficiary,
            Model model) {

        if (status != null && !status.isEmpty()) {
            model.addAttribute("lettersOfCredit", letterOfCreditService.getLetterOfCreditsByStatus(status));
            model.addAttribute("filterType", "status");
            model.addAttribute("filterValue", status);
        } else if (applicant != null && !applicant.isEmpty()) {
            model.addAttribute("lettersOfCredit", letterOfCreditService.getLetterOfCreditsByApplicant(applicant));
            model.addAttribute("filterType", "applicant");
            model.addAttribute("filterValue", applicant);
        } else if (beneficiary != null && !beneficiary.isEmpty()) {
            model.addAttribute("lettersOfCredit", letterOfCreditService.getLetterOfCreditsByBeneficiary(beneficiary));
            model.addAttribute("filterType", "beneficiary");
            model.addAttribute("filterValue", beneficiary);
        } else {
            model.addAttribute("lettersOfCredit", letterOfCreditService.getAllLettersOfCredit());
        }

        return "lc/list";
    }
}

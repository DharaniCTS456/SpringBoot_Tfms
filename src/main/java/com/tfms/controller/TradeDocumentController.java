package com.tfms.controller;

import com.tfms.model.TradeDocument;
import com.tfms.service.TradeDocumentService;
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
 * Controller for Trade Document operations
 */
@Controller
@RequestMapping("/document")
public class TradeDocumentController {

    @Autowired
    private TradeDocumentService tradeDocumentService;

    /**
     * Display all Trade Documents
     */
    @GetMapping
    public String getAllTradeDocuments(Model model) {
        model.addAttribute("tradeDocuments", tradeDocumentService.getAllTradeDocuments());
        return "document/list";
    }

    /**
     * Display a specific Trade Document
     */
    @GetMapping("/{id}")
    public String getTradeDocument(@PathVariable Long id, Model model) {
        model.addAttribute("document", tradeDocumentService.getTradeDocumentById(id));
        return "document/view";
    }

    /**
     * Show form to upload a new Trade Document
     */
    @GetMapping("/upload")
    public String showUploadForm(Model model) {
        model.addAttribute("tradeDocument", new TradeDocument());
        return "document/upload";
    }

    /**
     * Process form submission to upload a new Trade Document
     */
    @PostMapping("/upload")
    public String uploadDocument(
            @Valid @ModelAttribute("tradeDocument") TradeDocument tradeDocument,
            BindingResult result,
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes,
            Principal principal) {
        
        if (result.hasErrors() || file.isEmpty()) {
            if (file.isEmpty()) {
                result.rejectValue("fileName", "error.tradeDocument", "Please select a file to upload");
            }
            return "document/upload";
        }
        
        // Set the current user as uploader and creator
        tradeDocument.setUploadedBy(principal.getName());
        tradeDocument.setCreatedBy(principal.getName());
        tradeDocument.setLastModifiedBy(principal.getName());
        tradeDocument.setUploadDate(LocalDate.now());
        
        TradeDocument savedDoc = tradeDocumentService.uploadDocument(tradeDocument, file);
        redirectAttributes.addFlashAttribute("success", "Document uploaded successfully with ID: " + savedDoc.getDocumentId());
        
        return "redirect:/document";
    }

    /**
     * Show form to edit a Trade Document's details
     */
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("tradeDocument", tradeDocumentService.getTradeDocumentById(id));
        return "document/edit";
    }

    /**
     * Process form submission to update a Trade Document's details
     */
    @PostMapping("/{id}/edit")
    public String updateDocumentDetails(
            @PathVariable Long id,
            @Valid @ModelAttribute("tradeDocument") TradeDocument tradeDocument,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Principal principal) {
        
        if (result.hasErrors()) {
            return "document/edit";
        }
        
        // Set the current user as last modifier
        tradeDocument.setLastModifiedBy(principal.getName());
        
        tradeDocumentService.updateDocumentDetails(id, tradeDocument);
        redirectAttributes.addFlashAttribute("success", "Document details updated successfully");
        
        return "redirect:/document/" + id;
    }

    /**
     * Delete a Trade Document
     */
    @PostMapping("/{id}/delete")
    public String deleteTradeDocument(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        
        tradeDocumentService.deleteTradeDocument(id);
        redirectAttributes.addFlashAttribute("success", "Document deleted successfully");
        
        return "redirect:/document";
    }

    /**
     * Filter Trade Documents
     */
    @GetMapping("/filter")
    public String filterTradeDocuments(
            @RequestParam(required = false) String documentType,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String referenceNumber,
            @RequestParam(required = false) String uploadedBy,
            Model model) {
        
        if (documentType != null && !documentType.isEmpty()) {
            model.addAttribute("tradeDocuments", tradeDocumentService.getTradeDocumentsByDocumentType(documentType));
            model.addAttribute("filterType", "documentType");
            model.addAttribute("filterValue", documentType);
        } else if (status != null && !status.isEmpty()) {
            model.addAttribute("tradeDocuments", tradeDocumentService.getTradeDocumentsByStatus(status));
            model.addAttribute("filterType", "status");
            model.addAttribute("filterValue", status);
        } else if (referenceNumber != null && !referenceNumber.isEmpty()) {
            model.addAttribute("tradeDocuments", tradeDocumentService.getTradeDocumentsByReferenceNumber(referenceNumber));
            model.addAttribute("filterType", "referenceNumber");
            model.addAttribute("filterValue", referenceNumber);
        } else if (uploadedBy != null && !uploadedBy.isEmpty()) {
            model.addAttribute("tradeDocuments", tradeDocumentService.getTradeDocumentsByUploadedBy(uploadedBy));
            model.addAttribute("filterType", "uploadedBy");
            model.addAttribute("filterValue", uploadedBy);
        } else {
            model.addAttribute("tradeDocuments", tradeDocumentService.getAllTradeDocuments());
        }
        
        return "document/list";
    }
}
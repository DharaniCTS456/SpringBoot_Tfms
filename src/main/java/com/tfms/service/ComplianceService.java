package com.tfms.service;

import com.tfms.model.Compliance;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ComplianceService {
    
    List<Compliance> getAllComplianceReports();
    
    Compliance getComplianceReportById(Long id);
    
    Compliance getComplianceReportByComplianceId(String complianceId);
    
    Compliance createComplianceReport(Compliance compliance);
    
    Compliance updateComplianceReport(Long id, Compliance complianceDetails);
    
    Compliance generateComplianceReport(String transactionReference, String regulationType);
    
    Compliance submitRegulatoryReport(String complianceId, MultipartFile reportFile);
    
    void deleteComplianceReport(Long id);
    
    List<Compliance> getComplianceReportsByTransactionReference(String transactionReference);
    
    List<Compliance> getComplianceReportsByStatus(String status);
    
    List<Compliance> getComplianceReportsByRegulationType(String regulationType);
    
    List<Compliance> getComplianceReportsByReportingAuthority(String reportingAuthority);
}
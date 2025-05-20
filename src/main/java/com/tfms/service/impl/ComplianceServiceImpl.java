package com.tfms.service.impl;

import com.tfms.model.Compliance;
import com.tfms.repository.ComplianceRepository;
import com.tfms.service.ComplianceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ComplianceServiceImpl implements ComplianceService {

    private final ComplianceRepository complianceRepository;

    @Autowired
    public ComplianceServiceImpl(ComplianceRepository complianceRepository) {
        this.complianceRepository = complianceRepository;
    }

    @Override
    public List<Compliance> getAllComplianceReports() {
        return complianceRepository.findAll();
    }

    @Override
    public Compliance getComplianceReportById(Long id) {
        return complianceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compliance report not found for ID: " + id));
    }

    @Override
    public Compliance getComplianceReportByComplianceId(String complianceId) {
        return complianceRepository.findByComplianceId(complianceId)
                .orElseThrow(() -> new RuntimeException("Compliance report not found for Compliance ID: " + complianceId));
    }

    @Override
    public Compliance createComplianceReport(Compliance compliance) {
        return complianceRepository.save(compliance);
    }

    @Override
    public Compliance updateComplianceReport(Long id, Compliance complianceDetails) {
        Compliance compliance = getComplianceReportById(id);
        compliance.setComplianceStatus(complianceDetails.getComplianceStatus());
        compliance.setRemarks(complianceDetails.getRemarks());
        compliance.setLastModifiedBy(complianceDetails.getLastModifiedBy());
        return complianceRepository.save(compliance);
    }

    @Override
    public Compliance generateComplianceReport(String transactionReference, String regulationType) {
        Compliance compliance = new Compliance();
        compliance.setTransactionReference(transactionReference);
        compliance.setRegulationType(regulationType);
        compliance.setComplianceStatus("GENERATED");
        return complianceRepository.save(compliance);
    }

    @Override
    public Compliance submitRegulatoryReport(String complianceId, MultipartFile reportFile) {
        Compliance compliance = getComplianceReportByComplianceId(complianceId);
        compliance.setComplianceStatus("SUBMITTED");
        compliance.setReportFilePath(reportFile.getOriginalFilename());
        return complianceRepository.save(compliance);
    }

    @Override
    public void deleteComplianceReport(Long id) {
        Compliance compliance = getComplianceReportById(id);
        complianceRepository.delete(compliance);
    }

    @Override
    public List<Compliance> getComplianceReportsByTransactionReference(String transactionReference) {
        return complianceRepository.findByTransactionReference(transactionReference);
    }

    @Override
    public List<Compliance> getComplianceReportsByStatus(String status) {
        return complianceRepository.findByComplianceStatus(status);
    }

    @Override
    public List<Compliance> getComplianceReportsByRegulationType(String regulationType) {
        return complianceRepository.findByRegulationType(regulationType);
    }

    @Override
    public List<Compliance> getComplianceReportsByReportingAuthority(String reportingAuthority) {
        return complianceRepository.findByReportingAuthority(reportingAuthority);
    }
}

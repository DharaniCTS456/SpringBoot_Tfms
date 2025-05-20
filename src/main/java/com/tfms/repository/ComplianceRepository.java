package com.tfms.repository;

import com.tfms.model.Compliance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComplianceRepository extends JpaRepository<Compliance, Long> {
    
    Optional<Compliance> findByComplianceId(String complianceId);
    
    List<Compliance> findByTransactionReference(String transactionReference);
    
    List<Compliance> findByComplianceStatus(String status);
    
    List<Compliance> findByRegulationType(String regulationType);
    
    List<Compliance> findByReportingAuthority(String reportingAuthority);
    
    List<Compliance> findBySubmittedBy(String submittedBy);
}
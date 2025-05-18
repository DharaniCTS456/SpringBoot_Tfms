package com.tfms.repository;

import com.tfms.model.BankGuarantee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankGuaranteeRepository extends JpaRepository<BankGuarantee, Long> {
    
    Optional<BankGuarantee> findByGuaranteeId(String guaranteeId);
    
    List<BankGuarantee> findByStatus(String status);
    
    List<BankGuarantee> findByApplicantName(String applicantName);
    
    List<BankGuarantee> findByBeneficiaryName(String beneficiaryName);
    
    List<BankGuarantee> findByGuaranteeType(String guaranteeType);
}
package com.tfms.repository;

import com.tfms.model.LetterOfCredit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LetterOfCreditRepository extends JpaRepository<LetterOfCredit, Long> {
    
    Optional<LetterOfCredit> findByLcId(String lcId);
    
    List<LetterOfCredit> findByStatus(String status);
    
    List<LetterOfCredit> findByApplicantName(String applicantName);
    
    List<LetterOfCredit> findByBeneficiaryName(String beneficiaryName);
}
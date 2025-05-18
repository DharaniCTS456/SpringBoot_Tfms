package com.tfms.service;

import com.tfms.model.LetterOfCredit;

import java.util.List;

public interface LetterOfCreditService {
    
    List<LetterOfCredit> getAllLettersOfCredit();
    
    LetterOfCredit getLetterOfCreditById(Long id);
    
    LetterOfCredit getLetterOfCreditByLcId(String lcId);
    
    LetterOfCredit createLetterOfCredit(LetterOfCredit letterOfCredit);
    
    LetterOfCredit updateLetterOfCredit(Long id, LetterOfCredit letterOfCreditDetails);
    
    LetterOfCredit amendLetterOfCredit(String lcId, LetterOfCredit letterOfCreditDetails);
    
    LetterOfCredit closeLetterOfCredit(String lcId);
    
    void deleteLetterOfCredit(Long id);
    
    List<LetterOfCredit> getLetterOfCreditsByStatus(String status);
    
    List<LetterOfCredit> getLetterOfCreditsByApplicant(String applicantName);
    
    List<LetterOfCredit> getLetterOfCreditsByBeneficiary(String beneficiaryName);
}
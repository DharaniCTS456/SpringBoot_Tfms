package com.tfms.service;

import com.tfms.model.BankGuarantee;

import java.util.List;

public interface BankGuaranteeService {
    
    List<BankGuarantee> getAllBankGuarantees();
    
    BankGuarantee getBankGuaranteeById(Long id);
    
    BankGuarantee getBankGuaranteeByGuaranteeId(String guaranteeId);
    
    BankGuarantee requestGuarantee(BankGuarantee bankGuarantee);
    
    BankGuarantee issueGuarantee(String guaranteeId);
    
    BankGuarantee updateBankGuarantee(Long id, BankGuarantee bankGuaranteeDetails);
    
    BankGuarantee updateBankGuaranteeStatus(String guaranteeId, String status);
    
    void deleteBankGuarantee(Long id);
    
    List<BankGuarantee> getBankGuaranteesByStatus(String status);
    
    List<BankGuarantee> getBankGuaranteesByApplicant(String applicantName);
    
    List<BankGuarantee> getBankGuaranteesByBeneficiary(String beneficiaryName);
    
    List<BankGuarantee> getBankGuaranteesByType(String guaranteeType);
}
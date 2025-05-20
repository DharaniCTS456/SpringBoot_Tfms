package com.tfms.service.impl;

import com.tfms.model.BankGuarantee;
import com.tfms.repository.BankGuaranteeRepository;
import com.tfms.service.BankGuaranteeService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;

@Service
public class BankGuaranteeServiceImpl implements BankGuaranteeService {

    private final BankGuaranteeRepository bankGuaranteeRepository;

    public BankGuaranteeServiceImpl(BankGuaranteeRepository bankGuaranteeRepository) {
        this.bankGuaranteeRepository = bankGuaranteeRepository;
    }

    @Override
    public List<BankGuarantee> getAllBankGuarantees() {
        return bankGuaranteeRepository.findAll();
    }

    @Override
    public BankGuarantee getBankGuaranteeById(Long id) {
        return bankGuaranteeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bank Guarantee not found with ID: " + id));
    }

    @Override
    public BankGuarantee getBankGuaranteeByGuaranteeId(String guaranteeId) {
        return bankGuaranteeRepository.findByGuaranteeId(guaranteeId)
                .orElseThrow(() -> new EntityNotFoundException("Bank Guarantee not found with Guarantee ID: " + guaranteeId));
    }

    @Override
    public BankGuarantee requestGuarantee(BankGuarantee bankGuarantee) {
        bankGuarantee.setGuaranteeId(generateGuaranteeId(bankGuarantee.getGuaranteeId()));
        bankGuarantee.setStatus(bankGuarantee.getStatus() == null || bankGuarantee.getStatus().isEmpty()
                ? "REQUESTED" : bankGuarantee.getStatus());
        return bankGuaranteeRepository.save(bankGuarantee);
    }

    @Override
    public BankGuarantee issueGuarantee(String guaranteeId) {
        BankGuarantee bankGuarantee = getBankGuaranteeByGuaranteeId(guaranteeId);

        if (!"REQUESTED".equals(bankGuarantee.getStatus()) && !"PENDING".equals(bankGuarantee.getStatus())) {
            throw new IllegalStateException("Cannot issue a Bank Guarantee that is not in REQUESTED or PENDING status.");
        }

        bankGuarantee.setStatus("ISSUED");
        return bankGuaranteeRepository.save(bankGuarantee);
    }

    @Override
    public BankGuarantee updateBankGuarantee(Long id, BankGuarantee bankGuaranteeDetails) {
        BankGuarantee bankGuarantee = getBankGuaranteeById(id);
        bankGuarantee.setApplicantName(bankGuaranteeDetails.getApplicantName());
        bankGuarantee.setBeneficiaryName(bankGuaranteeDetails.getBeneficiaryName());
        bankGuarantee.setGuaranteeAmount(bankGuaranteeDetails.getGuaranteeAmount());
        bankGuarantee.setCurrency(bankGuaranteeDetails.getCurrency());
        bankGuarantee.setValidityPeriod(bankGuaranteeDetails.getValidityPeriod());
        bankGuarantee.setStartDate(bankGuaranteeDetails.getStartDate());
        bankGuarantee.setEndDate(bankGuaranteeDetails.getEndDate());
        bankGuarantee.setPurpose(bankGuaranteeDetails.getPurpose());
        bankGuarantee.setGuaranteeType(bankGuaranteeDetails.getGuaranteeType());
        bankGuarantee.setLastModifiedBy(bankGuaranteeDetails.getLastModifiedBy());

        return bankGuaranteeRepository.save(bankGuarantee);
    }

    @Override
    public BankGuarantee updateBankGuaranteeStatus(String guaranteeId, String status) {
        BankGuarantee bankGuarantee = getBankGuaranteeByGuaranteeId(guaranteeId);

        if ("CANCELLED".equals(bankGuarantee.getStatus()) || "EXPIRED".equals(bankGuarantee.getStatus())) {
            throw new IllegalStateException("Cannot update a Bank Guarantee that is already cancelled or expired.");
        }

        bankGuarantee.setStatus(status);
        return bankGuaranteeRepository.save(bankGuarantee);
    }

    @Override
    public void deleteBankGuarantee(Long id) {
        BankGuarantee bankGuarantee = getBankGuaranteeById(id);
        bankGuaranteeRepository.delete(bankGuarantee);
    }

    @Override
    public List<BankGuarantee> getBankGuaranteesByStatus(String status) {
        return bankGuaranteeRepository.findByStatus(status);
    }

    @Override
    public List<BankGuarantee> getBankGuaranteesByApplicant(String applicantName) {
        return bankGuaranteeRepository.findByApplicantName(applicantName);
    }

    @Override
    public List<BankGuarantee> getBankGuaranteesByBeneficiary(String beneficiaryName) {
        return bankGuaranteeRepository.findByBeneficiaryName(beneficiaryName);
    }

    @Override
    public List<BankGuarantee> getBankGuaranteesByType(String guaranteeType) {
        return bankGuaranteeRepository.findByGuaranteeType(guaranteeType);
    }

    private String generateGuaranteeId(String existingId) {
        return existingId == null || existingId.isEmpty() ? "BG-" + UUID.randomUUID().toString().substring(0, 8) : existingId;
    }
}

package com.tfms.service.impl;

import com.tfms.model.LetterOfCredit;
import com.tfms.repository.LetterOfCreditRepository;
import com.tfms.service.LetterOfCreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class LetterOfCreditServiceImpl implements LetterOfCreditService {

    @Autowired
    private LetterOfCreditRepository letterOfCreditRepository;

    @Override
    public List<LetterOfCredit> getAllLettersOfCredit() {
        return letterOfCreditRepository.findAll();
    }

    @Override
    public LetterOfCredit getLetterOfCreditById(Long id) {
        return letterOfCreditRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Letter of Credit not found with id: " + id));
    }

    @Override
    public LetterOfCredit getLetterOfCreditByLcId(String lcId) {
        return letterOfCreditRepository.findByLcId(lcId)
                .orElseThrow(() -> new EntityNotFoundException("Letter of Credit not found with LC ID: " + lcId));
    }

    @Override
    public LetterOfCredit createLetterOfCredit(LetterOfCredit letterOfCredit) {
        // Generate unique LC ID if not provided
        if (letterOfCredit.getLcId() == null || letterOfCredit.getLcId().isEmpty()) {
            letterOfCredit.setLcId("LC-" + UUID.randomUUID().toString().substring(0, 8));
        }
        
        // Set default status if not provided
        if (letterOfCredit.getStatus() == null || letterOfCredit.getStatus().isEmpty()) {
            letterOfCredit.setStatus("DRAFT");
        }
        
        // Set issue date if not provided
        if (letterOfCredit.getIssueDate() == null) {
            letterOfCredit.setIssueDate(LocalDate.now());
        }
        
        return letterOfCreditRepository.save(letterOfCredit);
    }

    @Override
    public LetterOfCredit updateLetterOfCredit(Long id, LetterOfCredit letterOfCreditDetails) {
        LetterOfCredit letterOfCredit = getLetterOfCreditById(id);
        
        letterOfCredit.setApplicantName(letterOfCreditDetails.getApplicantName());
        letterOfCredit.setBeneficiaryName(letterOfCreditDetails.getBeneficiaryName());
        letterOfCredit.setAmount(letterOfCreditDetails.getAmount());
        letterOfCredit.setCurrency(letterOfCreditDetails.getCurrency());
        letterOfCredit.setExpiryDate(letterOfCreditDetails.getExpiryDate());
        letterOfCredit.setDescription(letterOfCreditDetails.getDescription());
        letterOfCredit.setLastModifiedBy(letterOfCreditDetails.getLastModifiedBy());
        
        return letterOfCreditRepository.save(letterOfCredit);
    }

    @Override
    public LetterOfCredit amendLetterOfCredit(String lcId, LetterOfCredit letterOfCreditDetails) {
        LetterOfCredit letterOfCredit = getLetterOfCreditByLcId(lcId);
        
        // Only allow amendments if LC is not closed or expired
        if (letterOfCredit.getStatus().equals("CLOSED") || letterOfCredit.getStatus().equals("EXPIRED")) {
            throw new IllegalStateException("Cannot amend a Letter of Credit that is closed or expired");
        }
        
        letterOfCredit.setAmount(letterOfCreditDetails.getAmount());
        letterOfCredit.setCurrency(letterOfCreditDetails.getCurrency());
        letterOfCredit.setExpiryDate(letterOfCreditDetails.getExpiryDate());
        letterOfCredit.setDescription(letterOfCreditDetails.getDescription());
        letterOfCredit.setStatus("AMENDED");
        letterOfCredit.setLastModifiedBy(letterOfCreditDetails.getLastModifiedBy());
        
        return letterOfCreditRepository.save(letterOfCredit);
    }

    @Override
    public LetterOfCredit closeLetterOfCredit(String lcId) {
        LetterOfCredit letterOfCredit = getLetterOfCreditByLcId(lcId);
        
        // Only allow closing if LC is not already closed or expired
        if (letterOfCredit.getStatus().equals("CLOSED") || letterOfCredit.getStatus().equals("EXPIRED")) {
            throw new IllegalStateException("Letter of Credit is already closed or expired");
        }
        
        letterOfCredit.setStatus("CLOSED");
        
        return letterOfCreditRepository.save(letterOfCredit);
    }

    @Override
    public void deleteLetterOfCredit(Long id) {
        LetterOfCredit letterOfCredit = getLetterOfCreditById(id);
        letterOfCreditRepository.delete(letterOfCredit);
    }

    @Override
    public List<LetterOfCredit> getLetterOfCreditsByStatus(String status) {
        return letterOfCreditRepository.findByStatus(status);
    }

    @Override
    public List<LetterOfCredit> getLetterOfCreditsByApplicant(String applicantName) {
        return letterOfCreditRepository.findByApplicantName(applicantName);
    }

    @Override
    public List<LetterOfCredit> getLetterOfCreditsByBeneficiary(String beneficiaryName) {
        return letterOfCreditRepository.findByBeneficiaryName(beneficiaryName);
    }
}
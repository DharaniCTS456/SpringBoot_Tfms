package com.tfms.service.impl;

import com.tfms.model.RiskAssessment;
import com.tfms.repository.RiskAssessmentRepository;
import com.tfms.service.RiskAssessmentService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;

@Service
public class RiskAssessmentServiceImpl implements RiskAssessmentService {

    private final RiskAssessmentRepository riskAssessmentRepository;

    public RiskAssessmentServiceImpl(RiskAssessmentRepository riskAssessmentRepository) {
        this.riskAssessmentRepository = riskAssessmentRepository;
    }

    @Override
    public List<RiskAssessment> getAllRiskAssessments() {
        return riskAssessmentRepository.findAll();
    }

    @Override
    public RiskAssessment getRiskAssessmentById(Long id) {
        return riskAssessmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Risk Assessment not found with ID: " + id));
    }

    @Override
    public RiskAssessment getRiskAssessmentByRiskId(String riskId) {
        return riskAssessmentRepository.findByRiskId(riskId)
                .orElseThrow(() -> new EntityNotFoundException("Risk Assessment not found with Risk ID: " + riskId));
    }

    @Override
    public RiskAssessment createRiskAssessment(RiskAssessment riskAssessment) {
        riskAssessment.setRiskId(generateRiskId(riskAssessment.getRiskId()));
        riskAssessment.setRiskLevel(determineRiskLevel(riskAssessment.getRiskScore()));

        return riskAssessmentRepository.save(riskAssessment);
    }

    @Override
    public RiskAssessment updateRiskAssessment(Long id, RiskAssessment riskAssessmentDetails) {
        RiskAssessment riskAssessment = getRiskAssessmentById(id);

        riskAssessment.setTransactionReference(riskAssessmentDetails.getTransactionReference());
        riskAssessment.setRiskFactors(riskAssessmentDetails.getRiskFactors());
        riskAssessment.setRiskScore(riskAssessmentDetails.getRiskScore());
        riskAssessment.setRiskLevel(determineRiskLevel(riskAssessmentDetails.getRiskScore()));
        riskAssessment.setLastModifiedBy(riskAssessmentDetails.getLastModifiedBy());

        return riskAssessmentRepository.save(riskAssessment);
    }

    @Override
    public RiskAssessment analyzeRisk(String transactionReference, String riskFactors) {
        RiskAssessment riskAssessment = new RiskAssessment();
        riskAssessment.setRiskId(generateRiskId(null));
        riskAssessment.setTransactionReference(transactionReference);
        riskAssessment.setRiskFactors(riskFactors);
        riskAssessment.setRiskScore(calculateRiskScore(riskFactors));
        riskAssessment.setRiskLevel(determineRiskLevel(riskAssessment.getRiskScore()));

        return riskAssessmentRepository.save(riskAssessment);
    }

    @Override
    public Integer getRiskScore(String riskId) {
        RiskAssessment riskAssessment = getRiskAssessmentByRiskId(riskId);
        return riskAssessment.getRiskScore();
    }

    @Override
    public void deleteRiskAssessment(Long id) {
        RiskAssessment riskAssessment = getRiskAssessmentById(id);
        riskAssessmentRepository.delete(riskAssessment);
    }

    @Override
    public List<RiskAssessment> getRiskAssessmentsByTransactionReference(String transactionReference) {
        return riskAssessmentRepository.findByTransactionReference(transactionReference);
    }

    @Override
    public List<RiskAssessment> getRiskAssessmentsByRiskScoreGreaterThanEqual(Integer score) {
        return riskAssessmentRepository.findByRiskScoreGreaterThanEqual(score);
    }

    @Override
    public List<RiskAssessment> getRiskAssessmentsByRiskScoreLessThanEqual(Integer score) {
        return riskAssessmentRepository.findByRiskScoreLessThanEqual(score);
    }

    @Override
    public List<RiskAssessment> getRiskAssessmentsByRiskLevel(String riskLevel) {
        return riskAssessmentRepository.findByRiskLevel(riskLevel);
    }

    private String generateRiskId(String existingId) {
        return (existingId == null || existingId.isEmpty()) ? "RISK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase() : existingId;
    }

    private Integer calculateRiskScore(String riskFactors) {
        return riskFactors.length() % 100; // Example logic: Modify as needed
    }

    private String determineRiskLevel(Integer riskScore) {
        if (riskScore >= 75) return "HIGH";
        if (riskScore >= 50) return "MEDIUM";
        return "LOW";
    }
}

package com.tfms.service;

import com.tfms.model.RiskAssessment;

import java.util.List;

public interface RiskAssessmentService {
    
    List<RiskAssessment> getAllRiskAssessments();
    
    RiskAssessment getRiskAssessmentById(Long id);
    
    RiskAssessment getRiskAssessmentByRiskId(String riskId);
    
    RiskAssessment createRiskAssessment(RiskAssessment riskAssessment);
    
    RiskAssessment updateRiskAssessment(Long id, RiskAssessment riskAssessmentDetails);
    
    RiskAssessment analyzeRisk(String transactionReference, String riskFactors);
    
    Integer getRiskScore(String riskId);
    
    void deleteRiskAssessment(Long id);
    
    List<RiskAssessment> getRiskAssessmentsByTransactionReference(String transactionReference);
    
    List<RiskAssessment> getRiskAssessmentsByRiskScoreGreaterThanEqual(Integer score);
    
    List<RiskAssessment> getRiskAssessmentsByRiskScoreLessThanEqual(Integer score);
    
    List<RiskAssessment> getRiskAssessmentsByRiskLevel(String riskLevel);
}
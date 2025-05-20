package com.tfms.repository;

import com.tfms.model.RiskAssessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RiskAssessmentRepository extends JpaRepository<RiskAssessment, Long> {
    
    Optional<RiskAssessment> findByRiskId(String riskId);
    
    List<RiskAssessment> findByTransactionReference(String transactionReference);
    
    List<RiskAssessment> findByRiskScoreGreaterThanEqual(Integer score);
    
    List<RiskAssessment> findByRiskScoreLessThanEqual(Integer score);
    
    List<RiskAssessment> findByRiskLevel(String riskLevel);
    
    List<RiskAssessment> findByAssessorName(String assessorName);
}
package com.tfms.repository;

import com.tfms.model.TradeDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TradeDocumentRepository extends JpaRepository<TradeDocument, Long> {
    
    Optional<TradeDocument> findByDocumentId(String documentId);
    
    List<TradeDocument> findByDocumentType(String documentType);
    
    List<TradeDocument> findByStatus(String status);
    
    List<TradeDocument> findByReferenceNumber(String referenceNumber);
    
    List<TradeDocument> findByUploadedBy(String uploadedBy);
}
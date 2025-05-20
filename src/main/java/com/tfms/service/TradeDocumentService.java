package com.tfms.service;

import com.tfms.model.TradeDocument;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TradeDocumentService {
    
    List<TradeDocument> getAllTradeDocuments();
    
    TradeDocument getTradeDocumentById(Long id);
    
    TradeDocument getTradeDocumentByDocumentId(String documentId);
    
    TradeDocument uploadDocument(TradeDocument tradeDocument, MultipartFile file);
    
    TradeDocument updateDocumentDetails(Long id, TradeDocument tradeDocumentDetails);
    
    void deleteTradeDocument(Long id);
    
    List<TradeDocument> getTradeDocumentsByDocumentType(String documentType);
    
    List<TradeDocument> getTradeDocumentsByStatus(String status);
    
    List<TradeDocument> getTradeDocumentsByReferenceNumber(String referenceNumber);
    
    List<TradeDocument> getTradeDocumentsByUploadedBy(String uploadedBy);
}
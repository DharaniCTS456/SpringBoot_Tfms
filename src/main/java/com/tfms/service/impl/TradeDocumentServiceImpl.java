package com.tfms.service.impl;

import com.tfms.model.TradeDocument;
import com.tfms.repository.TradeDocumentRepository;
import com.tfms.service.TradeDocumentService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class TradeDocumentServiceImpl implements TradeDocumentService {

    private final TradeDocumentRepository tradeDocumentRepository;
    private static final String UPLOAD_DIR = "uploads/";

    public TradeDocumentServiceImpl(TradeDocumentRepository tradeDocumentRepository) {
        this.tradeDocumentRepository = tradeDocumentRepository;
    }

    @Override
    public List<TradeDocument> getAllTradeDocuments() {
        return tradeDocumentRepository.findAll();
    }

    @Override
    public TradeDocument getTradeDocumentById(Long id) {
        return tradeDocumentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Trade Document not found with ID: " + id));
    }

    @Override
    public TradeDocument getTradeDocumentByDocumentId(String documentId) {
        return tradeDocumentRepository.findByDocumentId(documentId)
                .orElseThrow(() -> new EntityNotFoundException("Trade Document not found with Document ID: " + documentId));
    }

    @Override
    public TradeDocument uploadDocument(TradeDocument tradeDocument, MultipartFile file) {
        tradeDocument.setDocumentId(generateDocumentId(tradeDocument.getDocumentId()));
        tradeDocument.setStatus(tradeDocument.getStatus() == null || tradeDocument.getStatus().isEmpty()
                ? "UPLOADED" : tradeDocument.getStatus());

        String filePath = saveFile(file);
        tradeDocument.setFilePath(filePath);

        return tradeDocumentRepository.save(tradeDocument);
    }

    @Override
    public TradeDocument updateDocumentDetails(Long id, TradeDocument tradeDocumentDetails) {
        TradeDocument tradeDocument = getTradeDocumentById(id);
        
        tradeDocument.setDocumentType(tradeDocumentDetails.getDocumentType());
        tradeDocument.setReferenceNumber(tradeDocumentDetails.getReferenceNumber());
        tradeDocument.setUploadedBy(tradeDocumentDetails.getUploadedBy());
        tradeDocument.setStatus(tradeDocumentDetails.getStatus());

        return tradeDocumentRepository.save(tradeDocument);
    }

    @Override
    public void deleteTradeDocument(Long id) {
        TradeDocument tradeDocument = getTradeDocumentById(id);
        tradeDocumentRepository.delete(tradeDocument);
    }

    @Override
    public List<TradeDocument> getTradeDocumentsByDocumentType(String documentType) {
        return tradeDocumentRepository.findByDocumentType(documentType);
    }

    @Override
    public List<TradeDocument> getTradeDocumentsByStatus(String status) {
        return tradeDocumentRepository.findByStatus(status);
    }

    @Override
    public List<TradeDocument> getTradeDocumentsByReferenceNumber(String referenceNumber) {
        return tradeDocumentRepository.findByReferenceNumber(referenceNumber);
    }

    @Override
    public List<TradeDocument> getTradeDocumentsByUploadedBy(String uploadedBy) {
        return tradeDocumentRepository.findByUploadedBy(uploadedBy);
    }

    private String generateDocumentId(String existingId) {
        return existingId == null || existingId.isEmpty() ? "DOC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase() : existingId;
    }

    private String saveFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalStateException("Cannot save an empty file.");
        }

        try {
            Path uploadPath = Path.of(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return filePath.toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file: " + file.getOriginalFilename(), e);
        }
    }
}

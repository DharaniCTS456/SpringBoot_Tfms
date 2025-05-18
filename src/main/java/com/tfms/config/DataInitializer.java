package com.tfms.config;

import com.tfms.model.*;
import com.tfms.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private LetterOfCreditRepository letterOfCreditRepository;

    @Autowired
    private BankGuaranteeRepository bankGuaranteeRepository;

    @Autowired
    private TradeDocumentRepository tradeDocumentRepository;

    @Autowired
    private RiskAssessmentRepository riskAssessmentRepository;

    @Autowired
    private ComplianceRepository complianceRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Create roles if they don't exist
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));
        
        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_ADMIN")));

        // Create users if they don't exist
        if (!userRepository.existsByUsername("user")) {
            User user = new User();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("password"));
            user.setFullName("Regular User");
            user.setEmail("user@tfms.com");
            user.setRoles(new HashSet<>(Arrays.asList(userRole)));
            userRepository.save(user);
        }

        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setFullName("Administrator");
            admin.setEmail("admin@tfms.com");
            admin.setRoles(new HashSet<>(Arrays.asList(adminRole, userRole)));
            userRepository.save(admin);
        }

        // Create sample Letters of Credit
        if (letterOfCreditRepository.count() == 0) {
            LetterOfCredit lc1 = new LetterOfCredit();
            lc1.setLcId("LC-" + UUID.randomUUID().toString().substring(0, 8));
            lc1.setApplicantName("ABC Manufacturing Ltd.");
            lc1.setBeneficiaryName("XYZ Suppliers Inc.");
            lc1.setAmount(new BigDecimal("250000.00"));
            lc1.setCurrency("USD");
            lc1.setExpiryDate(LocalDate.now().plusMonths(6));
            lc1.setStatus("ACTIVE");
            lc1.setIssueDate(LocalDate.now().minusDays(15));
            lc1.setDescription("Import of raw materials");
            lc1.setCreatedBy("admin");
            letterOfCreditRepository.save(lc1);

            LetterOfCredit lc2 = new LetterOfCredit();
            lc2.setLcId("LC-" + UUID.randomUUID().toString().substring(0, 8));
            lc2.setApplicantName("Global Trading Co.");
            lc2.setBeneficiaryName("European Exporters Ltd.");
            lc2.setAmount(new BigDecimal("180000.00"));
            lc2.setCurrency("EUR");
            lc2.setExpiryDate(LocalDate.now().plusMonths(3));
            lc2.setStatus("DRAFT");
            lc2.setDescription("Export of finished goods");
            lc2.setCreatedBy("user");
            letterOfCreditRepository.save(lc2);
        }

        // Create sample Bank Guarantees
        if (bankGuaranteeRepository.count() == 0) {
            BankGuarantee bg1 = new BankGuarantee();
            bg1.setGuaranteeId("BG-" + UUID.randomUUID().toString().substring(0, 8));
            bg1.setApplicantName("Construction Corp.");
            bg1.setBeneficiaryName("City Development Authority");
            bg1.setGuaranteeAmount(new BigDecimal("500000.00"));
            bg1.setCurrency("USD");
            bg1.setValidityPeriod("12 months");
            bg1.setStartDate(LocalDate.now());
            bg1.setEndDate(LocalDate.now().plusMonths(12));
            bg1.setStatus("ISSUED");
            bg1.setPurpose("Performance guarantee for city infrastructure project");
            bg1.setGuaranteeType("Performance");
            bg1.setCreatedBy("admin");
            bankGuaranteeRepository.save(bg1);

            BankGuarantee bg2 = new BankGuarantee();
            bg2.setGuaranteeId("BG-" + UUID.randomUUID().toString().substring(0, 8));
            bg2.setApplicantName("Import Export Traders");
            bg2.setBeneficiaryName("Foreign Supplier Corp.");
            bg2.setGuaranteeAmount(new BigDecimal("75000.00"));
            bg2.setCurrency("EUR");
            bg2.setValidityPeriod("6 months");
            bg2.setStartDate(LocalDate.now().minusDays(30));
            bg2.setEndDate(LocalDate.now().plusMonths(5));
            bg2.setStatus("REQUESTED");
            bg2.setPurpose("Customs guarantee for importation of goods");
            bg2.setGuaranteeType("Customs");
            bg2.setCreatedBy("user");
            bankGuaranteeRepository.save(bg2);
        }

        // Create sample Trade Documents
        if (tradeDocumentRepository.count() == 0) {
            TradeDocument doc1 = new TradeDocument();
            doc1.setDocumentId("DOC-" + UUID.randomUUID().toString().substring(0, 8));
            doc1.setDocumentType("Bill of Lading");
            doc1.setReferenceNumber("BL-12345");
            doc1.setUploadedBy("admin");
            doc1.setUploadDate(LocalDate.now().minusDays(5));
            doc1.setStatus("VERIFIED");
            doc1.setFileName("bill_of_lading_12345.pdf");
            doc1.setDescription("Bill of lading for shipment from Shanghai to Rotterdam");
            doc1.setCreatedBy("admin");
            tradeDocumentRepository.save(doc1);

            TradeDocument doc2 = new TradeDocument();
            doc2.setDocumentId("DOC-" + UUID.randomUUID().toString().substring(0, 8));
            doc2.setDocumentType("Commercial Invoice");
            doc2.setReferenceNumber("INV-78901");
            doc2.setUploadedBy("user");
            doc2.setUploadDate(LocalDate.now().minusDays(3));
            doc2.setStatus("PENDING");
            doc2.setFileName("commercial_invoice_78901.pdf");
            doc2.setDescription("Commercial invoice for goods exported to Germany");
            doc2.setCreatedBy("user");
            tradeDocumentRepository.save(doc2);
        }

        // Create sample Risk Assessments
        if (riskAssessmentRepository.count() == 0) {
            RiskAssessment risk1 = new RiskAssessment();
            risk1.setRiskId("RISK-" + UUID.randomUUID().toString().substring(0, 8));
            risk1.setTransactionReference("TX-67890");
            risk1.setRiskFactors("Country risk: Medium; Counterparty risk: Low; Market risk: Low");
            risk1.setRiskScore(45);
            risk1.setAssessmentDate(LocalDate.now().minusDays(10));
            risk1.setRiskLevel("MEDIUM");
            risk1.setAssessorName("admin");
            risk1.setRecommendations("Proceed with transaction but monitor country situation");
            risk1.setNextReviewDate(LocalDate.now().plusMonths(3));
            risk1.setCreatedBy("admin");
            riskAssessmentRepository.save(risk1);

            RiskAssessment risk2 = new RiskAssessment();
            risk2.setRiskId("RISK-" + UUID.randomUUID().toString().substring(0, 8));
            risk2.setTransactionReference("TX-54321");
            risk2.setRiskFactors("Country risk: High; Counterparty risk: Medium; Market risk: Medium");
            risk2.setRiskScore(75);
            risk2.setAssessmentDate(LocalDate.now().minusDays(5));
            risk2.setRiskLevel("HIGH");
            risk2.setAssessorName("user");
            risk2.setRecommendations("Additional due diligence required before proceeding");
            risk2.setNextReviewDate(LocalDate.now().plusMonths(1));
            risk2.setCreatedBy("user");
            riskAssessmentRepository.save(risk2);
        }

        // Create sample Compliance Reports
        if (complianceRepository.count() == 0) {
            Compliance comp1 = new Compliance();
            comp1.setComplianceId("COMP-" + UUID.randomUUID().toString().substring(0, 8));
            comp1.setTransactionReference("TX-67890");
            comp1.setComplianceStatus("COMPLIANT");
            comp1.setRemarks("All regulatory requirements met for this transaction");
            comp1.setReportDate(LocalDate.now().minusDays(8));
            comp1.setRegulationType("AML/KYC");
            comp1.setReportingAuthority("Financial Intelligence Unit");
            comp1.setReportPeriod("Q2 2025");
            comp1.setSubmittedBy("admin");
            comp1.setSubmissionDate(LocalDate.now().minusDays(8));
            comp1.setCreatedBy("admin");
            complianceRepository.save(comp1);

            Compliance comp2 = new Compliance();
            comp2.setComplianceId("COMP-" + UUID.randomUUID().toString().substring(0, 8));
            comp2.setTransactionReference("TX-54321");
            comp2.setComplianceStatus("PENDING");
            comp2.setRemarks("Additional documentation required for export compliance");
            comp2.setReportDate(LocalDate.now().minusDays(3));
            comp2.setRegulationType("Export Control");
            comp2.setReportingAuthority("Trade Authority");
            comp2.setReportPeriod("Q2 2025");
            comp2.setSubmittedBy("user");
            comp2.setCreatedBy("user");
            complianceRepository.save(comp2);
        }
    }
}
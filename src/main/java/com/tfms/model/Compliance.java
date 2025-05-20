package com.tfms.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "compliance_reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Compliance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Uses MySQL AUTO_INCREMENT
    private Long id;

    @Column(name = "compliance_id", nullable = false, unique = true, length = 50)
    private String complianceId;

    @Column(name = "transaction_reference", nullable = false, length = 50)
    private String transactionReference;

    @Column(name = "compliance_status", nullable = false, length = 20)
    private String complianceStatus;

    @Column(columnDefinition = "TEXT") // MySQL supports TEXT natively
    private String remarks;

    @Column(name = "report_date", nullable = false)
    private LocalDate reportDate;

    @Column(name = "regulation_type", length = 50)
    private String regulationType;

    @Column(name = "reporting_authority", length = 100)
    private String reportingAuthority;

    @Column(name = "report_period", length = 20)
    private String reportPeriod;

    @Column(name = "submitted_by", length = 50)
    private String submittedBy;

    @Column(name = "submission_date")
    private LocalDate submissionDate;

    @Column(name = "report_file_path", length = 255)
    private String reportFilePath;

    @Column(name = "created_by", length = 50)
    private String createdBy;

    @Column(name = "created_at", updatable = false) // Ensures value is set only during creation
    private LocalDate createdAt;

    @Column(name = "last_modified_by", length = 50)
    private String lastModifiedBy;

    @Column(name = "last_modified_at")
    private LocalDate lastModifiedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDate.now();
        lastModifiedAt = LocalDate.now();
        if (reportDate == null) {
            reportDate = LocalDate.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        lastModifiedAt = LocalDate.now();
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getComplianceId() {
		return complianceId;
	}

	public void setComplianceId(String complianceId) {
		this.complianceId = complianceId;
	}

	public String getTransactionReference() {
		return transactionReference;
	}

	public void setTransactionReference(String transactionReference) {
		this.transactionReference = transactionReference;
	}

	public String getComplianceStatus() {
		return complianceStatus;
	}

	public void setComplianceStatus(String complianceStatus) {
		this.complianceStatus = complianceStatus;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public LocalDate getReportDate() {
		return reportDate;
	}

	public void setReportDate(LocalDate reportDate) {
		this.reportDate = reportDate;
	}

	public String getRegulationType() {
		return regulationType;
	}

	public void setRegulationType(String regulationType) {
		this.regulationType = regulationType;
	}

	public String getReportingAuthority() {
		return reportingAuthority;
	}

	public void setReportingAuthority(String reportingAuthority) {
		this.reportingAuthority = reportingAuthority;
	}

	public String getReportPeriod() {
		return reportPeriod;
	}

	public void setReportPeriod(String reportPeriod) {
		this.reportPeriod = reportPeriod;
	}

	public String getSubmittedBy() {
		return submittedBy;
	}

	public void setSubmittedBy(String submittedBy) {
		this.submittedBy = submittedBy;
	}

	public LocalDate getSubmissionDate() {
		return submissionDate;
	}

	public void setSubmissionDate(LocalDate submissionDate) {
		this.submissionDate = submissionDate;
	}

	public String getReportFilePath() {
		return reportFilePath;
	}

	public void setReportFilePath(String reportFilePath) {
		this.reportFilePath = reportFilePath;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDate getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDate createdAt) {
		this.createdAt = createdAt;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public LocalDate getLastModifiedAt() {
		return lastModifiedAt;
	}

	public void setLastModifiedAt(LocalDate lastModifiedAt) {
		this.lastModifiedAt = lastModifiedAt;
	}
    
    
}

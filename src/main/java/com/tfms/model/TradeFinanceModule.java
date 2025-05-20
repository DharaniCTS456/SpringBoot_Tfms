package com.tfms.model;

import javax.persistence.*;

@Entity
@Table(name = "trade_finance_modules")
public class TradeFinanceModule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Uses MySQL AUTO_INCREMENT
    private Long id;

    @Column(nullable = false, unique = true, length = 100) // Added length constraint for indexing
    private String name;

    @Column(columnDefinition = "TEXT") // Allows large text storage for descriptions
    private String description;

    @Column(nullable = false) // Boolean mapping for MySQL (Stored as TINYINT(1))
    private boolean active;

    @Column(name = "status_message", length = 255)
    private String statusMessage;

    public TradeFinanceModule() {
    }

    public TradeFinanceModule(String name, String description, boolean active, String statusMessage) {
        this.name = name;
        this.description = description;
        this.active = active;
        this.statusMessage = statusMessage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
    
    
}

/**
 * Trade Finance Management System
 * Main JavaScript Functions
 */

// Execute when DOM is fully loaded
document.addEventListener('DOMContentLoaded', function() {
    initTooltips();
    initDateFormatting();
    updateCopyrightYear();
    initSidebarToggle();
});

/**
 * Initialize Bootstrap tooltips
 */
function initTooltips() {
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
}

/**
 * Set min date on date inputs to today
 */
function initDateFormatting() {
    // Format date inputs to have min date of today
    const dateInputs = document.querySelectorAll('input[type="date"]');
    const today = new Date().toISOString().split('T')[0];
    
    dateInputs.forEach(input => {
        if (!input.hasAttribute('min') && !input.classList.contains('past-allowed')) {
            input.setAttribute('min', today);
        }
    });
    
    // Format date displays
    const dateDisplays = document.querySelectorAll('.date-display');
    dateDisplays.forEach(element => {
        const dateString = element.textContent.trim();
        if (dateString) {
            try {
                const formattedDate = formatDate(dateString);
                element.textContent = formattedDate;
            } catch (e) {
                console.error('Error formatting date:', e);
            }
        }
    });
    
    // Format currency displays
    const currencyDisplays = document.querySelectorAll('.currency-display');
    currencyDisplays.forEach(element => {
        const amount = parseFloat(element.getAttribute('data-amount'));
        const currency = element.getAttribute('data-currency') || 'USD';
        if (!isNaN(amount)) {
            element.textContent = formatCurrency(amount, currency);
        }
    });
}

/**
 * Update copyright year in footer
 */
function updateCopyrightYear() {
    const yearElement = document.getElementById('current-year');
    if (yearElement) {
        yearElement.textContent = new Date().getFullYear();
    }
}

/**
 * Initialize sidebar toggle for mobile
 */
function initSidebarToggle() {
    const sidebarToggle = document.getElementById('sidebarToggle');
    if (sidebarToggle) {
        sidebarToggle.addEventListener('click', function() {
            document.body.classList.toggle('sb-sidenav-toggled');
        });
    }
}

/**
 * Format currency values with proper formatting
 * @param {number} amount - The amount to format
 * @param {string} currency - The currency code (USD, EUR, etc)
 * @returns {string} Formatted currency string
 */
function formatCurrency(amount, currency) {
    return new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: currency
    }).format(amount);
}

/**
 * Format dates into a readable format
 * @param {string} dateString - ISO date string
 * @returns {string} Formatted date string (DD-MM-YYYY)
 */
function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString('en-GB', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric'
    });
}

/**
 * Show notification message
 * @param {string} message - The message to display
 * @param {string} type - The type of message (success, danger, warning, info)
 */
function showNotification(message, type = 'info') {
    // Create the notification element
    const notification = document.createElement('div');
    notification.className = `alert alert-${type} alert-dismissible fade show notification-toast`;
    notification.innerHTML = `
        <i class="fas fa-${type === 'success' ? 'check-circle' : type === 'danger' ? 'exclamation-circle' : type === 'warning' ? 'exclamation-triangle' : 'info-circle'} me-2"></i>
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    `;
    
    // Add to page
    const container = document.querySelector('.notification-container') || document.body;
    container.appendChild(notification);
    
    // Auto-remove after delay
    setTimeout(() => {
        notification.classList.remove('show');
        setTimeout(() => {
            notification.remove();
        }, 500);
    }, 5000);
}

/**
 * Validate a form with custom rules
 * @param {HTMLFormElement} form - The form to validate
 * @returns {boolean} True if valid, false otherwise
 */
function validateForm(form) {
    // Basic form validation
    if (!form.checkValidity()) {
        form.classList.add('was-validated');
        return false;
    }
    
    // Custom validations can be added here
    
    return true;
}

/**
 * Print the current page
 */
function printPage() {
    window.print();
}

/**
 * Confirm an action with a modal
 * @param {string} message - Confirmation message
 * @param {Function} callback - Function to call if confirmed
 */
function confirmAction(message, callback) {
    if (window.confirm(message)) {
        callback();
    }
}

/**
 * Delete a record after confirmation
 * @param {string} endpoint - The API endpoint to call
 * @param {string} redirectUrl - URL to redirect to after deletion
 */
function deleteRecord(endpoint, redirectUrl) {
    confirmAction('Are you sure you want to delete this record? This action cannot be undone.', () => {
        fetch(endpoint, {
            method: 'POST',
            headers: {
                'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]').getAttribute('content')
            }
        })
        .then(response => {
            if (response.ok) {
                window.location.href = redirectUrl;
            } else {
                showNotification('Error deleting record. Please try again.', 'danger');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            showNotification('Error deleting record. Please try again.', 'danger');
        });
    });
}
// Global Application JavaScript

document.addEventListener('DOMContentLoaded', function() {
    console.log('Matrimony Website Loaded');

    // Initialize components
    initNavigation();
    initAlerts();
    initTooltips();
});

// Navigation Toggle for Mobile
function initNavigation() {
    const mobileMenuToggle = document.querySelector('.mobile-menu-toggle');
    const navLinks = document.querySelector('.nav-links');

    if (mobileMenuToggle) {
        mobileMenuToggle.addEventListener('click', function() {
            navLinks.classList.toggle('active');
        });
    }
}

// Auto-hide alerts after 5 seconds
function initAlerts() {
    const alerts = document.querySelectorAll('.alert');

    alerts.forEach(alert => {
        setTimeout(() => {
            alert.style.opacity = '0';
            setTimeout(() => alert.remove(), 300);
        }, 5000);
    });
}

// Tooltip functionality
function initTooltips() {
    const tooltipElements = document.querySelectorAll('[data-tooltip]');

    tooltipElements.forEach(element => {
        element.addEventListener('mouseenter', function() {
            const tooltipText = this.getAttribute('data-tooltip');
            const tooltip = document.createElement('div');
            tooltip.className = 'tooltip';
            tooltip.textContent = tooltipText;
            document.body.appendChild(tooltip);

            const rect = this.getBoundingClientRect();
            tooltip.style.left = rect.left + (rect.width / 2) - (tooltip.offsetWidth / 2) + 'px';
            tooltip.style.top = rect.top - tooltip.offsetHeight - 10 + 'px';

            this._tooltip = tooltip;
        });

        element.addEventListener('mouseleave', function() {
            if (this._tooltip) {
                this._tooltip.remove();
                this._tooltip = null;
            }
        });
    });
}

// Show loading spinner
function showLoading() {
    const spinner = document.createElement('div');
    spinner.className = 'spinner';
    spinner.id = 'global-spinner';
    document.body.appendChild(spinner);
}

// Hide loading spinner
function hideLoading() {
    const spinner = document.getElementById('global-spinner');
    if (spinner) {
        spinner.remove();
    }
}

// Show alert message
function showAlert(message, type = 'info') {
    const alert = document.createElement('div');
    alert.className = `alert alert-${type}`;
    alert.textContent = message;

    const container = document.querySelector('.container');
    if (container) {
        container.insertBefore(alert, container.firstChild);

        setTimeout(() => {
            alert.style.opacity = '0';
            setTimeout(() => alert.remove(), 300);
        }, 5000);
    }
}

// Form validation helper
function validateForm(formId) {
    const form = document.getElementById(formId);
    if (!form) return false;

    const requiredFields = form.querySelectorAll('[required]');
    let isValid = true;

    requiredFields.forEach(field => {
        if (!field.value.trim()) {
            isValid = false;
            field.classList.add('error');
            showFieldError(field, 'This field is required');
        } else {
            field.classList.remove('error');
            removeFieldError(field);
        }
    });

    return isValid;
}

// Show field error
function showFieldError(field, message) {
    let errorElement = field.nextElementSibling;

    if (!errorElement || !errorElement.classList.contains('form-error')) {
        errorElement = document.createElement('div');
        errorElement.className = 'form-error';
        field.parentNode.insertBefore(errorElement, field.nextSibling);
    }

    errorElement.textContent = message;
}

// Remove field error
function removeFieldError(field) {
    const errorElement = field.nextElementSibling;
    if (errorElement && errorElement.classList.contains('form-error')) {
        errorElement.remove();
    }
}

// Debounce function for search inputs
function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

// Format date
function formatDate(dateString) {
    const options = { year: 'numeric', month: 'long', day: 'numeric' };
    return new Date(dateString).toLocaleDateString(undefined, options);
}

// Export functions for use in other files
window.matrimonyApp = {
    showLoading,
    hideLoading,
    showAlert,
    validateForm,
    showFieldError,
    removeFieldError,
    debounce,
    formatDate
};
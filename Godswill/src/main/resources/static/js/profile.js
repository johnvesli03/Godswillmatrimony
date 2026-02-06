// Profile Management JavaScript

document.addEventListener('DOMContentLoaded', function() {
    initImageUpload();
    initProfileForm();
    initProfileActions();
});

// Image Upload Preview
function initImageUpload() {
    const fileInput = document.getElementById('profileImage');
    const imagePreview = document.querySelector('.image-preview');

    if (fileInput && imagePreview) {
        fileInput.addEventListener('change', function(e) {
            const file = e.target.files[0];

            if (file) {
                if (file.size > 5 * 1024 * 1024) { // 5MB limit
                    window.matrimonyApp.showAlert('Image size should be less than 5MB', 'error');
                    this.value = '';
                    return;
                }

                if (!file.type.match('image.*')) {
                    window.matrimonyApp.showAlert('Please select a valid image file', 'error');
                    this.value = '';
                    return;
                }

                const reader = new FileReader();

                reader.onload = function(event) {
                    imagePreview.innerHTML = `<img src="${event.target.result}" alt="Profile Preview">`;
                };

                reader.readAsDataURL(file);
            }
        });
    }
}

// Profile Form Validation
function initProfileForm() {
    const profileForm = document.getElementById('profileForm');

    if (profileForm) {
        profileForm.addEventListener('submit', function(e) {
            if (!validateProfileForm()) {
                e.preventDefault();
                window.matrimonyApp.showAlert('Please fill all required fields correctly', 'error');
            }
        });

        // Real-time validation
        const inputs = profileForm.querySelectorAll('input, select, textarea');
        inputs.forEach(input => {
            input.addEventListener('blur', function() {
                validateField(this);
            });
        });
    }
}

// Validate individual field
function validateField(field) {
    const value = field.value.trim();
    const fieldName = field.name;
    let isValid = true;
    let errorMessage = '';

    // Required field check
    if (field.hasAttribute('required') && !value) {
        isValid = false;
        errorMessage = 'This field is required';
    }

    // Email validation
    if (fieldName === 'email' && value) {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(value)) {
            isValid = false;
            errorMessage = 'Please enter a valid email address';
        }
    }

    // Phone validation
    if (fieldName === 'phone' && value) {
        const phoneRegex = /^[0-9]{10}$/;
        if (!phoneRegex.test(value.replace(/\D/g, ''))) {
            isValid = false;
            errorMessage = 'Please enter a valid 10-digit phone number';
        }
    }

    // Age validation
    if (fieldName === 'age' && value) {
        const age = parseInt(value);
        if (age < 18 || age > 100) {
            isValid = false;
            errorMessage = 'Age must be between 18 and 100';
        }
    }

    // Update UI
    if (!isValid) {
        field.classList.add('error');
        window.matrimonyApp.showFieldError(field, errorMessage);
    } else {
        field.classList.remove('error');
        window.matrimonyApp.removeFieldError(field);
    }

    return isValid;
}

// Validate entire profile form
function validateProfileForm() {
    const form = document.getElementById('profileForm');
    if (!form) return false;

    const requiredFields = form.querySelectorAll('[required]');
    let isValid = true;

    requiredFields.forEach(field => {
        if (!validateField(field)) {
            isValid = false;
        }
    });

    return isValid;
}

// Profile Action Buttons
function initProfileActions() {
    // View Profile Button
    const viewProfileBtns = document.querySelectorAll('.btn-view-profile');
    viewProfileBtns.forEach(btn => {
        btn.addEventListener('click', function(e) {
            e.preventDefault();
            const profileId = this.getAttribute('data-profile-id');
            viewProfile(profileId);
        });
    });

    // Contact Button
    const contactBtns = document.querySelectorAll('.btn-contact');
    contactBtns.forEach(btn => {
        btn.addEventListener('click', function(e) {
            e.preventDefault();
            const profileId = this.getAttribute('data-profile-id');
            contactProfile(profileId);
        });
    });

    // Shortlist Button
    const shortlistBtns = document.querySelectorAll('.btn-shortlist');
    shortlistBtns.forEach(btn => {
        btn.addEventListener('click', function(e) {
            e.preventDefault();
            const profileId = this.getAttribute('data-profile-id');
            toggleShortlist(profileId, this);
        });
    });
}

// View Profile
function viewProfile(profileId) {
    window.location.href = `/profile/${profileId}`;
}

// Contact Profile
function contactProfile(profileId) {
    // Check if user is logged in
    const isLoggedIn = document.body.hasAttribute('data-logged-in');

    if (!isLoggedIn) {
        window.matrimonyApp.showAlert('Please login to contact profiles', 'info');
        setTimeout(() => {
            window.location.href = '/login';
        }, 1500);
        return;
    }

    // Make API call to get contact details
    window.matrimonyApp.showLoading();

    fetch(`/api/profile/${profileId}/contact`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        }
    })
        .then(response => response.json())
        .then(data => {
            window.matrimonyApp.hideLoading();
            if (data.success) {
                showContactModal(data.contact);
            } else {
                window.matrimonyApp.showAlert(data.message || 'Unable to get contact details', 'error');
            }
        })
        .catch(error => {
            window.matrimonyApp.hideLoading();
            window.matrimonyApp.showAlert('An error occurred. Please try again.', 'error');
            console.error('Error:', error);
        });
}

// Show Contact Modal
function showContactModal(contact) {
    const modal = document.createElement('div');
    modal.className = 'modal';
    modal.innerHTML = `
        <div class="modal-content">
            <span class="modal-close">&times;</span>
            <h2>Contact Information</h2>
            <div class="contact-details">
                <p><strong>Phone:</strong> ${contact.phone}</p>
                <p><strong>Email:</strong> ${contact.email}</p>
            </div>
        </div>
    `;

    document.body.appendChild(modal);

    const closeBtn = modal.querySelector('.modal-close');
    closeBtn.addEventListener('click', () => modal.remove());

    modal.addEventListener('click', function(e) {
        if (e.target === modal) {
            modal.remove();
        }
    });
}

// Toggle Shortlist
function toggleShortlist(profileId, button) {
    const isLoggedIn = document.body.hasAttribute('data-logged-in');

    if (!isLoggedIn) {
        window.matrimonyApp.showAlert('Please login to shortlist profiles', 'info');
        setTimeout(() => {
            window.location.href = '/login';
        }, 1500);
        return;
    }

    const isShortlisted = button.classList.contains('shortlisted');
    const action = isShortlisted ? 'remove' : 'add';

    fetch(`/api/profile/${profileId}/shortlist`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ action: action })
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                button.classList.toggle('shortlisted');
                button.textContent = isShortlisted ? 'Shortlist' : 'Shortlisted';
                window.matrimonyApp.showAlert(data.message, 'success');
            } else {
                window.matrimonyApp.showAlert(data.message || 'Unable to update shortlist', 'error');
            }
        })
        .catch(error => {
            window.matrimonyApp.showAlert('An error occurred. Please try again.', 'error');
            console.error('Error:', error);
        });
}

// Auto-save draft (for profile creation)
let autoSaveTimeout;
function autoSaveDraft() {
    clearTimeout(autoSaveTimeout);

    autoSaveTimeout = setTimeout(() => {
        const form = document.getElementById('profileForm');
        if (!form) return;

        const formData = new FormData(form);
        const data = Object.fromEntries(formData);

        localStorage.setItem('profileDraft', JSON.stringify(data));
        console.log('Draft saved');
    }, 2000);
}

// Load draft on page load
function loadDraft() {
    const draft = localStorage.getItem('profileDraft');
    if (draft) {
        const data = JSON.parse(draft);
        const form = document.getElementById('profileForm');

        if (form && confirm('Do you want to load your saved draft?')) {
            Object.keys(data).forEach(key => {
                const field = form.elements[key];
                if (field) {
                    field.value = data[key];
                }
            });
        }
    }
}

// Clear draft
function clearDraft() {
    localStorage.removeItem('profileDraft');
}

// Initialize draft functionality
if (document.getElementById('profileForm')) {
    loadDraft();

    const formInputs = document.querySelectorAll('#profileForm input, #profileForm select, #profileForm textarea');
    formInputs.forEach(input => {
        input.addEventListener('input', autoSaveDraft);
    });

    document.getElementById('profileForm').addEventListener('submit', clearDraft);
}
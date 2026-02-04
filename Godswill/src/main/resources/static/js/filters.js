// Filter and Search Functionality

document.addEventListener('DOMContentLoaded', function() {
    initFilters();
    initSearch();
    initSorting();
});

// Initialize Filters
function initFilters() {
    const filterForm = document.getElementById('filterForm');

    if (filterForm) {
        // Apply filters on change
        const filterInputs = filterForm.querySelectorAll('select, input[type="checkbox"]');

        filterInputs.forEach(input => {
            input.addEventListener('change', function() {
                applyFilters();
            });
        });

        // Reset filters
        const resetBtn = document.getElementById('resetFilters');
        if (resetBtn) {
            resetBtn.addEventListener('click', function(e) {
                e.preventDefault();
                resetFilters();
            });
        }
    }
}

// Apply Filters
function applyFilters() {
    const filterForm = document.getElementById('filterForm');
    if (!filterForm) return;

    const formData = new FormData(filterForm);
    const params = new URLSearchParams(formData);

    // Show loading
    window.matrimonyApp.showLoading();

    // Update URL
    const newUrl = `${window.location.pathname}?${params.toString()}`;
    window.history.pushState({}, '', newUrl);

    // Fetch filtered results
    fetch(`/api/profiles/filter?${params.toString()}`, {
        headers: {
            'Accept': 'application/json'
        }
    })
        .then(response => response.json())
        .then(data => {
            window.matrimonyApp.hideLoading();
            updateProfileGrid(data.profiles);
            updateResultsCount(data.total);
        })
        .catch(error => {
            window.matrimonyApp.hideLoading();
            window.matrimonyApp.showAlert('Error loading profiles', 'error');
            console.error('Error:', error);
        });
}

// Reset Filters
function resetFilters() {
    const filterForm = document.getElementById('filterForm');
    if (!filterForm) return;

    filterForm.reset();

    // Clear URL params
    window.history.pushState({}, '', window.location.pathname);

    // Reload profiles
    applyFilters();
}

// Initialize Search
function initSearch() {
    const searchInput = document.getElementById('profileSearch');

    if (searchInput) {
        const debouncedSearch = window.matrimonyApp.debounce(function(value) {
            performSearch(value);
        }, 500);

        searchInput.addEventListener('input', function() {
            debouncedSearch(this.value);
        });
    }
}

// Perform Search
function performSearch(query) {
    if (query.length < 3 && query.length > 0) {
        return; // Minimum 3 characters
    }

    window.matrimonyApp.showLoading();

    fetch(`/api/profiles/search?q=${encodeURIComponent(query)}`, {
        headers: {
            'Accept': 'application/json'
        }
    })
        .then(response => response.json())
        .then(data => {
            window.matrimonyApp.hideLoading();
            updateProfileGrid(data.profiles);
            updateResultsCount(data.total);
        })
        .catch(error => {
            window.matrimonyApp.hideLoading();
            window.matrimonyApp.showAlert('Error searching profiles', 'error');
            console.error('Error:', error);
        });
}

// Initialize Sorting
function initSorting() {
    const sortSelect = document.getElementById('sortBy');

    if (sortSelect) {
        sortSelect.addEventListener('change', function() {
            applySorting(this.value);
        });
    }
}

// Apply Sorting
function applySorting(sortBy) {
    const currentParams = new URLSearchParams(window.location.search);
    currentParams.set('sort', sortBy);

    window.matrimonyApp.showLoading();

    fetch(`/api/profiles?${currentParams.toString()}`, {
        headers: {
            'Accept': 'application/json'
        }
    })
        .then(response => response.json())
        .then(data => {
            window.matrimonyApp.hideLoading();
            updateProfileGrid(data.profiles);
        })
        .catch(error => {
            window.matrimonyApp.hideLoading();
            window.matrimonyApp.showAlert('Error sorting profiles', 'error');
            console.error('Error:', error);
        });
}

// Update Profile Grid
function updateProfileGrid(profiles) {
    const profileGrid = document.querySelector('.profile-grid');

    if (!profileGrid) return;

    if (profiles.length === 0) {
        profileGrid.innerHTML = `
            <div class="no-results">
                <div class="no-results-icon">😔</div>
                <h3>No profiles found</h3>
                <p>Try adjusting your filters or search criteria</p>
            </div>
        `;
        return;
    }

    profileGrid.innerHTML = profiles.map(profile => createProfileCard(profile)).join('');

    // Re-initialize profile actions
    if (window.initProfileActions) {
        window.initProfileActions();
    }
}

// Create Profile Card HTML
function createProfileCard(profile) {
    return `
        <div class="card profile-card">
            <div class="profile-card-image">
                <img src="${profile.imageUrl || '/images/default-avatar.jpg'}" alt="${profile.name}">
                ${profile.verified ? '<div class="profile-verified"><span class="verified-badge">Verified</span></div>' : ''}
            </div>
            <div class="profile-card-content">
                <h3>${profile.name}, ${profile.age}</h3>
                <div class="profile-info">
                    <div class="profile-info-item">${profile.religion}</div>
                    <div class="profile-info-item">${profile.location}</div>
                    <div class="profile-info-item">${profile.education}</div>
                    <div class="profile-info-item">${profile.profession}</div>
                </div>
                <div class="profile-card-actions">
                    <button class="btn btn-primary btn-view-profile" data-profile-id="${profile.id}">
                        View Profile
                    </button>
                    <button class="btn btn-outline btn-shortlist" data-profile-id="${profile.id}">
                        Shortlist
                    </button>
                </div>
            </div>
        </div>
    `;
}

// Update Results Count
function updateResultsCount(total) {
    const resultsCount = document.querySelector('.results-count');
    if (resultsCount) {
        resultsCount.textContent = `Showing ${total} profile${total !== 1 ? 's' : ''}`;
    }
}

// Verified Only Filter Toggle
const verifiedOnlyCheckbox = document.getElementById('verifiedOnly');
if (verifiedOnlyCheckbox) {
    verifiedOnlyCheckbox.addEventListener('change', function() {
        const profileCards = document.querySelectorAll('.profile-card');

        profileCards.forEach(card => {
            const isVerified = card.querySelector('.verified-badge');

            if (this.checked) {
                card.style.display = isVerified ? 'block' : 'none';
            } else {
                card.style.display = 'block';
            }
        });

        // Update count
        const visibleCards = document.querySelectorAll('.profile-card:not([style*="display: none"])');
        updateResultsCount(visibleCards.length);
    });
}
/**
 * OpportunityHub - Core Frontend Logic
 */
const API_BASE_URL = window.location.origin.includes('8080') ? '' : 'http://localhost:8080';

// App State
let currentUser = null;
let currentCategory = 'All';
let currentView = 'all'; // 'all', 'recommended', 'saved'
let currentSearch = '';

document.addEventListener('DOMContentLoaded', () => {
    initAuth();
    initModals();
    initFilters();
    initTabs();
    initSearch();
    fetchFeed();
});

/* ==========================================================================
   Authentication & User State
   ========================================================================== */
function initAuth() {
    const storedUser = localStorage.getItem('oppHubUser');
    if (storedUser) {
        currentUser = JSON.parse(storedUser);
        updateNavForAuth(true);
    } else {
        updateNavForAuth(false);
    }

    document.getElementById('login-form').addEventListener('submit', handleLogin);
    document.getElementById('register-form').addEventListener('submit', handleRegister);
    document.getElementById('btn-logout').addEventListener('click', handleLogout);
}

function updateNavForAuth(isLoggedIn) {
    const authActions = document.getElementById('auth-actions');
    const userActions = document.getElementById('user-actions');
    const tabRec = document.getElementById('tab-recommended');
    const tabSaved = document.getElementById('tab-saved');
    const btnAdmin = document.getElementById('btn-admin');
    
    if (isLoggedIn) {
        authActions.style.display = 'none';
        userActions.style.display = 'flex';
        tabRec.style.display = 'inline-block';
        tabSaved.style.display = 'inline-block';
        
        let name = currentUser.fullName ? currentUser.fullName.split(' ')[0] : 'Admin';
        document.getElementById('user-greeting').textContent = `Hi, ${name}!`;

        if (currentUser.role === 'ADMIN') {
            btnAdmin.style.display = 'inline-flex';
        } else {
            btnAdmin.style.display = 'none';
        }
    } else {
        authActions.style.display = 'flex';
        userActions.style.display = 'none';
        tabRec.style.display = 'none';
        tabSaved.style.display = 'none';
        btnAdmin.style.display = 'none';
        
        if(currentView !== 'all') {
            document.querySelector('[data-view="all"]').click();
        }
    }
}

async function handleLogin(e) {
    e.preventDefault();
    const email = document.getElementById('login-email').value;
    const password = document.getElementById('login-password').value;
    const errorEl = document.getElementById('login-error');
    
    try {
        const res = await fetch(`${API_BASE_URL}/api/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password })
        });
        const data = await res.json();
        
        if (res.ok) {
            currentUser = data;
            localStorage.setItem('oppHubUser', JSON.stringify(data));
            updateNavForAuth(true);
            closeModal('login-modal');
            errorEl.style.display = 'none';
            document.getElementById('login-form').reset();
            fetchFeed();
        } else {
            errorEl.textContent = data.error || 'Login failed';
            errorEl.style.display = 'block';
        }
    } catch (err) {
        errorEl.textContent = 'Network error. Try again.';
        errorEl.style.display = 'block';
    }
}

async function handleRegister(e) {
    e.preventDefault();
    const fullName = document.getElementById('reg-name').value;
    const email = document.getElementById('reg-email').value;
    const password = document.getElementById('reg-password').value;
    const errorEl = document.getElementById('register-error');
    
    try {
        const res = await fetch(`${API_BASE_URL}/api/auth/register`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ fullName, email, password })
        });
        const data = await res.json();
        
        if (res.ok) {
            currentUser = data;
            localStorage.setItem('oppHubUser', JSON.stringify(data));
            updateNavForAuth(true);
            closeModal('register-modal');
            errorEl.style.display = 'none';
            document.getElementById('register-form').reset();
            
            // Generate some default skills for a new user so recommendations work immediately!
            await fetch(`${API_BASE_URL}/api/profile/${currentUser.id}/tags`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ skills: ["Java", "Spring Boot", "HTML"], categories: ["Internships", "Hackathons"] })
            });
            
            fetchFeed();
        } else {
            errorEl.textContent = data.error || 'Registration failed';
            errorEl.style.display = 'block';
        }
    } catch (err) {
        errorEl.textContent = 'Network error. Try again.';
        errorEl.style.display = 'block';
    }
}

function handleLogout() {
    currentUser = null;
    localStorage.removeItem('oppHubUser');
    updateNavForAuth(false);
    fetchFeed(); 
}

/* ==========================================================================
   Modals & Admin Panel
   ========================================================================== */
function initModals() {
    document.getElementById('btn-login-modal').addEventListener('click', () => openModal('login-modal'));
    document.getElementById('btn-register-modal').addEventListener('click', () => openModal('register-modal'));
    document.getElementById('btn-admin').addEventListener('click', () => openModal('admin-modal'));
    
    document.getElementById('close-login').addEventListener('click', () => closeModal('login-modal'));
    document.getElementById('close-register').addEventListener('click', () => closeModal('register-modal'));
    document.getElementById('close-admin').addEventListener('click', () => closeModal('admin-modal'));
    
    document.getElementById('admin-form').addEventListener('submit', handleAdminSubmit);
    
    window.addEventListener('click', (e) => {
        if (e.target.classList.contains('modal-overlay')) {
            e.target.classList.remove('active');
        }
    });
}

function openModal(id) { document.getElementById(id).classList.add('active'); }
function closeModal(id) { document.getElementById(id).classList.remove('active'); }

async function handleAdminSubmit(e) {
    e.preventDefault();
    const skillsRaw = document.getElementById('opp-skills').value;
    const skillsArray = skillsRaw.split(',').map(s => s.trim()).filter(s => s.length > 0);
    
    const payload = {
        title: document.getElementById('opp-title').value,
        organization: document.getElementById('opp-org').value,
        description: document.getElementById('opp-desc').value,
        category: document.getElementById('opp-category').value,
        location: document.getElementById('opp-location').value,
        deadline: document.getElementById('opp-deadline').value,
        stipend: parseFloat(document.getElementById('opp-stipend').value),
        skillsRequired: skillsArray,
        mode: "ONLINE",
        fee: 0,
        applicationUrl: "https://example.com/apply"
    };

    try {
        const res = await fetch(`${API_BASE_URL}/api/opportunities`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });
        
        if (res.ok) {
            closeModal('admin-modal');
            document.getElementById('admin-form').reset();
            
            // Force refresh feed
            document.querySelector('[data-view="all"]').click();
            alert("Opportunity Successfully Published!");
        } else {
            document.getElementById('admin-error').textContent = 'Failed to publish.';
            document.getElementById('admin-error').style.display = 'block';
        }
    } catch (err) {
        document.getElementById('admin-error').textContent = 'Network error.';
        document.getElementById('admin-error').style.display = 'block';
    }
}

/* ==========================================================================
   Tabs & Filters
   ========================================================================== */
function initTabs() {
    const tabs = document.querySelectorAll('.tab-btn');
    const filterSection = document.getElementById('category-filters');
    
    tabs.forEach(tab => {
        tab.addEventListener('click', () => {
            tabs.forEach(t => t.classList.remove('active'));
            tab.classList.add('active');
            currentView = tab.getAttribute('data-view');
            
            // Only show category filters and search on "All" view
            if(currentView === 'all') {
                filterSection.style.display = 'flex';
                document.querySelector('.search-wrapper').style.display = 'flex';
                document.getElementById('feed-main-title').textContent = 'Latest Opportunities';
            } else if (currentView === 'recommended') {
                filterSection.style.display = 'none';
                document.querySelector('.search-wrapper').style.display = 'none';
                document.getElementById('feed-main-title').textContent = 'Personalized For You';
            } else {
                filterSection.style.display = 'none';
                document.querySelector('.search-wrapper').style.display = 'none';
                document.getElementById('feed-main-title').textContent = 'Your Saved Items';
            }
            
            fetchFeed();
        });
    });
}

function initFilters() {
    const pills = document.querySelectorAll('.quick-category-pills .pill');
    pills.forEach(pill => {
        pill.addEventListener('click', () => {
            pills.forEach(p => p.classList.remove('active'));
            pill.classList.add('active');
            
            const rawCategory = pill.getAttribute('data-category');
            currentCategory = rawCategory === 'All' ? null : rawCategory;
            fetchFeed();
        });
    });
}

function initSearch() {
    const searchBtn = document.getElementById('btn-search');
    const searchInput = document.getElementById('search-input');
    
    searchBtn.addEventListener('click', () => {
        currentSearch = searchInput.value.trim();
        fetchFeed();
    });
    
    searchInput.addEventListener('keyup', (e) => {
        if (e.key === 'Enter') {
            currentSearch = searchInput.value.trim();
            fetchFeed();
        }
    });
}

/* ==========================================================================
   Feed Rendering
   ========================================================================== */
async function fetchFeed() {
    const feed = document.getElementById('opportunities-feed');
    const count = document.getElementById('feed-count');
    feed.innerHTML = '<div class="loading-spinner">Loading opportunities...</div>';
    
    try {
        // Fetch bookmarked IDs to highlight buttons
        let savedOppIds = new Set();
        if (currentUser) {
            const bRes = await fetch(`${API_BASE_URL}/api/bookmarks/${currentUser.id}`);
            if (bRes.ok) {
                const bookmarks = await bRes.json();
                bookmarks.forEach(b => savedOppIds.add(b.opportunity.id));
            }
        }

        let opportunities = [];

        if (currentView === 'all') {
            let url = `${API_BASE_URL}/api/opportunities?size=20`;
            if (currentCategory && currentCategory !== 'All') {
                url += `&category=${encodeURIComponent(currentCategory)}`;
            }
            if (currentSearch) {
                url += `&search=${encodeURIComponent(currentSearch)}`;
            }
            const response = await fetch(url);
            const page = await response.json();
            opportunities = page.content;
            
        } else if (currentView === 'recommended' && currentUser) {
            const response = await fetch(`${API_BASE_URL}/api/recommendations/${currentUser.id}`);
            const recommendations = await response.json();
            // Map the recommendation wrappers back to plain opportunity objects for the renderer
            opportunities = recommendations.map(rec => {
                const opp = rec.opportunity;
                opp._matchScore = rec.matchScore; // Attach score for display
                return opp;
            });
            
        } else if (currentView === 'saved' && currentUser) {
            const response = await fetch(`${API_BASE_URL}/api/bookmarks/${currentUser.id}`);
            const bookmarks = await response.json();
            // Map bookmark wrappers to opportunity objects
            opportunities = bookmarks.map(b => b.opportunity);
        }
        
        count.textContent = `${opportunities.length} found`;
        
        if (opportunities.length === 0) {
            feed.innerHTML = '<div class="loading-spinner">No opportunities found.</div>';
            return;
        }

        feed.innerHTML = opportunities.map(opp => {
            const isSaved = savedOppIds.has(opp.id);
            const skillsHtml = opp.skillsRequired.slice(0, 3).map(s => `<span class="skill-tag">${s}</span>`).join('');
            const scoreBadge = opp._matchScore ? `<div class="match-score">★ ${opp._matchScore} Point Match</div>` : '';
            
            return `
                <div class="opp-card">
                    ${scoreBadge}
                    <div class="opp-header">
                        <span class="opp-category">${opp.category}</span>
                    </div>
                    <h3 class="opp-title">${opp.title}</h3>
                    <div class="opp-org">🏢 ${opp.organization}</div>
                    
                    <div class="opp-meta">
                        <span class="meta-item">📍 ${opp.location}</span>
                        <span class="meta-item">💰 ₹${opp.stipend}</span>
                        <span class="meta-item">⏳ Deadline: ${opp.deadline}</span>
                    </div>
                    
                    <p class="opp-desc">${opp.description}</p>
                    
                    <div class="opp-skills">
                        ${skillsHtml}
                        ${opp.skillsRequired.length > 3 ? `<span class="skill-tag">+${opp.skillsRequired.length - 3}</span>` : ''}
                    </div>
                    
                    <div class="opp-footer">
                        <a href="${opp.applicationUrl}" target="_blank" class="btn btn-primary">Apply Now</a>
                        <button class="btn-bookmark ${isSaved ? 'saved' : ''}" onclick="toggleBookmark(${opp.id}, this)" title="Save for later">
                            <svg width="20" height="20" viewBox="0 0 24 24" fill="${isSaved ? 'currentColor' : 'none'}" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                                <path d="m19 21-7-4-7 4V5a2 2 0 0 1 2-2h10a2 2 0 0 1 2 2v16z"></path>
                            </svg>
                        </button>
                    </div>
                </div>
            `;
        }).join('');
        
    } catch (error) {
        console.error('Failed to load feed:', error);
        feed.innerHTML = '<div class="loading-spinner" style="color: var(--danger);">Failed to load opportunities. Backend might be offline.</div>';
    }
}

async function toggleBookmark(oppId, btnElement) {
    if (!currentUser) {
        openModal('login-modal');
        return;
    }
    
    const isCurrentlySaved = btnElement.classList.contains('saved');
    
    try {
        if (isCurrentlySaved) {
            await fetch(`${API_BASE_URL}/api/bookmarks/${currentUser.id}/${oppId}`, { method: 'DELETE' });
            btnElement.classList.remove('saved');
            btnElement.querySelector('svg').setAttribute('fill', 'none');
            
            // If we are currently looking at the saved tab, removing a bookmark should remove it from the screen
            if (currentView === 'saved') {
                fetchFeed(); 
            }
        } else {
            await fetch(`${API_BASE_URL}/api/bookmarks/${currentUser.id}/${oppId}`, { method: 'POST' });
            btnElement.classList.add('saved');
            btnElement.querySelector('svg').setAttribute('fill', 'currentColor');
        }
    } catch (error) {
        alert('Failed to update bookmark. Please try again.');
    }
}

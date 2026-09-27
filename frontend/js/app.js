/**
 * OpportunityHub - Core Frontend Logic
 * Connects frontend client to Spring Boot REST APIs.
 */

// Centralized API Base URL configuration
const API_BASE_URL = window.location.origin.includes('8080')
    ? ''
    : 'http://localhost:8080';

document.addEventListener('DOMContentLoaded', () => {
    checkSystemStatus();
    // Poll system status every 15 seconds to keep dashboard live
    setInterval(checkSystemStatus, 15000);
});

/**
 * Checks connection to the Spring Boot REST API
 */
async function checkSystemStatus() {
    const statusBadge = document.getElementById('system-status-badge');
    const dbStatusEl = document.getElementById('db-status');
    const javaVersionEl = document.getElementById('java-version');
    const serverTimeEl = document.getElementById('server-time');
    const profileEl = document.getElementById('active-profile');

    try {
        const response = await fetch(`${API_BASE_URL}/api/status`);
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.json();

        // Update UI with real backend telemetry
        if (statusBadge) {
            statusBadge.className = 'status-badge online';
            statusBadge.innerHTML = '<span class="pulse-dot"></span> Backend Online';
        }

        if (dbStatusEl && data.database) {
            const isConnected = data.database.connected;
            dbStatusEl.innerHTML = isConnected
                ? `<span style="color: var(--success); font-weight: 600;">● Connected (${data.database.databaseProduct || 'Active'})</span>`
                : `<span style="color: var(--danger); font-weight: 600;">● Disconnected</span>`;
        }

        if (javaVersionEl) {
            javaVersionEl.textContent = data.javaVersion || '17+';
        }

        if (serverTimeEl) {
            serverTimeEl.textContent = data.serverTime || new Date().toLocaleTimeString();
        }

        if (profileEl) {
            profileEl.textContent = data.activeProfile || 'default';
        }

    } catch (error) {
        console.warn('Backend API connection check failed:', error);
        if (statusBadge) {
            statusBadge.className = 'status-badge offline';
            statusBadge.innerHTML = '<span class="pulse-dot"></span> Backend Connecting / Offline';
        }
        if (dbStatusEl) {
            dbStatusEl.innerHTML = '<span style="color: var(--text-muted);">Waiting for backend...</span>';
        }
    }
}

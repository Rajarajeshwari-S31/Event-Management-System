// Authentication & Session Helper Utilities

const API_BASE = "";

function getCurrentUser() {
    const raw = localStorage.getItem("currentUser");
    if (!raw) return null;
    try {
        return JSON.parse(raw);
    } catch (e) {
        localStorage.removeItem("currentUser");
        return null;
    }
}

function setCurrentUser(user) {
    localStorage.setItem("currentUser", JSON.stringify(user));
}

function logout() {
    localStorage.removeItem("currentUser");
    window.location.href = "login.html";
}

function requireAuth(requiredRole = null) {
    const user = getCurrentUser();
    if (!user) {
        window.location.href = "login.html";
        return null;
    }
    if (requiredRole && user.role !== requiredRole) {
        alert("Access Denied: You do not have permissions for this page (" + requiredRole + " required).");
        window.location.href = "index.html";
        return null;
    }
    return user;
}

function showAlert(containerId, message, type = "success") {
    const container = document.getElementById(containerId);
    if (!container) return;
    container.innerHTML = `
        <div class="alert alert-${type} alert-dismissible fade show shadow-sm" role="alert">
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    `;
}

function formatDate(dateStr) {
    if (!dateStr) return "TBA";
    try {
        const parts = dateStr.split("-");
        if (parts.length === 3) {
            const d = new Date(parts[0], parts[1] - 1, parts[2]);
            return d.toLocaleDateString(undefined, { year: 'numeric', month: 'short', day: 'numeric' });
        }
        return dateStr;
    } catch (e) {
        return dateStr;
    }
}

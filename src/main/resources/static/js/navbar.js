// Dynamic Navbar Component
document.addEventListener("DOMContentLoaded", function () {
    const navbarContainer = document.getElementById("navbar-placeholder");
    if (!navbarContainer) return;

    const user = getCurrentUser();
    let authNavItems = "";

    if (user) {
        let adminLinks = "";
        let userLinks = "";

        if (user.role === "ADMIN") {
            adminLinks = `
                <li class="nav-item">
                    <a class="nav-link text-warning fw-semibold" href="admin-dashboard.html">
                        <i class="bi bi-speedometer2 me-1"></i>Admin Dashboard
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link text-white" href="add-event.html">
                        <i class="bi bi-plus-circle me-1"></i>Add Event
                    </a>
                </li>
            `;
        } else {
            userLinks = `
                <li class="nav-item">
                    <a class="nav-link text-white" href="my-registrations.html">
                        <i class="bi bi-ticket-perforated me-1"></i>My Registrations
                    </a>
                </li>
            `;
        }

        authNavItems = `
            ${userLinks}
            ${adminLinks}
            <li class="nav-item dropdown ms-lg-2">
                <a class="nav-link dropdown-toggle btn btn-sm btn-outline-light px-3 text-start" href="#" id="userDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                    <i class="bi bi-person-circle me-1"></i> ${user.name} <span class="badge ${user.role === 'ADMIN' ? 'bg-warning text-dark' : 'bg-primary'} ms-1">${user.role}</span>
                </a>
                <ul class="dropdown-menu dropdown-menu-end shadow" aria-labelledby="userDropdown">
                    <li><span class="dropdown-item-text small text-muted"><i class="bi bi-envelope me-1"></i>${user.email}</span></li>
                    <li><hr class="dropdown-divider"></li>
                    ${user.role === 'ADMIN' ? '<li><a class="dropdown-item" href="admin-dashboard.html"><i class="bi bi-speedometer2 me-2"></i>Admin Dashboard</a></li>' : '<li><a class="dropdown-item" href="my-registrations.html"><i class="bi bi-ticket-detailed me-2"></i>My Registrations</a></li>'}
                    <li><a class="dropdown-item text-danger" href="javascript:void(0)" onclick="logout()"><i class="bi bi-box-arrow-right me-2"></i>Logout</a></li>
                </ul>
            </li>
        `;
    } else {
        authNavItems = `
            <li class="nav-item me-2">
                <a class="nav-link btn btn-sm btn-outline-light px-3" href="login.html">Login</a>
            </li>
            <li class="nav-item">
                <a class="btn btn-sm btn-primary px-3" href="register.html">Sign Up</a>
            </li>
        `;
    }

    navbarContainer.innerHTML = `
        <nav class="navbar navbar-expand-lg navbar-dark navbar-custom py-3">
            <div class="container">
                <a class="navbar-brand d-flex align-items-center" href="index.html">
                    <i class="bi bi-calendar-event-fill text-primary me-2 fs-3"></i>
                    <span>Event<span class="text-primary">Pulse</span></span>
                </a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarNav">
                    <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                        <li class="nav-item">
                            <a class="nav-link" href="index.html">Home</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="events.html">Browse Events</a>
                        </li>
                    </ul>
                    <ul class="navbar-nav align-items-lg-center">
                        ${authNavItems}
                    </ul>
                </div>
            </div>
        </nav>
    `;
});

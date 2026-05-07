// Global App Config
const API_BASE = '/api';

// Utility functions
const formatCurrency = (amount) => {
    return new Intl.NumberFormat('en-IN', {
        style: 'currency',
        currency: 'INR',
        maximumFractionDigits: 0
    }).format(amount);
};

// Check authentication status
const checkAuth = () => {
    const token = localStorage.getItem('token');
    const user = JSON.parse(localStorage.getItem('user'));
    
    if (token && user) {
        document.getElementById('loginBtn').style.display = 'none';
        document.getElementById('registerBtn').style.display = 'none';
        document.getElementById('userMenu').style.display = 'block';
        
        document.getElementById('userNameNav').innerText = user.name.split(' ')[0];
        document.getElementById('userAvatarText').innerText = user.name.charAt(0).toUpperCase();
        
        // Update dashboard link based on role
        const dashLink = document.querySelector('.dropdown-item[href="dashboard.html"]');
        if (dashLink && user.role === 'ADMIN') dashLink.href = 'admin.html';
        if (dashLink && user.role === 'PROVIDER') dashLink.href = 'provider.html';
        
        return user;
    }
    return null;
};

// Auth Header for API requests
const getAuthHeaders = () => {
    const token = localStorage.getItem('token');
    return {
        'Content-Type': 'application/json',
        ...(token ? { 'Authorization': `Bearer ${token}` } : {})
    };
};

// Logout
const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    window.location.href = 'index.html';
};

// Initialize common UI elements
document.addEventListener('DOMContentLoaded', () => {
    const user = checkAuth();
    
    // Navbar scroll effect
    const navbar = document.getElementById('navbar');
    if (navbar) {
        window.addEventListener('scroll', () => {
            if (window.scrollY > 50) {
                navbar.style.background = 'rgba(255, 255, 255, 0.95)';
                navbar.style.boxShadow = '0 4px 6px -1px rgba(0, 0, 0, 0.1)';
            } else {
                navbar.style.background = 'rgba(255, 255, 255, 0.8)';
                navbar.style.boxShadow = 'none';
            }
        });
    }

    // User dropdown toggle
    const userAvatarBtn = document.getElementById('userAvatarBtn');
    const dropdownMenu = document.getElementById('dropdownMenu');
    
    if (userAvatarBtn && dropdownMenu) {
        userAvatarBtn.addEventListener('click', (e) => {
            e.stopPropagation();
            dropdownMenu.classList.toggle('show');
        });
        
        document.addEventListener('click', (e) => {
            if (!dropdownMenu.contains(e.target) && !userAvatarBtn.contains(e.target)) {
                dropdownMenu.classList.remove('show');
            }
        });
    }

    const logoutBtn = document.getElementById('logoutBtn');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', logout);
    }
});

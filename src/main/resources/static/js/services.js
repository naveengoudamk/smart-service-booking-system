let allServices = [];

document.addEventListener('DOMContentLoaded', () => {
    // Check url params for category or search
    const urlParams = new URLSearchParams(window.location.search);
    const category = urlParams.get('cat');
    const query = urlParams.get('q');
    
    if (category) {
        updateActiveFilter(category);
        fetchServicesByCategory(category);
    } else if (query) {
        searchServices(query);
    } else {
        fetchAllServices();
    }
    
    // Set min date for booking to today
    const dateInput = document.getElementById('bookingDate');
    if (dateInput) {
        const today = new Date().toISOString().split('T')[0];
        dateInput.setAttribute('min', today);
    }
});

async function fetchAllServices() {
    try {
        const res = await fetch('/api/services/public');
        const data = await res.json();
        if (data.success) {
            allServices = data.data;
            renderServices(allServices);
        }
    } catch (e) {
        console.error(e);
        document.getElementById('servicesList').innerHTML = '<p class="text-danger">Failed to load services.</p>';
    }
}

async function fetchServicesByCategory(cat) {
    try {
        const res = await fetch(`/api/services/public/category/${cat}`);
        const data = await res.json();
        if (data.success) {
            renderServices(data.data);
        }
    } catch (e) { console.error(e); }
}

async function searchServices(q) {
    try {
        const res = await fetch(`/api/services/public/search?q=${encodeURIComponent(q)}`);
        const data = await res.json();
        if (data.success) {
            renderServices(data.data);
            document.querySelector('.section-subtitle').innerText = `Search results for "${q}"`;
        }
    } catch (e) { console.error(e); }
}

function filterServices(category) {
    updateActiveFilter(category);
    if (category === 'All') {
        renderServices(allServices);
    } else {
        const filtered = allServices.filter(s => s.category === category);
        renderServices(filtered);
    }
}

function updateActiveFilter(category) {
    document.querySelectorAll('.filter-btn').forEach(btn => {
        btn.classList.remove('active');
        if (btn.innerText.includes(category)) btn.classList.add('active');
    });
}

function renderServices(services) {
    const grid = document.getElementById('servicesList');
    grid.innerHTML = '';
    
    if (services.length === 0) {
        grid.innerHTML = '<p>No services found.</p>';
        return;
    }
    
    services.forEach(service => {
        const card = document.createElement('div');
        card.className = 'service-card';
        card.innerHTML = `
            <div class="service-icon">${service.icon || '🛠️'}</div>
            <h3 class="service-title">${service.name}</h3>
            <p class="service-desc">${service.description}</p>
            <div class="service-price">${formatCurrency(service.basePrice)}</div>
            <div class="service-meta">
                <span>⏱️ ${service.durationMinutes} mins</span>
                <span>⭐ ${service.ratingAvg.toFixed(1)}</span>
            </div>
            <button class="btn btn-primary" onclick="openBookingModal(${service.id}, '${service.name.replace(/'/g, "\\'")}')">Book Now</button>
        `;
        grid.appendChild(card);
    });
}

function openBookingModal(serviceId, serviceName) {
    const user = checkAuth();
    if (!user) {
        window.location.href = `login.html?redirect=services.html`;
        return;
    }
    
    document.getElementById('serviceId').value = serviceId;
    document.getElementById('modalTitle').innerText = `Book ${serviceName}`;
    document.getElementById('bookingModal').classList.add('show');
}

function closeBookingModal() {
    document.getElementById('bookingModal').classList.remove('show');
    document.getElementById('bookingForm').reset();
    document.getElementById('bookingError').innerText = '';
}

async function submitBooking(e) {
    e.preventDefault();
    const btn = document.getElementById('confirmBookBtn');
    btn.innerText = 'Booking...';
    btn.disabled = true;
    
    const request = {
        serviceId: document.getElementById('serviceId').value,
        bookingDate: document.getElementById('bookingDate').value,
        bookingTime: document.getElementById('bookingTime').value + ':00',
        address: document.getElementById('address').value,
        notes: document.getElementById('notes').value
    };
    
    try {
        const res = await fetch('/api/bookings', {
            method: 'POST',
            headers: getAuthHeaders(),
            body: JSON.stringify(request)
        });
        
        const data = await res.json();
        if (res.ok && data.success) {
            alert('Booking confirmed successfully!');
            closeBookingModal();
            window.location.href = 'dashboard.html';
        } else {
            document.getElementById('bookingError').innerText = data.message || 'Failed to book service';
        }
    } catch (err) {
        document.getElementById('bookingError').innerText = 'An error occurred. Please try again.';
    } finally {
        btn.innerText = 'Confirm Booking';
        btn.disabled = false;
    }
}

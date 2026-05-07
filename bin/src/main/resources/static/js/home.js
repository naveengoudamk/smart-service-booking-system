document.addEventListener('DOMContentLoaded', () => {
    // Number counter animation
    const counters = document.querySelectorAll('.stat-number');
    const speed = 200;

    counters.forEach(counter => {
        const updateCount = () => {
            const target = +counter.getAttribute('data-target');
            const suffix = counter.getAttribute('data-suffix') || '';
            
            if (!target) {
                counter.innerText = counter.innerText;
                return;
            }
            
            const count = +counter.innerText.replace(/\D/g, '');
            const inc = target / speed;

            if (count < target) {
                counter.innerText = Math.ceil(count + inc) + (target > 1000 ? '+' : '') + suffix;
                setTimeout(updateCount, 1);
            } else {
                counter.innerText = (target > 1000 ? (target/1000).toFixed(0) + 'K+' : target) + suffix;
            }
        };
        updateCount();
    });

    // Fetch popular services
    fetchServices();
});

async function fetchServices() {
    try {
        const response = await fetch('/api/services/public');
        const res = await response.json();
        
        if (res.success) {
            const grid = document.getElementById('categoriesGrid');
            grid.innerHTML = ''; // clear loading
            
            // Just show top 8 for homepage
            const services = res.data.slice(0, 8);
            
            services.forEach(service => {
                const card = document.createElement('div');
                card.className = 'service-card';
                card.onclick = () => window.location.href = `services.html?id=${service.id}`;
                
                card.innerHTML = `
                    <div class="service-icon">${service.icon || '🛠️'}</div>
                    <h3 class="service-title">${service.name}</h3>
                    <p class="service-desc">${service.description}</p>
                    <div class="service-price">${formatCurrency(service.basePrice)}</div>
                    <div class="service-meta">
                        <span>⏱️ ${service.durationMinutes} mins</span>
                        <span>⭐ ${service.ratingAvg.toFixed(1)}</span>
                    </div>
                    <button class="btn btn-outline" onclick="event.stopPropagation(); window.location.href='services.html?id=${service.id}'">Book Now</button>
                `;
                grid.appendChild(card);
            });
        }
    } catch (error) {
        console.error('Error fetching services:', error);
        document.getElementById('categoriesGrid').innerHTML = '<p class="text-danger">Failed to load services. Please try again later.</p>';
    }
}

function searchServices() {
    const q = document.getElementById('heroSearch').value;
    if (q) {
        window.location.href = `services.html?q=${encodeURIComponent(q)}`;
    }
}

package com.navi.servicebooking.config;

import com.navi.servicebooking.model.Provider;
import com.navi.servicebooking.model.Service;
import com.navi.servicebooking.model.User;
import com.navi.servicebooking.repository.ProviderRepository;
import com.navi.servicebooking.repository.ServiceRepository;
import com.navi.servicebooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.SecureRandom;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ServiceRepository serviceRepository;
    private final ProviderRepository providerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Create Admin
        if (!userRepository.existsByEmail("admin@serveasy.com")) {
            User admin = new User();
            admin.setName("Admin User");
            admin.setEmail("admin@serveasy.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setPhone("+91-9000000001");
            admin.setAddress("ServeEasy HQ, Bangalore");
            admin.setRole(User.Role.ADMIN);
            userRepository.save(admin);
        }

        // Create sample providers
        createProviderIfNotExists("Rajesh Kumar", "rajesh@serveasy.com", "+91-9000000002");
        createProviderIfNotExists("Priya Sharma", "priya@serveasy.com", "+91-9000000003");
        createProviderIfNotExists("Suresh Nair", "suresh@serveasy.com", "+91-9000000004");
        createProviderIfNotExists("Anita Patel", "anita@serveasy.com", "+91-9000000005");

        // Create Services
        if (serviceRepository.count() == 0) {
            createServices();
        }
    }

    private void createProviderIfNotExists(String name, String email, String phone) {
        if (!userRepository.existsByEmail(email)) {
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode("provider123"));
            user.setPhone(phone);
            user.setRole(User.Role.PROVIDER);
            userRepository.save(user);

            Provider provider = new Provider();
            provider.setUser(user);
            provider.setExperienceYears(3);
            provider.setBio("Experienced professional with excellent service track record.");
            provider.setRatingAvg(4.5);
            providerRepository.save(provider);
        }
    }

    private void createServices() {
        List<Service> services = List.of(
            buildService("Home Cleaning", "Complete home deep cleaning service including all rooms, kitchen, and bathrooms.", "Cleaning", new BigDecimal("499"), 180, "🧹"),
            buildService("Electrical Repair", "Fix switches, wiring, fans, lights, and all electrical issues by certified electricians.", "Electrical", new BigDecimal("299"), 60, "⚡"),
            buildService("Plumbing Service", "Fix leaks, clogs, pipe repairs, tap installation and all plumbing problems.", "Plumbing", new BigDecimal("349"), 90, "🔧"),
            buildService("AC Servicing", "Complete AC service including cleaning, gas refill check, and performance tuning.", "Appliances", new BigDecimal("799"), 120, "❄️"),
            buildService("Painting Service", "Interior and exterior wall painting with premium quality paints.", "Painting", new BigDecimal("1999"), 480, "🎨"),
            buildService("Pest Control", "Comprehensive pest control for cockroaches, termites, mosquitoes and more.", "Pest Control", new BigDecimal("599"), 120, "🐜"),
            buildService("Carpentry Work", "Furniture repair, assembly, custom woodwork and carpentry solutions.", "Carpentry", new BigDecimal("449"), 120, "🪚"),
            buildService("Sofa Cleaning", "Professional sofa and upholstery deep cleaning with foam extraction.", "Cleaning", new BigDecimal("399"), 90, "🛋️"),
            buildService("Water Purifier Service", "RO/UV water purifier cleaning, filter replacement and repair.", "Appliances", new BigDecimal("499"), 60, "💧"),
            buildService("Bathroom Cleaning", "Deep bathroom cleaning with descaling, disinfection and sanitization.", "Cleaning", new BigDecimal("299"), 60, "🚿"),
            buildService("Kitchen Chimney Repair", "Chimney cleaning, motor repair and filter replacement.", "Appliances", new BigDecimal("599"), 90, "🍳"),
            buildService("TV Wall Mount", "Professional TV wall mounting with cable management.", "Electrical", new BigDecimal("249"), 45, "📺")
        );
        serviceRepository.saveAll(services);
    }

    private Service buildService(String name, String desc, String category,
                                  BigDecimal price, int duration, String icon) {
        Service s = new Service();
        s.setName(name);
        s.setDescription(desc);
        s.setCategory(category);
        s.setBasePrice(price);
        s.setDurationMinutes(duration);
        s.setIcon(icon);
        s.setRatingAvg(4.0 + Math.random() * 1.0);
        s.setTotalBookings((int)(Math.random() * 500 + 50));
        return s;
    }
}

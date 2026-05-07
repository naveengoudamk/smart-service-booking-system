package com.navi.servicebooking.service;

import com.navi.servicebooking.model.User;

import com.navi.servicebooking.repository.BookingRepository;
import com.navi.servicebooking.repository.ServiceRepository;
import com.navi.servicebooking.repository.UserRepository;
import com.navi.servicebooking.repository.ProviderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final ServiceRepository serviceRepository;
    private final BookingRepository bookingRepository;
    private final ProviderRepository providerRepository;

    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userRepository.countByRole(User.Role.USER));
        stats.put("totalProviders", userRepository.countByRole(User.Role.PROVIDER));
        stats.put("totalServices", serviceRepository.count());
        stats.put("totalBookings", bookingRepository.count());
        stats.put("pendingBookings", bookingRepository.countByStatus(com.navi.servicebooking.model.Booking.BookingStatus.PENDING));
        stats.put("completedBookings", bookingRepository.countCompleted());
        BigDecimal revenue = bookingRepository.sumRevenue();
        stats.put("totalRevenue", revenue != null ? revenue : BigDecimal.ZERO);
        return stats;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void toggleUserStatus(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setActive(!user.isActive());
        userRepository.save(user);
    }
}

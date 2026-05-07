package com.navi.servicebooking.service;

import com.navi.servicebooking.dto.BookingRequest;

import com.navi.servicebooking.model.*;
import com.navi.servicebooking.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ServiceRepository serviceRepository;
    private final ProviderRepository providerRepository;

    @Transactional
    public Booking createBooking(BookingRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        com.navi.servicebooking.model.Service service = serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new RuntimeException("Service not found"));

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setService(service);
        booking.setBookingDate(request.getBookingDate());
        booking.setBookingTime(request.getBookingTime());
        booking.setAddress(request.getAddress());
        booking.setNotes(request.getNotes());
        booking.setTotalAmount(service.getBasePrice());

        // Calculate end time
        if (service.getDurationMinutes() != null) {
            booking.setEndTime(request.getBookingTime().plusMinutes(service.getDurationMinutes()));
        } else {
            booking.setEndTime(request.getBookingTime().plusHours(1));
        }

        // Assign provider if specified
        if (request.getProviderId() != null) {
            Provider provider = providerRepository.findById(request.getProviderId())
                    .orElseThrow(() -> new RuntimeException("Provider not found"));

            // Check time slot conflict
            List<Booking> conflicts = bookingRepository.findConflictingBookings(
                    provider.getId(), request.getBookingDate(),
                    request.getBookingTime(), booking.getEndTime());

            if (!conflicts.isEmpty()) {
                throw new RuntimeException("Time slot not available. Please choose another time.");
            }

            booking.setProvider(provider);
        } else {
            // Auto-assign available provider for the service
            List<Provider> available = providerRepository.findAvailableProvidersByServiceId(service.getId());
            for (Provider p : available) {
                List<Booking> conflicts = bookingRepository.findConflictingBookings(
                        p.getId(), request.getBookingDate(),
                        request.getBookingTime(), booking.getEndTime());
                if (conflicts.isEmpty()) {
                    booking.setProvider(p);
                    break;
                }
            }
        }

        return bookingRepository.save(booking);
    }

    public List<Booking> getUserBookings(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return bookingRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
    }

    public List<Booking> getProviderBookings(String providerEmail) {
        User user = userRepository.findByEmail(providerEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Provider provider = providerRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Provider not found"));
        return bookingRepository.findByProviderIdOrderByCreatedAtDesc(provider.getId());
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAllByOrderByCreatedAtDesc();
    }

    @Transactional
    public Booking updateBookingStatus(Long bookingId, Booking.BookingStatus status) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setStatus(status);
        return bookingRepository.save(booking);
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }
}

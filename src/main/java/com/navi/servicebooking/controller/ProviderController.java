package com.navi.servicebooking.controller;

import com.navi.servicebooking.dto.ApiResponse;

import com.navi.servicebooking.model.Booking;
import com.navi.servicebooking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/provider")
@RequiredArgsConstructor
public class ProviderController {

    private final BookingService bookingService;

    @GetMapping("/bookings")
    @PreAuthorize("hasAnyRole('PROVIDER','ADMIN')")
    public ResponseEntity<ApiResponse<List<Booking>>> getMyJobs(Authentication authentication) {
        List<Booking> bookings = bookingService.getProviderBookings(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Jobs fetched", bookings));
    }

    @PatchMapping("/bookings/{id}/accept")
    @PreAuthorize("hasAnyRole('PROVIDER','ADMIN')")
    public ResponseEntity<ApiResponse<Booking>> acceptBooking(@PathVariable Long id) {
        Booking booking = bookingService.updateBookingStatus(id, Booking.BookingStatus.CONFIRMED);
        return ResponseEntity.ok(ApiResponse.success("Booking accepted", booking));
    }

    @PatchMapping("/bookings/{id}/reject")
    @PreAuthorize("hasAnyRole('PROVIDER','ADMIN')")
    public ResponseEntity<ApiResponse<Booking>> rejectBooking(@PathVariable Long id) {
        Booking booking = bookingService.updateBookingStatus(id, Booking.BookingStatus.REJECTED);
        return ResponseEntity.ok(ApiResponse.success("Booking rejected", booking));
    }

    @PatchMapping("/bookings/{id}/start")
    @PreAuthorize("hasAnyRole('PROVIDER','ADMIN')")
    public ResponseEntity<ApiResponse<Booking>> startJob(@PathVariable Long id) {
        Booking booking = bookingService.updateBookingStatus(id, Booking.BookingStatus.IN_PROGRESS);
        return ResponseEntity.ok(ApiResponse.success("Job started", booking));
    }

    @PatchMapping("/bookings/{id}/complete")
    @PreAuthorize("hasAnyRole('PROVIDER','ADMIN')")
    public ResponseEntity<ApiResponse<Booking>> completeJob(@PathVariable Long id) {
        Booking booking = bookingService.updateBookingStatus(id, Booking.BookingStatus.COMPLETED);
        return ResponseEntity.ok(ApiResponse.success("Job completed", booking));
    }
}

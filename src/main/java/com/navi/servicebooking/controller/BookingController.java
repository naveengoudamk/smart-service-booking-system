package com.navi.servicebooking.controller;

import com.navi.servicebooking.dto.ApiResponse;

import com.navi.servicebooking.dto.BookingRequest;
import com.navi.servicebooking.model.Booking;
import com.navi.servicebooking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<Booking>> createBooking(
            @RequestBody BookingRequest request,
            Authentication authentication) {
        try {
            Booking booking = bookingService.createBooking(request, authentication.getName());
            return ResponseEntity.ok(ApiResponse.success("Booking created successfully!", booking));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<List<Booking>>> getMyBookings(Authentication authentication) {
        List<Booking> bookings = bookingService.getUserBookings(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Bookings fetched", bookings));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Booking>> getBooking(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(ApiResponse.success("Booking found", bookingService.getBookingById(id)));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<Booking>> cancelBooking(@PathVariable Long id) {
        try {
            Booking booking = bookingService.updateBookingStatus(id, Booking.BookingStatus.CANCELLED);
            return ResponseEntity.ok(ApiResponse.success("Booking cancelled", booking));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}

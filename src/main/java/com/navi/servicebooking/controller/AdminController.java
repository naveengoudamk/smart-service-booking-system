package com.navi.servicebooking.controller;

import com.navi.servicebooking.dto.ApiResponse;

import com.navi.servicebooking.model.Booking;
import com.navi.servicebooking.model.Service;
import com.navi.servicebooking.model.User;
import com.navi.servicebooking.service.AdminService;
import com.navi.servicebooking.service.BookingService;
import com.navi.servicebooking.service.ServiceCatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final BookingService bookingService;
    private final ServiceCatalogService serviceCatalogService;

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboard() {
        return ResponseEntity.ok(ApiResponse.success("Dashboard stats", adminService.getDashboardStats()));
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        return ResponseEntity.ok(ApiResponse.success("Users fetched", adminService.getAllUsers()));
    }

    @PatchMapping("/users/{id}/toggle")
    public ResponseEntity<ApiResponse<Void>> toggleUser(@PathVariable Long id) {
        adminService.toggleUserStatus(id);
        return ResponseEntity.ok(ApiResponse.success("User status toggled", null));
    }

    @GetMapping("/bookings")
    public ResponseEntity<ApiResponse<List<Booking>>> getAllBookings() {
        return ResponseEntity.ok(ApiResponse.success("All bookings", bookingService.getAllBookings()));
    }

    @PatchMapping("/bookings/{id}/status")
    public ResponseEntity<ApiResponse<Booking>> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        try {
            Booking.BookingStatus bookingStatus = Booking.BookingStatus.valueOf(status.toUpperCase());
            Booking booking = bookingService.updateBookingStatus(id, bookingStatus);
            return ResponseEntity.ok(ApiResponse.success("Status updated", booking));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/services")
    public ResponseEntity<ApiResponse<List<Service>>> getAllServices() {
        return ResponseEntity.ok(ApiResponse.success("All services", serviceCatalogService.getAllServices()));
    }
}

package com.navi.servicebooking.repository;

import com.navi.servicebooking.model.Booking;
import com.navi.servicebooking.model.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<Booking> findByProviderIdOrderByCreatedAtDesc(Long providerId);

    List<Booking> findByStatusOrderByCreatedAtDesc(Booking.BookingStatus status);

    List<Booking> findAllByOrderByCreatedAtDesc();

    // Check for time slot conflicts for a provider
    @Query("SELECT b FROM Booking b WHERE b.provider.id = :providerId " +
           "AND b.bookingDate = :date " +
           "AND b.status NOT IN ('CANCELLED', 'REJECTED') " +
           "AND ((b.bookingTime <= :startTime AND b.endTime > :startTime) " +
           "  OR (b.bookingTime < :endTime AND b.endTime >= :endTime) " +
           "  OR (b.bookingTime >= :startTime AND b.endTime <= :endTime))")
    List<Booking> findConflictingBookings(Long providerId, LocalDate date, LocalTime startTime, LocalTime endTime);

    long countByStatus(Booking.BookingStatus status);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.status = 'COMPLETED'")
    long countCompleted();

    @Query("SELECT SUM(b.totalAmount) FROM Booking b WHERE b.status = 'COMPLETED'")
    java.math.BigDecimal sumRevenue();
}

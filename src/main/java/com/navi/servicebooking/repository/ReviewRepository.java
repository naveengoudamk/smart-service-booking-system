package com.navi.servicebooking.repository;

import com.navi.servicebooking.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByBookingId(Long bookingId);
    List<Review> findByProviderId(Long providerId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.provider.id = :providerId")
    Double findAverageRatingByProviderId(Long providerId);
}

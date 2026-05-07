package com.navi.servicebooking.repository;

import com.navi.servicebooking.model.Provider;
import com.navi.servicebooking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Long> {
    Optional<Provider> findByUser(User user);
    Optional<Provider> findByUserId(Long userId);
    List<Provider> findByAvailableTrue();
    List<Provider> findByStatus(Provider.ProviderStatus status);

    @Query("SELECT p FROM Provider p JOIN p.services s WHERE s.id = :serviceId AND p.available = true")
    List<Provider> findAvailableProvidersByServiceId(Long serviceId);
}

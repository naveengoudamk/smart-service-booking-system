package com.navi.servicebooking.repository;

import com.navi.servicebooking.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    List<Service> findByActiveTrue();
    List<Service> findByCategoryAndActiveTrue(String category);
    List<Service> findByNameContainingIgnoreCaseAndActiveTrue(String name);
}

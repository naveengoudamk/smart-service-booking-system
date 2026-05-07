package com.navi.servicebooking.service;

import com.navi.servicebooking.model.Service;

import com.navi.servicebooking.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceCatalogService {

    private final ServiceRepository serviceRepository;

    public List<Service> getAllActiveServices() {
        return serviceRepository.findByActiveTrue();
    }

    public List<Service> getServicesByCategory(String category) {
        return serviceRepository.findByCategoryAndActiveTrue(category);
    }

    public List<Service> searchServices(String query) {
        return serviceRepository.findByNameContainingIgnoreCaseAndActiveTrue(query);
    }

    public Service getServiceById(Long id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found: " + id));
    }

    @Transactional
    public Service createService(Service service) {
        return serviceRepository.save(service);
    }

    @Transactional
    public Service updateService(Long id, Service updated) {
        Service service = getServiceById(id);
        service.setName(updated.getName());
        service.setDescription(updated.getDescription());
        service.setCategory(updated.getCategory());
        service.setBasePrice(updated.getBasePrice());
        service.setDurationMinutes(updated.getDurationMinutes());
        service.setImageUrl(updated.getImageUrl());
        service.setIcon(updated.getIcon());
        return serviceRepository.save(service);
    }

    @Transactional
    public void deleteService(Long id) {
        Service service = getServiceById(id);
        service.setActive(false);
        serviceRepository.save(service);
    }

    public List<Service> getAllServices() {
        return serviceRepository.findAll();
    }
}

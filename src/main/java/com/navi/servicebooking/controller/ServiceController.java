package com.navi.servicebooking.controller;

import com.navi.servicebooking.dto.ApiResponse;

import com.navi.servicebooking.model.Service;
import com.navi.servicebooking.service.ServiceCatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class ServiceController {

    private final ServiceCatalogService serviceCatalogService;

    @GetMapping("/public")
    public ResponseEntity<ApiResponse<List<Service>>> getActiveServices() {
        return ResponseEntity.ok(ApiResponse.success("Services fetched", serviceCatalogService.getAllActiveServices()));
    }

    @GetMapping("/public/category/{category}")
    public ResponseEntity<ApiResponse<List<Service>>> getByCategory(@PathVariable String category) {
        return ResponseEntity.ok(ApiResponse.success("Services by category", serviceCatalogService.getServicesByCategory(category)));
    }

    @GetMapping("/public/search")
    public ResponseEntity<ApiResponse<List<Service>>> search(@RequestParam String q) {
        return ResponseEntity.ok(ApiResponse.success("Search results", serviceCatalogService.searchServices(q)));
    }

    @GetMapping("/public/{id}")
    public ResponseEntity<ApiResponse<Service>> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(ApiResponse.success("Service found", serviceCatalogService.getServiceById(id)));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Service>> create(@RequestBody Service service) {
        return ResponseEntity.ok(ApiResponse.success("Service created", serviceCatalogService.createService(service)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Service>> update(@PathVariable Long id, @RequestBody Service service) {
        try {
            return ResponseEntity.ok(ApiResponse.success("Service updated", serviceCatalogService.updateService(id, service)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        serviceCatalogService.deleteService(id);
        return ResponseEntity.ok(ApiResponse.success("Service deleted", null));
    }
}

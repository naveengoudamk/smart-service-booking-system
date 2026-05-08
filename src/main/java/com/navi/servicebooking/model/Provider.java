package com.navi.servicebooking.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "providers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Provider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "experience_years")
    private Integer experienceYears;

    private String bio;

    @Column(name = "rating_avg")
    private Double ratingAvg = 0.0;

    @Column(name = "total_jobs")
    private Integer totalJobs = 0;

    @Column(name = "is_available")
    private boolean available = true;

    @Enumerated(EnumType.STRING)
    private ProviderStatus status = ProviderStatus.ACTIVE;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "provider_services",
        joinColumns = @JoinColumn(name = "provider_id"),
        inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    private List<Service> services = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "provider", cascade = CascadeType.ALL)
    private List<Booking> bookings = new ArrayList<>();

    public enum ProviderStatus {
        ACTIVE, INACTIVE, SUSPENDED
    }
}

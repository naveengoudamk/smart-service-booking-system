package com.navi.servicebooking.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "services")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private String category;

    @Column(name = "base_price", nullable = false)
    private BigDecimal basePrice;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Column(name = "image_url")
    private String imageUrl;

    private String icon;

    private boolean active = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "rating_avg")
    private Double ratingAvg = 0.0;

    @Column(name = "total_bookings")
    private Integer totalBookings = 0;

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL)
    private List<Booking> bookings = new ArrayList<>();

    @ManyToMany(mappedBy = "services")
    private List<Provider> providers = new ArrayList<>();
}

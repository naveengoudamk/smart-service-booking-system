package com.navi.servicebooking.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class BookingRequest {
    private Long serviceId;
    private Long providerId;
    private LocalDate bookingDate;
    private LocalTime bookingTime;
    private String address;
    private String notes;
}

package com.mandeep.billing_service;

import org.springframework.stereotype.Component;

@Component
public class ReservationFallback implements ReservationClient {
    @Override
    public ReservationDTO getReservationById(Long id) {
        ReservationDTO fallback = new ReservationDTO();
        fallback.setRoomNumber("Unavailable");
        fallback.setRoomType("Unavailable");
        fallback.setCheckInDate(null);
        fallback.setCheckOutDate(null);
        return fallback;
    }
}

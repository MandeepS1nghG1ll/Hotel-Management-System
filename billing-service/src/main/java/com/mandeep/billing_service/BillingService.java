package com.mandeep.billing_service;

import java.time.LocalDate;

import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;

@Service
public class BillingService {
	
	private final BillingRepository billingRepository;
    private final ReservationClient reservationClient;
    private final RoomClient roomClient;

    @Autowired
    public BillingService(BillingRepository billingRepository, ReservationClient reservationClient, RoomClient roomClient) {
        this.billingRepository = billingRepository;
        this.reservationClient = reservationClient;
        this.roomClient = roomClient;
    }

    public Billing generateBill(Long reservationId, Double amountPerDay) {
        ReservationDTO reservation = reservationClient.getReservationById(reservationId);

        if (reservation == null) {
            throw new RuntimeException("Reservation not found for ID: " + reservationId);
        }

        long daysStayed = ChronoUnit.DAYS.between(reservation.getCheckInDate(), reservation.getCheckOutDate());

        double totalAmount = daysStayed * amountPerDay;

        Billing billing = new Billing();
        billing.setReservationId(reservationId);
        billing.setRoomNumber(reservation.getRoomNumber());
        billing.setAmount(totalAmount);
        billing.setCheckInDate(reservation.getCheckInDate());
        billing.setCheckOutDate(reservation.getCheckOutDate());

        billingRepository.save(billing);

        // Perform manual checkout here
        ReservationDTO guest = reservationClient.getReservationById(reservationId);
        if (guest != null) {
            roomClient.updateRoomAvailability(guest.getRoomNumber(), true);
        }

        return billing;
    }

    public Double getTotalAmount(Long reservationId) {
        Optional<Billing> billing = billingRepository.findByReservationId(reservationId);
        return billing.map(Billing::getAmount).orElseThrow(() -> 
            new RuntimeException("Billing record not found for reservation ID: " + reservationId)
        );
    }

}

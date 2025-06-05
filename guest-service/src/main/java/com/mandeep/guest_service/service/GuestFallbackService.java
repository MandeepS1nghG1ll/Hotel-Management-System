package com.mandeep.guest_service.service;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import dto.BillingClient;
import dto.BillingDTO;
import dto.ReservationClient;
import dto.ReservationDTO;

@Service
public class GuestFallbackService implements ReservationClient, BillingClient{
//	@Override
//    public List<ReservationDTO> getReservationsByGuestId(Long guestId) {
//        System.out.println("Fallback: Reservation service is down");
//        return Collections.emptyList();
//    }
//
//    @Override
//    public List<BillingDTO> getBillingByGuestId(Long guestId) {
//        System.out.println("Fallback: Billing service is down");
//        return Collections.emptyList();
//    }

	@Override
	public Double getTotalAmount(Long reservationId) {
		// TODO Auto-generated method stub
        System.out.println("Fallback: Reservation service is down");

		return null;
	}

	@Override
	public ReservationDTO getReservationById(Long id) {
		// TODO Auto-generated method stub
        System.out.println("Fallback: Reservation service is down");

		return null;
	}

}

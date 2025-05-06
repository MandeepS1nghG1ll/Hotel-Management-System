package com.mandeep.guest_service.controller;

import java.util.List;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mandeep.guest_service.entity.Guest;
import com.mandeep.guest_service.service.GuestService;

import dto.BillingClient;
import dto.BillingDTO;
import dto.GuestDetailsDTO;
import dto.ReservationClient;
import dto.ReservationDTO;

@RestController
@RequestMapping("/guests")
public class GuestController {
    @Autowired
    private GuestService service;
    
    @Autowired
    private ReservationClient reservationClient;

    @Autowired
    private BillingClient billingClient;

    @GetMapping
    public ResponseEntity<List<Guest>> getAllGuests() {
        return service.getAllGuests();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Guest>> getGuestById(@PathVariable Long id) {
        return service.getGuestById(id);
    }

    @PostMapping
    public ResponseEntity<String> addGuest(@RequestBody Guest guest) {
        return service.addGuest(guest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGuest(@PathVariable Long id) {
        return service.deleteGuest(id);
    }
    
    @GetMapping("/details/{id}")
    public ResponseEntity<GuestDetailsDTO> getFullGuestDetails(@PathVariable Long id) {
        Optional<Guest> guestOpt = service.getGuestById(id).getBody();

        if (guestOpt.isPresent()) {
            Guest guest = guestOpt.get();

            ReservationDTO reservation = reservationClient.getReservationById(id);
            Double totalAmount = billingClient.getTotalAmount(reservation.getId()); 

            GuestDetailsDTO details = new GuestDetailsDTO();
            details.setGuest(guest);
            details.setReservation(reservation);
            details.setBillingAmount(totalAmount); 

            return new ResponseEntity<>(details, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    
    
    
}	
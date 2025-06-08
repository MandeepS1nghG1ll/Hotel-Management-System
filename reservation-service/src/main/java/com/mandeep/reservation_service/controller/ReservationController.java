package com.mandeep.reservation_service.controller;



import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mandeep.reservation_service.entity.Reservation;
import com.mandeep.reservation_service.entity.Room;
import com.mandeep.reservation_service.service.ReservationService;

@RestController
@RequestMapping("/res")
public class ReservationController {
	
	@Autowired
    private  ReservationService service;
    
	    
    @GetMapping
    public List<Reservation> getAllReservations() { 
    	return service.getAllReservations(); }
    
    
    @GetMapping("/{id}")
    public Reservation getReservationById(@PathVariable Long id){
    	return service.getReservationByIdOrThrow(id);
    }
    
    
    @PostMapping("/createReservation")
    public Reservation createReservation(@RequestBody Reservation reservation) { 
    	return service.createReservation(reservation); }
	    
    
    
    @DeleteMapping("/{id}")
    public void deleteReservation(@PathVariable Long id) { 
    	service.deleteReservation(id); }
   
    
    
    @PostMapping("/addRoom")
    public Room addRoom(@RequestBody Room room) {
    	return service.addRoomToInventory(room.getRoomNumber(), room.getRoomType());
    }
    
    
    @GetMapping("/availableRooms")
    public List<Room> getAvailableRooms(){
    	return service.getAvailableRooms();
    }
    
    
//    @PutMapping("/checkout/{guestId}")
//    public ResponseEntity<String> checkoutGuest(@PathVariable Long guestId) {
//        service.checkoutGuest(guestId);
//        return ResponseEntity.ok("Guest checked out successfully. Room is now available.");
//    }
    
    
    @PutMapping("/updateRoom/{roomNumber}")
    public ResponseEntity<String> updateRoomAvailability( @PathVariable String roomNumber, @RequestParam boolean available) {
        
        boolean updated = service.updateRoomAvailability(roomNumber, available);
        
        if (updated) {
            return ResponseEntity.ok("Room availability updated.");
        } else {
            return ResponseEntity.status(404).body("Room not found.");
        }
    }

}



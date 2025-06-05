package com.mandeep.billing_service;

import org.springframework.cloud.openfeign.FeignClient;


import org.springframework.web.bind.annotation.*;

@FeignClient(name = "RESERVATION-SERVICE" , contextId = "roomClient", fallback = RoomFallback.class)

public interface RoomClient {
	@PutMapping("/res/updateRoom/{roomNumber}")
    void updateRoomAvailability(@PathVariable String roomNumber, @RequestParam boolean available);

}

package com.mandeep.billing_service;

import org.springframework.stereotype.Component;

@Component
public class RoomFallback implements RoomClient {
    @Override
    public void updateRoomAvailability(String roomNumber, boolean available) {
        System.out.println("Fallback: Room update failed for " + roomNumber);
    }
}

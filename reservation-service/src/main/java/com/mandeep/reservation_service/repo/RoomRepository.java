package com.mandeep.reservation_service.repo;


import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mandeep.reservation_service.entity.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Room findByRoomNumber(String roomNumber);
    List<Room> findByAvailable(boolean available);
}
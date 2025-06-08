package com.mandeep.reservation_service.service;

import java.util.List;



import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.mandeep.reservation_service.entity.Reservation;
import com.mandeep.reservation_service.entity.Room;
import com.mandeep.reservation_service.repo.ReservationRepository;
import com.mandeep.reservation_service.repo.RoomRepository;

@Service
public class ReservationService {
	
	@Autowired
    private ReservationRepository repository;
	
	@Autowired
	private RoomRepository roomRepository;
	
    
    public List<Reservation> getAllReservations() { 
    	return repository.findAll(); }
    
    
    public Reservation getReservationByIdOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation not found"));   }
    
    
    public Optional<Reservation> getReservationById(Long id) { 
    	return repository.findById(id);}
    
    
//    @Transactional
//    public Reservation createReservation(Reservation reservation) {
//        Room room = roomRepository.findByRoomNumber(reservation.getRoomNumber());
//
//        if (room != null && room.isAvailable()) {
//            room.setAvailable(false);
//            roomRepository.save(room);
//
//            reservation.setRoomType(room.getRoomType());
//            return repository.save(reservation);
//        } else {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Room not available for booking.");
//        }
//    }
    
    
    public void deleteReservation(Long id) { 
    	repository.deleteById(id); }
    

//    public Room addRoomToInventory(String roomNumber, String roomType) {
//        if (roomRepository.findByRoomNumber(roomNumber) != null) {
//            throw new ResponseStatusException(HttpStatus.CONFLICT, "Room already exists.");
//        }
//        Room room = new Room(roomNumber, roomType, true);
//        return roomRepository.save(room);
//    }
    public Room addRoomToInventory(String roomNumber, String roomType) {
        if (roomRepository.findByRoomNumber(roomNumber) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Room already exists.");
        }

        int roomNum;
        try {
            roomNum = Integer.parseInt(roomNumber);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Room number must be a valid integer.");
        }

        // Apply constraints based on room type
        switch (roomType.toLowerCase()) {
            case "single":
                if (roomNum < 100 || roomNum > 199) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Single room numbers must be between 100 and 199.");
                }
                break;
            case "double":
                if (roomNum < 200 || roomNum > 299) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Double room numbers must be between 200 and 299.");
                }
                break;
            case "suite":
                if (roomNum < 300 || roomNum > 399) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Suite room numbers must be between 300 and 399.");
                }
                break;
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid room type. Must be 'Single', 'Double', or 'Suite'.");
        }

        Room room = new Room(roomNumber, roomType, true);
        return roomRepository.save(room);
    }

    

    public List<Room> getAvailableRooms() {
        return roomRepository.findByAvailable(true); }
	
//	
//    public void checkoutGuest(Long guestId) {
//        Reservation guest = repository.findById(guestId)
//            .orElseThrow(() -> new RuntimeException("Guest not found"));
//
//        // Find the room and mark it available
//        Room room = roomRepository.findByRoomNumber(guest.getRoomNumber());
//        if (room != null) {
//            room.setAvailable(true);
//            roomRepository.save(room);
//        }
//
//    }
    @Transactional
    public Reservation createReservation(Reservation reservation) {
        String roomType = reservation.getRoomType();
        
        if (roomType == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Room type is required.");
        }

        // Find all available rooms of the specified type
        List<Room> availableRooms = roomRepository.findByAvailableAndRoomType(true, roomType);

        // Filter by room number range based on type
        Room assignedRoom = availableRooms.stream()
            .filter(room -> {
                int roomNum = Integer.parseInt(room.getRoomNumber());
                return switch (roomType.toLowerCase()) {
                    case "single" -> roomNum >= 100 && roomNum < 200;
                    case "double" -> roomNum >= 200 && roomNum < 300;
                    case "suite"  -> roomNum >= 300 && roomNum < 400;
                    default       -> false;
                };
            })
            .findFirst()
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No available rooms of type: " + roomType));

        // Mark the room as unavailable
        assignedRoom.setAvailable(false);
        roomRepository.save(assignedRoom);

        // Set roomNumber and save reservation
        reservation.setRoomNumber(assignedRoom.getRoomNumber());
        return repository.save(reservation);
    }



	public boolean updateRoomAvailability(String roomNumber, boolean available) {
		 Optional<Room> optionalRoom = Optional.of(roomRepository.findByRoomNumber(roomNumber));
		    
		    if (optionalRoom.isPresent()) {
		        Room room = optionalRoom.get();
		        room.setAvailable(available);
		        roomRepository.save(room);
		        return true;
		    } else {
		        return false;
		    }
	}
    
    

}

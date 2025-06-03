package com.mandeep.guest_service.service;

import java.util.ArrayList;

import java.util.List;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mandeep.guest_service.entity.Guest;
import com.mandeep.guest_service.repo.GuestRepository;

@Service
public class GuestService {
	
	    @Autowired
	    private GuestRepository repository;
	    
	    

	    public ResponseEntity<List<Guest>> getAllGuests() {
	        try {
	        	return new ResponseEntity<>(repository.findAll(), HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
			}
	        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
	    }

	    
	    
	    public ResponseEntity<Optional<Guest>> getGuestById(Long id) {
	    	try {
	    		return new ResponseEntity<>(repository.findById(id), HttpStatus.OK);	
			} catch (Exception e) {
				e.printStackTrace();
			}
	    	return new ResponseEntity<>(Optional.empty(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }

	    
	    
	    public ResponseEntity<String> addGuest(Guest guest) {
	        try {
	        	repository.save(guest);
		    	return new ResponseEntity<>("Created records successfully",HttpStatus.CREATED);
			} catch (Exception e) {
				e.printStackTrace();
			}
	        return new ResponseEntity<>("Failed",HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	    
	    
	    

	    public ResponseEntity<String> deleteGuest(Long id) {
	    	try {
	            if (repository.existsById(id)) {
	                repository.deleteById(id);
	                return new ResponseEntity<>("Guest deleted successfully!", HttpStatus.OK);
	            } else {
	                return new ResponseEntity<>("Guest not found!", HttpStatus.NOT_FOUND);
	            }
	        } catch (Exception e) {
	            e.printStackTrace(); 
	            return new ResponseEntity<>("Failed to delete guest!", HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }

}

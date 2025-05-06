package com.mandeep.guest_service.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mandeep.guest_service.entity.Guest;

public interface GuestRepository extends JpaRepository<Guest, Long> {

}

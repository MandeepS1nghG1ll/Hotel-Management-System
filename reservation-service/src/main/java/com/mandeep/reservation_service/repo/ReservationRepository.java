package com.mandeep.reservation_service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mandeep.reservation_service.entity.Reservation;
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

}

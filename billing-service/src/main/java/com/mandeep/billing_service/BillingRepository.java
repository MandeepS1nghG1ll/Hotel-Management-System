package com.mandeep.billing_service;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BillingRepository extends JpaRepository<Billing, Long>{
	Optional<Billing> findByReservationId(Long reservationId);

}

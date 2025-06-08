package com.mandeep.billing_service;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/billing")
public class BillingController {
	private final BillingService billingService;

    @Autowired
    public BillingController(BillingService billingService) {
        this.billingService = billingService;
    }

    @PostMapping("/generate/{reservationId}/{amountPerDay}")
    public ResponseEntity<Billing> generateBill(@PathVariable Long reservationId, @PathVariable Double amountPerDay) {
        
        Billing billing = billingService.generateBill(reservationId, amountPerDay);
        return ResponseEntity.ok(billing);
    }

    @GetMapping("/total/{reservationId}")
    public ResponseEntity<Billing> getTotalAmount(@PathVariable Long reservationId) {
        Billing totalAmount = billingService.getTotalAmount(reservationId);
        return ResponseEntity.ok(totalAmount);
    }

}

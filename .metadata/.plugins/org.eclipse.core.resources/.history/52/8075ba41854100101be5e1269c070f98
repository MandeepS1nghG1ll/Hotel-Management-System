package dto;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "BILLING-SERVICE")
public interface BillingClient {
	@GetMapping("/billing/total/{reservationId}")
    Double getTotalAmount(@PathVariable("reservationId") Long reservationId);

}

package dto;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.cloud.openfeign.FeignClient;


@FeignClient(name = "RESERVATION-SERVICE")
public interface ReservationClient {
	@GetMapping("/res/{id}")
    public ReservationDTO getReservationById(@PathVariable Long id);

}

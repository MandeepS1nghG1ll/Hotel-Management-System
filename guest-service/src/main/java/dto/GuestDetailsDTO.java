package dto;

import com.mandeep.guest_service.entity.Guest;

public class GuestDetailsDTO {
	private Guest guest;
    private ReservationDTO reservation;
    private Double billingAmount;
	public Guest getGuest() {
		return guest;
	}
	public void setGuest(Guest guest) {
		this.guest = guest;
	}
	public ReservationDTO getReservation() {
		return reservation;
	}
	public void setReservation(ReservationDTO reservation) {
		this.reservation = reservation;
	}
	public Double getBillingAmount() {
		return billingAmount;
	}
	public void setBillingAmount(Double billingAmount) {
		this.billingAmount = billingAmount;
	}
	
    

}

package com.mandeep.billing_service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class BillingServiceTest {

    @Mock
    private BillingRepository billingRepository;

    @MockBean
    private ReservationClient reservationClient;

    @MockBean
    private RoomClient roomClient;

    @InjectMocks
    private BillingService billingService;

    private ReservationDTO mockReservation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockReservation = new ReservationDTO();
        mockReservation.setId(10L);
        mockReservation.setGuestId(101L);
        mockReservation.setRoomNumber("A101");
        mockReservation.setRoomType("Deluxe");
        mockReservation.setCheckInDate(LocalDate.of(2024, 6, 1));
        mockReservation.setCheckOutDate(LocalDate.of(2024, 6, 5)); // 4 days
    }

    @Test
    void testGenerateBill_Success() {
        // Given
        when(reservationClient.getReservationById(10L)).thenReturn(mockReservation);
        when(billingRepository.save(any(Billing.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Billing bill = billingService.generateBill(10L, 1000.0);

        // Then
        assertNotNull(bill);
        assertEquals(10L, bill.getReservationId());
        assertEquals("A101", bill.getRoomNumber());
        assertEquals(4000.0, bill.getAmount());
        assertEquals(mockReservation.getCheckInDate(), bill.getCheckInDate());
        assertEquals(mockReservation.getCheckOutDate(), bill.getCheckOutDate());

        verify(roomClient, times(1)).updateRoomAvailability("A101", true);
        verify(billingRepository, times(1)).save(any(Billing.class));
    }

    @Test
    void testGenerateBill_ReservationNotFound() {
        when(reservationClient.getReservationById(2L)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
            billingService.generateBill(2L, 1000.0)
        );

        assertEquals("Reservation not found for ID: 2", exception.getMessage());
    }

//    @Test
//    void testGetTotalAmount_BillingExists() {
//        Billing bill = new Billing();
//        bill.setReservationId(1L);
//        bill.setAmount(2500.0);
//
//        when(billingRepository.findByReservationId(1L)).thenReturn(Optional.of(bill));
//
//        Double result = billingService.getTotalAmount(1L);
//
//        assertEquals(2500.0, result);
//    }

    @Test
    void testGetTotalAmount_BillingNotFound() {
        when(billingRepository.findByReservationId(10L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
            billingService.getTotalAmount(10L)
        );

        assertEquals("Billing record not found for reservation ID: 10", exception.getMessage());
    }
}

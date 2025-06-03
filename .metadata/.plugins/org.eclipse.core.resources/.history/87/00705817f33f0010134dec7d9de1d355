package com.mandeep.guest_service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.mandeep.guest_service.entity.Guest;
import com.mandeep.guest_service.repo.GuestRepository;
import com.mandeep.guest_service.service.GuestService;

public class GuestServiceTest {

    @Mock
    private GuestRepository repository;

    @InjectMocks
    private GuestService service;

    private Guest guest;

    @BeforeEach
    void setUp() {
        openMocks(this);
        guest = new Guest();
        guest.setId(1L);
        guest.setName("John Doe");
        guest.setEmail("john@example.com");
        guest.setPhone("1234567890");
        guest.setAddress("NY");
    }

    @Test
    void testGetAllGuests() {
        when(repository.findAll()).thenReturn(List.of(guest));
        ResponseEntity<List<Guest>> response = service.getAllGuests();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
    }

    @Test
    void testGetGuestByIdFound() {
        when(repository.findById(1L)).thenReturn(Optional.of(guest));
        ResponseEntity<Optional<Guest>> response = service.getGuestById(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isPresent();
    }

    @Test
    void testAddGuest() {
        when(repository.save(any(Guest.class))).thenReturn(guest);
        ResponseEntity<String> response = service.addGuest(guest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).contains("Created");
    }

    @Test
    void testDeleteGuestExists() {
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        ResponseEntity<String> response = service.deleteGuest(1L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("deleted");
    }

    @Test
    void testDeleteGuestNotFound() {
        when(repository.existsById(1L)).thenReturn(false);

        ResponseEntity<String> response = service.deleteGuest(1L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).contains("not found");
    }
}

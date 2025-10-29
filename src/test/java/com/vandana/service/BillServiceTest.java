package com.vandana.service;

import com.vandana.dto.request.BillCreateRequest;
import com.vandana.entity.Bill;
import com.vandana.entity.User;
import com.vandana.exception.NotFoundException;
import com.vandana.repository.BillRepository;
import com.vandana.repository.ProductRepository;
import com.vandana.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BillServiceTest {

    @Mock
    private BillRepository billRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BillCalculationService billCalculationService;

    @InjectMocks
    private BillService billService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateBill_UserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());
        BillCreateRequest req = new BillCreateRequest();

        NotFoundException ex = assertThrows(NotFoundException.class, () -> billService.createBill(req, 1));
        assertEquals("User not found", ex.getMessage());
    }

    // Add full bill creation success, item validation, etc.
}

package co.com.hotel.sensortrigger;

import co.com.hotel.room.RoomUseCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SensorTriggerTest {

    @InjectMocks
    private SensorTrigger sensorTrigger;

    @Mock
    private RoomUseCase roomUseCase;

    @BeforeEach
    void setUp() {
        sensorTrigger = new SensorTrigger(roomUseCase);
    }

    @Test
    void couldApplyReservationSuccessFull() {
        when(roomUseCase.applyReservations()).thenReturn(Mono.empty());
        Assertions.assertNotNull(sensorTrigger.applyReservation());
    }

    @Test
    void couldApplyReservationFailed() {
        when(roomUseCase.applyReservations()).thenReturn(Mono.empty());
        Assertions.assertNotNull(sensorTrigger.applyReservation());
    }

}
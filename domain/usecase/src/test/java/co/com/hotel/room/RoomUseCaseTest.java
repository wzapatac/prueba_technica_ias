package co.com.hotel.room;

import co.com.hotel.room.gateways.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoomUseCaseTest {

    @InjectMocks
    private RoomUseCase roomUseCase;

    @Mock
    private RoomRepository roomRepository;

    private Room room;

    @BeforeEach
    void setUp() {
        room = new Room();
        room.setRoomType("Sencilla");
        room.setRoomState("Disponible");
        room.setNightValue(100000);
        room.setNumberGuests(2);
        room.setHotelId("Hotel_id");
        room.setRoomId("401");
        room.setUserId("User_test_id");
        room.setReservationDate("22/04/2024");
        room.setReservationStartDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        room.setReservationEndDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        roomUseCase = new RoomUseCase(roomRepository);
    }

    @Test
    void couldApplyReservationsSuccessFull() {
        when(roomRepository.findRooms(any(RoomFilter.class), any(String.class)))
                .thenReturn(Mono.just(List.of(room)));
        when(roomRepository.registerRoom(any(Room.class))).thenReturn(Mono.just(room));

        StepVerifier.create(roomUseCase.applyReservations())
                .verifyComplete();
    }

    @Test
    void couldDesApplyReservationsSuccessFull() {
        when(roomRepository.findRooms(any(RoomFilter.class), any(String.class)))
                .thenReturn(Mono.just(List.of(room)));
        when(roomRepository.registerRoom(any(Room.class))).thenReturn(Mono.just(room));

        StepVerifier.create(roomUseCase.desApplyReservations())
                .verifyComplete();
    }

    @Test
    void couldRegisterRoomSuccessFull() {

        when(roomRepository.registerRoom(any(Room.class))).thenReturn(Mono.just(room));

        StepVerifier.create(roomUseCase.registerRoom(room))
                .expectNext(room)
                .verifyComplete();
    }

    @Test
    void couldDeleteRoomSuccessFull() {

        when(roomRepository.findRoomById(any(String.class), any(String.class))).thenReturn(Mono.just(room));
        when(roomRepository.registerRoom(any(Room.class))).thenReturn(Mono.just(room));

        StepVerifier.create(roomUseCase.deleteRoom("hotel_id_test", "room_id_test"))
                .expectNextMatches(roomResponse-> "No disponible".equals(roomResponse.getRoomState()))
                .verifyComplete();
    }

    @Test
    void couldFindRoomsSuccessFull() {
        var roomFilter = new RoomFilter();
        roomFilter.setRoomType("");
        roomFilter.setRoomState("");
        roomFilter.setReservationDate("");
        roomFilter.setNumberGuests(0);

        when(roomRepository.findRooms(any(RoomFilter.class), any(String.class)))
                .thenReturn(Mono.just(List.of(room)));

        StepVerifier.create(roomUseCase.findRooms(roomFilter, "hotel_id_test"))
                .expectNext(List.of(room))
                .verifyComplete();
    }
}
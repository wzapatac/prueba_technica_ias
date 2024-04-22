package co.com.hotel.user;

import co.com.hotel.api.exception.BusinessException;
import co.com.hotel.room.Room;
import co.com.hotel.room.gateways.RoomRepository;
import co.com.hotel.user.gateways.UserRepository;
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

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserUserCaseTest {

    @InjectMocks
    private UserUserCase userUserCase;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoomRepository roomRepository;

    private User user;
    private Room room;

    @BeforeEach
    void setUp(){
        userUserCase = new UserUserCase(userRepository, roomRepository);

        user = new User();
        user.setId("user_id_test");
        user.setDocumentType("cc");
        user.setEndDate("25/04/2024");
        user.setName("Walther test");
        user.setStartDate("22/04/2024");
        user.setIdentification(5555555);
        user.setRoomId("101");

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
    }

    @Test
    void couldRegisterUserSuccessFull(){
        when(roomRepository.findRoomById(any(String.class),any(String.class))).thenReturn(Mono.just(room));
        when(userRepository.registerUser(any(User.class))).thenReturn(Mono.just(user));
        when(roomRepository.registerRoom(any(Room.class))).thenReturn(Mono.just(room));

        StepVerifier.create(userUserCase.registerUser(user))
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    void couldRegisterUserFailFieldsNull(){
        var userRequest = new User();
        userRequest.setRoomId("401");
        when(roomRepository.findRoomById(any(String.class),any(String.class))).thenReturn(Mono.just(room));
        StepVerifier.create(userUserCase.registerUser(userRequest))
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void couldRegisterUserFailFieldNameNumber(){
        user.setName("Walther 25");
        when(roomRepository.findRoomById(any(String.class),any(String.class))).thenReturn(Mono.just(room));
        StepVerifier.create(userUserCase.registerUser(user))
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void couldRegisterUserFailFieldNameLength(){
        user.setName("Walther ffasdfasfa fasdfasd" +
                "fasdfasd fasdfasdfasdf fasdfasdfasd fasdfasdfsd");
        when(roomRepository.findRoomById(any(String.class),any(String.class))).thenReturn(Mono.just(room));
        StepVerifier.create(userUserCase.registerUser(user))
                .expectError(BusinessException.class)
                .verify();
    }
}
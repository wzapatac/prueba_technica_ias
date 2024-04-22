package co.com.hotel.api;

import co.com.hotel.GenericModelMapper;
import co.com.hotel.TechnicalException;
import co.com.hotel.api.config.HotelRoutes;
import co.com.hotel.api.dto.RoomDTO;
import co.com.hotel.api.dto.UserDTO;
import co.com.hotel.api.exception.BusinessException;
import co.com.hotel.api.exception.WebExceptionHandler;
import co.com.hotel.api.exception.message.BusinessExceptionMessage;
import co.com.hotel.api.exception.model.ErrorModel;
import co.com.hotel.api.util.RequestUtil;
import co.com.hotel.api.util.ResponseUtil;
import co.com.hotel.api.util.Util;
import co.com.hotel.message.TechnicalExceptionMessage;
import co.com.hotel.room.Room;
import co.com.hotel.room.RoomFilter;
import co.com.hotel.room.RoomUseCase;
import co.com.hotel.user.User;
import co.com.hotel.user.UserUserCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {
        RouterRestHotel.class,
        HandlerHotel.class,
        RequestUtil.class,
        ResponseUtil.class,
        HotelRoutes.class,
        Util.class,
        WebExceptionHandler.class
})
@WebFluxTest
@ExtendWith(MockitoExtension.class)
class RouterRestHotelTest {
    @Autowired
    private WebTestClient webTestClient;
    @InjectMocks
    private RouterRestHotel routerRestHotel;
    @MockBean
    private UserUserCase userUserCase;
    @MockBean
    private RoomUseCase roomUseCase;
    @MockBean
    private GenericModelMapper genericModelMapper;

    private RoomDTO roomDTO;
    private Room room;
    private RoomFilter roomFilter;
    private User user;
    private UserDTO userDTO;


    @BeforeEach
    void setUp() {

        roomDTO = new RoomDTO();
        roomDTO.setRoomType("Sencilla");
        roomDTO.setRoomState("Disponible");
        roomDTO.setNightValue(100000);
        roomDTO.setNumberGuests(2);

        room = new Room();
        room.setRoomType("Sencilla");
        room.setRoomState("Disponible");
        room.setNightValue(100000);
        room.setNumberGuests(2);

        roomFilter = new RoomFilter();
        roomFilter.setRoomState("");
        roomFilter.setReservationDate("");
        roomFilter.setNumberGuests(0);
        roomFilter.setRoomType("");

        user = new User();
        user.setId("user_id_test");
        user.setDocumentType("cc");
        user.setEndDate("25/04/2024");
        user.setName("Walther test");
        user.setStartDate("22/04/2024");
        user.setIdentification(5555555);
        user.setRoomId("101");

        userDTO = new UserDTO();

        userDTO.setDocumentType("cc");
        userDTO.setEndDate("25/04/2024");
        userDTO.setName("Walther test");
        userDTO.setStartDate("22/04/2024");
        userDTO.setIdentification(5555555);
        userDTO.setRoomId("101");

        when(genericModelMapper.getRoomSinceRoomDTO(any(RoomDTO.class))).thenReturn(room);
        when(genericModelMapper.getRoomDTOSinceRoom(any(Room.class))).thenReturn(roomDTO);
    }

    @Test
    void couldCreateRoomSuccessFull() {

        when(roomUseCase.registerRoom(any(Room.class))).thenReturn(Mono.just(room));

        webTestClient.post().uri("/room").bodyValue(roomDTO)
                .exchange().expectStatus().isCreated().expectBody(RoomDTO.class);
    }

    @Test
    void couldCreateRoomFail() {

        when(roomUseCase.registerRoom(any(Room.class)))
                .thenReturn(Mono.error(new BusinessException(BusinessExceptionMessage.UN_EXPECTED_ERROR)));

        webTestClient.post().uri("/room").bodyValue(roomDTO)
                .exchange().expectStatus().is4xxClientError().expectBody(ErrorModel.class);
    }

    @Test
    void couldDeleteRoomSuccessFull() {
        when(roomUseCase.deleteRoom(any(String.class), any(String.class))).thenReturn(Mono.just(room));

        webTestClient.delete().uri("/room/10")
                .header("hotel-id", "hotel_id_test")
                .exchange().expectStatus().isOk().expectBody(RoomDTO.class);
    }

    @Test
    void couldDeleteRoomFail() {
        when(roomUseCase.deleteRoom(any(String.class), any(String.class)))
                .thenReturn(Mono.error(new TechnicalException(TechnicalExceptionMessage.UN_EXPECTED_ERROR)));

        webTestClient.delete().uri("/room/10")
                .header("hotel-id", "hotel_id_test")
                .exchange().expectStatus().is4xxClientError().expectBody(ErrorModel.class);
    }

    @Test
    void couldListRoomsSuccessFull() {

        when(roomUseCase.findRooms(any(RoomFilter.class), any(String.class))).thenReturn(Mono.just(List.of(room)));

        webTestClient.post().uri("/rooms").bodyValue(roomFilter)
                .header("hotel-id", "hotel_id_test")
                .exchange().expectStatus().isOk().expectBody(RoomDTO.class);
    }

    @Test
    void couldListRoomsFail() {

        when(roomUseCase.findRooms(any(RoomFilter.class), any(String.class)))
                .thenReturn(Mono.error(new TechnicalException(TechnicalExceptionMessage.COULD_NOT_LIST_ALL_ROOMS)));

        webTestClient.post().uri("/rooms").bodyValue(roomFilter)
                .header("hotel-id", "hotel_id_test")
                .exchange().expectStatus().is4xxClientError().expectBody(ErrorModel.class);
    }


    @Test
    void couldCreateUserSuccessFull() {

        when(userUserCase.registerUser(any(User.class))).thenReturn(Mono.just(user));
        when(genericModelMapper.getUserDTOSinceUser(any(User.class))).thenReturn(userDTO);
        when(genericModelMapper.getUserSinceUserDTO(any(UserDTO.class))).thenReturn(user);

        webTestClient.post().uri("/user").bodyValue(userDTO)
                .exchange().expectStatus().isCreated().expectBody(UserDTO.class);
    }

    @Test
    void couldCreateUserFail() {

        when(genericModelMapper.getUserDTOSinceUser(any(User.class))).thenReturn(userDTO);
        when(genericModelMapper.getUserSinceUserDTO(any(UserDTO.class))).thenReturn(user);
        when(userUserCase.registerUser(any(User.class)))
                .thenReturn(Mono.error(new BusinessException(BusinessExceptionMessage.NAME_NULL)));

        webTestClient.post().uri("/user").bodyValue(userDTO)
                .exchange().expectStatus().is4xxClientError().expectBody(ErrorModel.class);
    }
}
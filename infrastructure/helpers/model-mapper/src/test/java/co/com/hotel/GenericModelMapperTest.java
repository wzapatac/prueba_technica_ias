package co.com.hotel;

import co.com.hotel.api.dto.RoomDTO;
import co.com.hotel.api.dto.UserDTO;
import co.com.hotel.dao.RoomDAO;
import co.com.hotel.dao.UserDAO;
import co.com.hotel.room.Room;
import co.com.hotel.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GenericModelMapperTest {

    @InjectMocks
    private GenericModelMapper genericModelMapper;
    @Mock
    private ModelMapper modelMapper;

    private User user;
    private UserDTO userDTO;
    private UserDAO userDAO;

    private Room room;

    private RoomDTO roomDTO;

    private RoomDAO roomDAO;

    @BeforeEach
    void setUp() {
        genericModelMapper = new GenericModelMapper(modelMapper);

        user = new User();
        user.setId("user_id_test");
        user.setDocumentType("cc");
        user.setEndDate("25/04/2024");
        user.setName("Walther test");
        user.setStartDate("22/04/2024");
        user.setIdentification(5555555);
        user.setRoomId("101");

        userDAO = new UserDAO();
        userDAO.setId("user_id_test");
        userDAO.setDocumentType("cc");
        userDAO.setEndDate("25/04/2024");
        userDAO.setName("Walther test");
        userDAO.setStartDate("22/04/2024");
        userDAO.setIdentification(5555555);
        userDAO.setRoomId("101");

        userDTO = new UserDTO();
        userDTO.setDocumentType("cc");
        userDTO.setEndDate("25/04/2024");
        userDTO.setName("Walther test");
        userDTO.setStartDate("22/04/2024");
        userDTO.setIdentification(5555555);
        userDTO.setRoomId("101");

        room = new Room();
        room.setRoomType("Sencilla");
        room.setRoomState("Disponible");
        room.setNightValue(100000);
        room.setNumberGuests(2);
        room.setHotelId("Hotel_id");
        room.setRoomId("401");
        room.setUserId("User_test_id");
        room.setReservationDate("22/04/2024");
        room.setReservationStartDate("23/04/2024");
        room.setReservationEndDate("25/04/2024");

        roomDAO = new RoomDAO();
        roomDAO.setRoomType("Sencilla");
        roomDAO.setRoomState("Disponible");
        roomDAO.setNightValue(100000);
        roomDAO.setNumberGuests(2);
        roomDAO.setHotelId("Hotel_id");
        roomDAO.setRoomId("401");
        roomDAO.setUserId("User_test_id");
        roomDAO.setReservationDate("22/04/2024");
        roomDAO.setReservationStartDate("23/04/2024");
        roomDAO.setReservationEndDate("25/04/2024");

        roomDTO = new RoomDTO();
        roomDTO.setRoomType("Sencilla");
        roomDTO.setRoomState("Disponible");
        roomDTO.setNightValue(100000);
        roomDTO.setNumberGuests(2);
    }

    @Test
    void couldGetUserByDTO() {
        when(modelMapper.map(any(UserDTO.class), any())).thenReturn(user);
        var userTest = genericModelMapper.getUserSinceUserDTO(userDTO);

        Assertions.assertNotNull(genericModelMapper);
        Assertions.assertEquals(user.getIdentification(), userTest.getIdentification());
        Assertions.assertEquals(user.getName(), userTest.getName());
        Assertions.assertEquals(user.getId(), userTest.getId());
        Assertions.assertEquals(user.getDocumentType(), userTest.getDocumentType());
        Assertions.assertEquals(user.getEndDate(), userTest.getEndDate());
        Assertions.assertEquals(user.getStartDate(), userTest.getStartDate());
        Assertions.assertEquals(user.getRoomId(), userTest.getRoomId());


    }

    @Test
    void couldGetUserByDAO() {
        when(modelMapper.map(any(UserDAO.class), any())).thenReturn(user);
        var userTest = genericModelMapper.getUserSinceUserDAO(userDAO);
        Assertions.assertNotNull(genericModelMapper);
        Assertions.assertEquals(user.getIdentification(), userTest.getIdentification());
        Assertions.assertEquals(user.getName(), userTest.getName());
        Assertions.assertEquals(user.getId(), userTest.getId());
        Assertions.assertEquals(user.getDocumentType(), userTest.getDocumentType());
        Assertions.assertEquals(user.getEndDate(), userTest.getEndDate());
        Assertions.assertEquals(user.getStartDate(), userTest.getStartDate());
        Assertions.assertEquals(user.getRoomId(), userTest.getRoomId());
    }

    @Test
    void couldGetDTOByUser() {
        when(modelMapper.map(any(User.class), any())).thenReturn(userDTO);
        var userTest = genericModelMapper.getUserDTOSinceUser(user);
        Assertions.assertNotNull(genericModelMapper);
        Assertions.assertEquals(user.getIdentification(), userTest.getIdentification());
        Assertions.assertEquals(user.getName(), userTest.getName());
        Assertions.assertEquals(user.getDocumentType(), userTest.getDocumentType());
        Assertions.assertEquals(user.getEndDate(), userTest.getEndDate());
        Assertions.assertEquals(user.getStartDate(), userTest.getStartDate());
        Assertions.assertEquals(user.getRoomId(), userTest.getRoomId());
    }

    @Test
    void couldGetDAOByUser() {
        when(modelMapper.map(any(User.class), any())).thenReturn(userDAO);
        var userTest = genericModelMapper.getUserDAOSinceUser(user);
        Assertions.assertNotNull(genericModelMapper);
        Assertions.assertEquals(user.getIdentification(), userTest.getIdentification());
        Assertions.assertEquals(user.getName(), userTest.getName());
        Assertions.assertEquals(user.getId(), userTest.getId());
        Assertions.assertEquals(user.getDocumentType(), userTest.getDocumentType());
        Assertions.assertEquals(user.getEndDate(), userTest.getEndDate());
        Assertions.assertEquals(user.getStartDate(), userTest.getStartDate());
        Assertions.assertEquals(user.getRoomId(), userTest.getRoomId());
    }

    @Test
    void couldGetRoomByRoomDAO() {
        when(modelMapper.map(any(Room.class), any())).thenReturn(roomDAO);
        var roomTest = genericModelMapper.getRoomDAOSinceRoom(room);

        Assertions.assertNotNull(genericModelMapper);
        Assertions.assertEquals(room.getRoomType(), roomTest.getRoomType());
        Assertions.assertEquals(room.getRoomState(), roomTest.getRoomState());
        Assertions.assertEquals(room.getNightValue(), roomTest.getNightValue());
        Assertions.assertEquals(room.getNumberGuests(), roomTest.getNumberGuests());
        Assertions.assertEquals(room.getHotelId(), roomTest.getHotelId());
        Assertions.assertEquals(room.getRoomId(), roomTest.getRoomId());
        Assertions.assertEquals(room.getUserId(), roomTest.getUserId());
        Assertions.assertEquals(room.getReservationDate(), roomTest.getReservationDate());
        Assertions.assertEquals(room.getReservationStartDate(), roomTest.getReservationStartDate());
        Assertions.assertEquals(room.getReservationEndDate(), roomTest.getReservationEndDate());

    }

    @Test
    void couldGetRoomByRoomDTO() {
        when(modelMapper.map(any(Room.class), any())).thenReturn(roomDTO);
        var roomTest = genericModelMapper.getRoomDTOSinceRoom(room);

        roomTest.setRoomType("Sencilla");
        roomTest.setRoomState("Disponible");
        roomTest.setNightValue(100000);
        roomTest.setNumberGuests(2);
        roomTest.setRoomId("401");
        roomTest.setUserId("User_test_id");
        roomTest.setReservationDate("22/04/2024");
        roomTest.setReservationStartDate("23/04/2024");
        roomTest.setReservationEndDate("25/04/2024");

        Assertions.assertNotNull(genericModelMapper);
        Assertions.assertEquals(room.getRoomType(), roomTest.getRoomType());
        Assertions.assertEquals(room.getRoomState(), roomTest.getRoomState());
        Assertions.assertEquals(room.getNightValue(), roomTest.getNightValue());
        Assertions.assertEquals(room.getNumberGuests(), roomTest.getNumberGuests());
        Assertions.assertEquals(room.getRoomId(), roomTest.getRoomId());
        Assertions.assertEquals(room.getUserId(), roomTest.getUserId());
        Assertions.assertEquals(room.getReservationDate(), roomTest.getReservationDate());
        Assertions.assertEquals(room.getReservationStartDate(), roomTest.getReservationStartDate());
        Assertions.assertEquals(room.getReservationEndDate(), roomTest.getReservationEndDate());

    }

    @Test
    void couldGetRoomDAOByRoom() {
        when(modelMapper.map(any(RoomDAO.class), any())).thenReturn(room);
        var roomTest = genericModelMapper.getRoomSinceRoomDAO(roomDAO);

        Assertions.assertNotNull(genericModelMapper);
        Assertions.assertEquals(room.getRoomType(), roomTest.getRoomType());
        Assertions.assertEquals(room.getRoomState(), roomTest.getRoomState());
        Assertions.assertEquals(room.getNightValue(), roomTest.getNightValue());
        Assertions.assertEquals(room.getNumberGuests(), roomTest.getNumberGuests());
        Assertions.assertEquals(room.getHotelId(), roomTest.getHotelId());
        Assertions.assertEquals(room.getRoomId(), roomTest.getRoomId());
        Assertions.assertEquals(room.getUserId(), roomTest.getUserId());
        Assertions.assertEquals(room.getReservationDate(), roomTest.getReservationDate());
        Assertions.assertEquals(room.getReservationStartDate(), roomTest.getReservationStartDate());
        Assertions.assertEquals(room.getReservationEndDate(), roomTest.getReservationEndDate());

    }

    @Test
    void couldGetRoomDTOByRoom() {
        when(modelMapper.map(any(RoomDTO.class), any())).thenReturn(room);
        var roomTest = genericModelMapper.getRoomSinceRoomDTO(roomDTO);

        Assertions.assertNotNull(genericModelMapper);
        Assertions.assertEquals(room.getRoomType(), roomTest.getRoomType());
        Assertions.assertEquals(room.getRoomState(), roomTest.getRoomState());
        Assertions.assertEquals(room.getNightValue(), roomTest.getNightValue());
        Assertions.assertEquals(room.getNumberGuests(), roomTest.getNumberGuests());

    }
}
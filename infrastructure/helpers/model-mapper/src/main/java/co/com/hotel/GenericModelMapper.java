package co.com.hotel;

import co.com.hotel.dao.RoomDAO;
import co.com.hotel.dao.UserDAO;
import co.com.hotel.api.dto.RoomDTO;
import co.com.hotel.api.dto.UserDTO;
import co.com.hotel.room.Room;
import co.com.hotel.user.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class GenericModelMapper {
    private final ModelMapper modelMapper;

    public GenericModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public User getUserSinceUserDAO(UserDAO userDAO) {
        return modelMapper.map(userDAO, User.class);
    }

    public UserDAO getUserDAOSinceUser(User user) {
        return modelMapper.map(user, UserDAO.class);
    }

    public User getUserSinceUserDTO(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    public UserDTO getUserDTOSinceUser(User user) {
        return modelMapper.map(user, UserDTO.class);
    }


    public Room getRoomSinceRoomDAO(RoomDAO roomDAO) {
        return modelMapper.map(roomDAO, Room.class);
    }

    public RoomDAO getRoomDAOSinceRoom(Room room) {
        return modelMapper.map(room, RoomDAO.class);
    }

    public Room getRoomSinceRoomDTO(RoomDTO roomDTO) {
        return modelMapper.map(roomDTO, Room.class);
    }

    public RoomDTO getRoomDTOSinceRoom(Room room) {
        return modelMapper.map(room, RoomDTO.class);
    }
}

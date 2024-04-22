package co.com.hotel.api.util;

import co.com.hotel.GenericModelMapper;
import co.com.hotel.api.dto.ResponseDTO;
import co.com.hotel.room.Room;
import co.com.hotel.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class ResponseUtil {

    private final GenericModelMapper genericModelMapper;

    public ResponseUtil(GenericModelMapper genericModelMapper) {
        this.genericModelMapper = genericModelMapper;
    }

    public Mono<ServerResponse> buildResponseRegisterRoom(Room room) {
        return buildResponseDTO(HttpStatus.CREATED, "Created room",
                genericModelMapper.getRoomDTOSinceRoom(room));
    }

    public Mono<ServerResponse> buildResponseFindRooms(List<Room> roomList) {
        var rooms = roomList.stream().map(genericModelMapper::getRoomDTOSinceRoom).toList();
        return buildResponseDTO(HttpStatus.OK, "Listed rooms", rooms);
    }

    public Mono<ServerResponse> buildResponseDeleteRoom(Room room) {
        return buildResponseDTO(HttpStatus.OK, "Deleted room", genericModelMapper.getRoomDTOSinceRoom(room));
    }

    public Mono<ServerResponse> buildResponseRegisterUser(User user) {
        return buildResponseDTO(HttpStatus.CREATED, "Created User",
                genericModelMapper.getUserDTOSinceUser(user));
    }

    private Mono<ServerResponse> buildResponseDTO(HttpStatus status, String description, Object data) {
        var responseDto = new ResponseDTO<Object>();
        responseDto.setData(data);
        responseDto.setStatus(status.toString());
        responseDto.setMessage(description);
        return ServerResponse.status(status).bodyValue(responseDto);
    }
}

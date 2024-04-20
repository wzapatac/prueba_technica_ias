package co.com.hotel.util;

import co.com.hotel.dto.ResponseDTO;
import co.com.hotel.dto.RoomDTO;
import co.com.hotel.dto.UserDTO;
import co.com.hotel.room.Room;
import co.com.hotel.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

public class ResponseUtil {

    public Mono<ServerResponse> buildResponseRegisterRoom(Room room) {
        return buildResponseDTO(HttpStatus.CREATED, "Created room", room);
    }

    public Mono<ServerResponse> buildResponseFindRooms(List<Room> roomList) {
        return buildResponseDTO(HttpStatus.OK, "Listed rooms", roomList);
    }

    public Mono<ServerResponse> buildResponseDeleteRoom(Room room) {
        return buildResponseDTO(HttpStatus.OK, "Deleted room", room);
    }

    public Mono<ServerResponse> buildResponseRegisterUser(User user) {
        return buildResponseDTO(HttpStatus.CREATED, "Created User", user);
    }

    private Mono<ServerResponse> buildResponseDTO(HttpStatus status, String description, Object data) {
        var responseDto = new ResponseDTO<Object>();
        responseDto.setData(data);
        responseDto.setStatus(status.toString());
        responseDto.setMessage(description);
        return ServerResponse.status(status).bodyValue(responseDto);
    }
}

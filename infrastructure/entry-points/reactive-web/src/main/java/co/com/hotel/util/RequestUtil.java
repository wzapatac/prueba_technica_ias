package co.com.hotel.util;

import co.com.hotel.GenericModelMapper;
import co.com.hotel.dto.RoomDTO;
import co.com.hotel.dto.UserDTO;
import co.com.hotel.room.Room;
import co.com.hotel.user.User;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class RequestUtil {

    private final GenericModelMapper genericModelMapper;

    public RequestUtil(GenericModelMapper genericModelMapper) {
        this.genericModelMapper = genericModelMapper;
    }

    public Mono<Room> buildRequestRegisterRoom(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(RoomDTO.class)
                .map(genericModelMapper::getRoomSinceRoomDTO);
    }

    public Mono<String> buildRequestFindRooms(ServerRequest serverRequest) {
        return Mono.just(serverRequest.headers().firstHeader("hotel-id"));
    }

    public Mono<Room> buildRequestDeleteRoom(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(RoomDTO.class)
                .map(genericModelMapper::getRoomSinceRoomDTO);
    }

    public Mono<User> buildRequestRegisterUser(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(UserDTO.class)
                .map(genericModelMapper::getUserSinceUserDTO);
    }
}

package co.com.hotel.api.util;

import co.com.hotel.GenericModelMapper;
import co.com.hotel.api.dto.RoomDTO;
import co.com.hotel.api.dto.RoomDeleteRequest;
import co.com.hotel.api.dto.RoomWithFilterRequest;
import co.com.hotel.api.dto.UserDTO;
import co.com.hotel.room.Room;
import co.com.hotel.user.User;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
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

    public Mono<RoomWithFilterRequest> buildRequestFindRooms(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(RoomWithFilterRequest.class)
                .map(roomWithFilterRequest -> {
                    roomWithFilterRequest.setHotelId(serverRequest.headers().firstHeader("hotel-id"));
                    return roomWithFilterRequest;
                });
    }

    public Mono<RoomDeleteRequest> buildRequestDeleteRoom(ServerRequest serverRequest) {
        var requestDelete = new RoomDeleteRequest();
        requestDelete.setHotelId(serverRequest.headers().firstHeader("hotel-id"));
        requestDelete.setRoomId(serverRequest.pathVariable("id"));
        return Mono.just(requestDelete);
    }

    public Mono<User> buildRequestRegisterUser(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(UserDTO.class)
                .map(genericModelMapper::getUserSinceUserDTO);
    }
}

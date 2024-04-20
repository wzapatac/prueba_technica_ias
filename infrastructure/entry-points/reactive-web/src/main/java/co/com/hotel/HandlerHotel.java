package co.com.hotel;

import co.com.hotel.room.RoomUseCase;
import co.com.hotel.user.UserUserCase;
import co.com.hotel.util.RequestUtil;
import co.com.hotel.util.ResponseUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class HandlerHotel {


    private final RoomUseCase roomUseCase;
    private final UserUserCase userUserCase;
    private final RequestUtil requestUtil;
    private final ResponseUtil responseUtil;

    public HandlerHotel(RoomUseCase roomUseCase, UserUserCase userUserCase, RequestUtil requestUtil,
                        ResponseUtil responseUtil) {
        this.roomUseCase = roomUseCase;
        this.userUserCase = userUserCase;
        this.requestUtil = requestUtil;
        this.responseUtil = responseUtil;
    }

    public Mono<ServerResponse> registerRoom(ServerRequest serverRequest) {
        return requestUtil.buildRequestRegisterRoom(serverRequest)
                .flatMap(roomUseCase::registerRoom).flatMap(responseUtil::buildResponseRegisterRoom);
    }
    public Mono<ServerResponse> findRooms(ServerRequest serverRequest) {
        return null;
    }

    public Mono<ServerResponse> deleteRoom(ServerRequest serverRequest) {
        return null;
    }

    public Mono<ServerResponse> registerUser(ServerRequest serverRequest) {
        return null;
    }
}

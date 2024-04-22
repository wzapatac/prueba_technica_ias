package co.com.hotel.api;

import co.com.hotel.room.RoomFilter;
import co.com.hotel.room.RoomUseCase;
import co.com.hotel.user.UserUserCase;
import co.com.hotel.api.util.RequestUtil;
import co.com.hotel.api.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class HandlerHotel {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
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
                .flatMap(roomUseCase::registerRoom).flatMap(responseUtil::buildResponseRegisterRoom)
                .doOnSuccess(isOk -> logger.info("Room created!!"))
                .doOnError(error -> logger.error(error.getMessage()));
    }

    public Mono<ServerResponse> findRooms(ServerRequest serverRequest) {
        return requestUtil.buildRequestFindRooms(serverRequest)
                .flatMap(roomWithFilterRequest -> {
                    var roomFilter = new RoomFilter();
                    roomFilter.setRoomState(roomWithFilterRequest.getRoomState());
                    roomFilter.setRoomType(roomWithFilterRequest.getRoomType());
                    roomFilter.setNumberGuests(roomWithFilterRequest.getNumberGuests());
                    roomFilter.setReservationDate(roomWithFilterRequest.getReservationDate());
                    return roomUseCase.findRooms(roomFilter, roomWithFilterRequest.getHotelId());
                })
                .flatMap(responseUtil::buildResponseFindRooms)
                .doOnSuccess(isOk -> logger.info("Rooms listed!!"))
                .doOnError(error -> logger.error(error.getMessage()));
    }

    public Mono<ServerResponse> deleteRoom(ServerRequest serverRequest) {
        return requestUtil.buildRequestDeleteRoom(serverRequest)
                .flatMap(roomDeleteRequest -> roomUseCase
                        .deleteRoom(roomDeleteRequest.getHotelId(), roomDeleteRequest.getRoomId()))
                .flatMap(responseUtil::buildResponseDeleteRoom)
                .doOnSuccess(isOk -> logger.info("Room deleted!!"))
                .doOnError(error -> logger.error(error.getMessage()));
    }

    public Mono<ServerResponse> registerUser(ServerRequest serverRequest) {
        return requestUtil.buildRequestRegisterUser(serverRequest)
                .flatMap(userUserCase::registerUser).flatMap(responseUtil::buildResponseRegisterUser)
                .doOnSuccess(isOk -> logger.info("User created!!"))
                .doOnError(error -> logger.error(error.getMessage()));
    }
}

package co.com.hotel.user;

import co.com.hotel.api.exception.BusinessException;
import co.com.hotel.api.exception.message.BusinessExceptionMessage;
import co.com.hotel.room.RoomStatus;
import co.com.hotel.room.gateways.RoomRepository;
import co.com.hotel.user.gateways.UserRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Service
public class UserUserCase {
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    public UserUserCase(UserRepository userRepository, RoomRepository roomRepository) {
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
    }

    public Mono<User> registerUser(User user) {

        return roomRepository.findRoomById("hotel_ias_med", user.getRoomId())
                .switchIfEmpty(Mono.defer(() -> Mono.error(new BusinessException(BusinessExceptionMessage.ROOM_NOT_EXIST))))
                .filter(roomResponse -> RoomStatus.AVAILABLE.getStatus().equals(roomResponse.getRoomState()))
                .switchIfEmpty(Mono.defer(() -> Mono.error(new BusinessException(BusinessExceptionMessage.ROOM_NOT_AVAILABLE))))
                .flatMap(room ->
                        validateRequiredFields(user)
                                .flatMap(userRequest -> {
                                    userRequest.setId(user.getDocumentType().concat("_")
                                            .concat(user.getIdentification().toString()).concat("_")
                                            .concat(user.getRoomId()));
                                    return userRepository.registerUser(userRequest);
                                })
                                .map(userResponse -> {
                                    room.setRoomState(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                                            .equals(userResponse.getStartDate())
                                            ? RoomStatus.NOT_AVAILABLE.getStatus() : RoomStatus.AVAILABLE.getStatus());
                                    room.setUserId(userResponse.getId());
                                    room.setReservationDate(LocalDateTime.now()
                                            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss")));
                                    room.setReservationStartDate(userResponse.getStartDate());
                                    room.setReservationEndDate(userResponse.getEndDate());
                                    return roomRepository.registerRoom(room).subscribe();
                                })).thenReturn(user);
    }

    private Mono<User> validateRequiredFields(User user) {
        return Mono.just(user)
                .filter(userValidateIdentification -> Objects.nonNull(userValidateIdentification.getIdentification()))
                .switchIfEmpty(Mono.defer(() -> Mono.error(new BusinessException(BusinessExceptionMessage.IDENTIFICATION_NULL))))
                .filter(userValidateName -> Objects.nonNull(userValidateName.getName()))
                .switchIfEmpty(Mono.defer(() -> Mono.error(new BusinessException(BusinessExceptionMessage.NAME_NULL))))
                .filter(userValidateStartDate -> Objects.nonNull(userValidateStartDate.getStartDate()))
                .switchIfEmpty(Mono.defer(() -> Mono.error(new BusinessException(BusinessExceptionMessage.START_DATE_NULL))))
                .filter(userValidateEndDate -> Objects.nonNull(userValidateEndDate.getEndDate()))
                .switchIfEmpty(Mono.defer(() -> Mono.error(new BusinessException(BusinessExceptionMessage.END_DATE_NULL))))
                .filter(userValidateRoomId -> Objects.nonNull(userValidateRoomId.getRoomId()))
                .switchIfEmpty(Mono.defer(() -> Mono.error(new BusinessException(BusinessExceptionMessage.ROOM_NULL))))
                .filter(userValidateDocumentLength -> String.valueOf(userValidateDocumentLength.getIdentification())
                        .length() <= 12)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new BusinessException(BusinessExceptionMessage.IDENTIFICATION_LENGTH))))
                .filter(userValidateNameLength -> userValidateNameLength.getName().length() <= 30)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new BusinessException(BusinessExceptionMessage.NAME_LENGTH))))
                .filter(userValidateNameNumber -> userValidateNameNumber.getName().matches("[A-Za-z ]*"))
                .switchIfEmpty(Mono.defer(() -> Mono.error(new BusinessException(BusinessExceptionMessage.NAME_NUMBER))))
                .thenReturn(user);
    }
}

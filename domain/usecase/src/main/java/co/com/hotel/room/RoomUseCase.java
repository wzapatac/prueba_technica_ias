package co.com.hotel.room;


import co.com.hotel.room.gateways.RoomRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class RoomUseCase {
    private final RoomRepository roomRepository;
    private Float counter = 0.0f;
    private static final String HOTEL_ID = "hotel_ias_med";

    public RoomUseCase(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Mono<Void> applyReservations() {
        List<Room> rooms = new ArrayList<>();
        var date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        var roomFilter = new RoomFilter();
        roomFilter.setRoomType("");
        roomFilter.setReservationDate("");
        roomFilter.setNumberGuests(0);
        roomFilter.setRoomState("");

        return roomRepository.findRooms(roomFilter, HOTEL_ID)
                .map(roomList -> {
                    rooms.addAll(roomList.stream()
                            .filter(room -> Objects.nonNull(room.getReservationStartDate()))
                            .filter(room -> room.getReservationStartDate().equals(date))
                            .toList());
                    return rooms;
                }).map(roomList ->
                        roomList.stream().map(room -> {
                            room.setRoomState(RoomStatus.NOT_AVAILABLE.getStatus());
                            return roomRepository.registerRoom(room);
                        }).toList()
                ).then();
    }

    public Mono<Void> desApplyReservations() {
        List<Room> rooms = new ArrayList<>();
        var date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        var roomFilter = new RoomFilter();
        roomFilter.setRoomType("");
        roomFilter.setReservationDate("");
        roomFilter.setNumberGuests(0);
        roomFilter.setRoomState("");

        return roomRepository.findRooms(roomFilter, HOTEL_ID)
                .map(roomList -> {
                    rooms.addAll(roomList.stream()
                            .filter(room -> Objects.nonNull(room.getReservationEndDate()))
                            .filter(room -> room.getReservationEndDate().equals(date))
                            .toList());
                    return rooms;
                }).map(roomList ->
                        roomList.stream().map(room -> {
                            room.setRoomState(RoomStatus.AVAILABLE.getStatus());
                            room.setUserId(null);
                            room.setReservationStartDate(null);
                            room.setReservationEndDate(null);
                            room.setReservationDate(null);
                            return roomRepository.registerRoom(room);
                        }).toList()
                ).then();
    }

    public Mono<Room> registerRoom(Room room) {
        counter++;
        room.setHotelId(HOTEL_ID);
        room.setRoomId(getRoomId());
        room.setRoomState(RoomStatus.AVAILABLE.getStatus());
        return roomRepository.registerRoom(room);
    }

    public Mono<Room> deleteRoom(String hotelId, String roomId) {
        return roomRepository.findRoomById(hotelId, roomId)
                .flatMap(room -> {
                    room.setRoomState(RoomStatus.NOT_AVAILABLE.getStatus());
                    return roomRepository.registerRoom(room);
                });
    }

    public Mono<List<Room>> findRooms(RoomFilter roomFilter, String hotelId) {
        return roomRepository.findRooms(roomFilter, hotelId);
    }


    private String getRoomId() {

        var numberString = String.valueOf(counter / 10);
        var numberOne = (numberString.charAt(2) == '0') ? numberString.substring(0, 1) : Integer.parseInt(numberString.substring(0, 1)) + 1;
        var numberTwo = (numberString.charAt(2) == '0') ? "1" : "0";
        var numberThree = Integer.parseInt(numberString.substring(2, 3));

        return numberOne + numberTwo + numberThree;
    }
}

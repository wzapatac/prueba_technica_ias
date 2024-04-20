package co.com.hotel.room;


import co.com.hotel.room.gateways.RoomRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class RoomUseCase {
    private final RoomRepository roomRepository;

    public RoomUseCase(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Mono<Room> registerRoom(Room room) {
        return roomRepository.registerRoom(room);
    }

    public Mono<Room> deleteRoom(Room room) {
        return roomRepository.deleteRoom(room);
    }

    public Mono<List<Room>> findRooms(String hotelId) {
        return roomRepository.findRooms(hotelId);
    }
}

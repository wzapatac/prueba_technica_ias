package co.com.hotel.room.gateways;

import co.com.hotel.room.Room;
import co.com.hotel.room.RoomFilter;
import reactor.core.publisher.Mono;

import java.util.List;

public interface RoomRepository {
    Mono<Room> registerRoom(Room room);

    Mono<List<Room>> findRooms(RoomFilter roomFilter, String hotelId);

    Mono<Room> findRoomById(String hotelId, String roomId);

}

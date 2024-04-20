package co.com.hotel.room.gateways;

import co.com.hotel.room.Room;
import reactor.core.publisher.Mono;

import java.util.List;

public interface RoomRepository {
    Mono<Room> registerRoom(Room room);

    Mono<List<Room>> findRooms(String hotelId);

    Mono<Room> deleteRoom(Room room);

}

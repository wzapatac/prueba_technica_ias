package co.com.hotel.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "hotel.paths")
public class HotelRoutes {
    private String registerRoom;
    private String findRooms;
    private String deleteRoom;
    private String registerUser;

    public String getRegisterRoom() {
        return registerRoom;
    }

    public void setRegisterRoom(String registerRoom) {
        this.registerRoom = registerRoom;
    }

    public String getFindRooms() {
        return findRooms;
    }

    public void setFindRooms(String findRoom) {
        this.findRooms = findRoom;
    }

    public String getDeleteRoom() {
        return deleteRoom;
    }

    public void setDeleteRoom(String deleteRoom) {
        this.deleteRoom = deleteRoom;
    }

    public String getRegisterUser() {
        return registerUser;
    }

    public void setRegisterUser(String registerUser) {
        this.registerUser = registerUser;
    }
}

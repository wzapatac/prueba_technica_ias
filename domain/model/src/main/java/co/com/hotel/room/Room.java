package co.com.hotel.room;

public class Room {

    private String hotelId;
    private String roomId;
    private String roomType;
    private String RoomState;
    private Integer nightValue;
    private String userId;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getRoomState() {
        return RoomState;
    }

    public void setRoomState(String roomState) {
        RoomState = roomState;
    }

    public Integer getNightValue() {
        return nightValue;
    }

    public void setNightValue(Integer nightValue) {
        this.nightValue = nightValue;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

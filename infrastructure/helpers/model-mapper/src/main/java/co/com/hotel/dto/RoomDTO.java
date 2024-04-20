package co.com.hotel.dto;

public class RoomDTO {
    private String roomType;
    private String RoomState;
    private Integer nightValue;
    private String userId;

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

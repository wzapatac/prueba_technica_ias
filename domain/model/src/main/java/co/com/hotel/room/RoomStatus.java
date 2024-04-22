package co.com.hotel.room;

public enum RoomStatus {
    AVAILABLE("Disponible"),
    NOT_AVAILABLE("No disponible");

    private final String status;


    RoomStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}

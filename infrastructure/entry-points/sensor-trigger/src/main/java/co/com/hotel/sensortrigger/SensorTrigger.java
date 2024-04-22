package co.com.hotel.sensortrigger;

import co.com.hotel.room.RoomUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;

@EnableScheduling
@Component
public class SensorTrigger {
    private final RoomUseCase roomUseCase;

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String TIME_ZONE = "America/Bogota";
    private static final String CRON_STRING_APPLY = "0 01 01 * * *";
    private static final String CRON_STRING_DES_APPLY = "0 59 11 * * *";

    public SensorTrigger(RoomUseCase roomUseCase) {
        this.roomUseCase = roomUseCase;
    }


    @Scheduled(cron = CRON_STRING_APPLY, zone = TIME_ZONE)
    public Disposable applyReservation() {
        return roomUseCase.applyReservations()
                .doOnSuccess(isOk -> logger.info("Reservations applied!!"))
                .doOnError(error->logger.error("Apply reservations failed!!"))
                .subscribe();
    }

    @Scheduled(cron = CRON_STRING_DES_APPLY, zone = TIME_ZONE)
    public Disposable desApplyReservation() {
        return roomUseCase.desApplyReservations()
                .doOnSuccess(isOk -> logger.info("Reservations des-applied!!"))
                .doOnError(error->logger.error("Des-apply reservations failed!!"))
                .subscribe();
    }
}

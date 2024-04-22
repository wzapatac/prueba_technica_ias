package co.com.hotel.api;

import co.com.hotel.api.config.HotelRoutes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRestHotel {

    private final HotelRoutes hotelRoutes;

    public RouterRestHotel(HotelRoutes hotelRoutes) {
        this.hotelRoutes = hotelRoutes;
    }

    @Bean
    public RouterFunction<ServerResponse> routerFunction(HandlerHotel handlerHotel) {
        return route(POST(hotelRoutes.getRegisterRoom()), handlerHotel::registerRoom)
                .andRoute(POST(hotelRoutes.getRegisterUser()), handlerHotel::registerUser)
                .andRoute(POST(hotelRoutes.getFindRooms()), handlerHotel::findRooms)
                .andRoute(DELETE(hotelRoutes.getDeleteRoom()), handlerHotel::deleteRoom);
    }
}
